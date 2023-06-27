class Template {
    companion object {
        const val root = "theme/"
        const val data = "data/"
        const val public = "public/"
        val functions = mutableMapOf<String, TemplateFunction>()
        init {
            functions["import"] = object: TemplateFunction {
                override fun exec(args: List<String>): String {
                    return import(args)
                }
            }
            functions["createPage"] = object : TemplateFunction {
                override fun exec(args: List<String>): String {
                    return createPage(args)
                }
            }
        }
        fun import(args: List<String>): String {
            if(args.isNotEmpty()){
                val temp = args[0]
                val map = mutableMapOf<String,String>()
                for(i in 1 until args.size) {
                    if(args[i].contains("=")){
                        args[i].split("=", limit = 2).let {
                            map[it[0]] = it[1]
                        }
                    }else{
                        map[args[i]] = ""
                    }
                }
                Application.core.fileHandle?.let {
                    if(it.exist("$data$temp.html")){
                        var html = it.readString("$data$temp.html")
                        html = html.parse()
                        html = html.parse(map)
                        return html
                    }else if(it.exist("$root$temp.html")){
                        var html = it.readString("$root$temp.html")
                        html = html.parse()
                        html = html.parse(map)
                        return html
                    }else{
                        return "template file $temp not found"
                    }
                }
                return "platform not support"
            }
            return "args = 0"
        }
        fun createPage(args: List<String>): String {
            if (args.size < 4) return "args < 4"
            if (Application.core.fileHandle == null) return "fileHandle is null"
            val fs = Application.core.fileHandle!!
            val dst = args[0]
            val temp1 = args[1]
            val temp2 = args[2]
            val md = fs.readString("$data${args[3]}")
            val data = Markdown2Html.getDataBlock(md)
            val content = Markdown2Html.removeDataBlock(md)//Markdown2Html.convert(Markdown2Html.removeDataBlock(md))
            val dataArg = mutableListOf<String>()
            data.forEach {
                dataArg.add("${it.key}=${it.value}")
            }
            dataArg.add("content=$content")
            dataArg.add("next=$dst")
            val args1 = mutableListOf(temp1)
            args1.addAll(dataArg)
            val html = import(args1)
            fs.write("$public$dst", html)
            val args2 = mutableListOf(temp2)
            args2.addAll(dataArg)
            val res = import(args2)
            if (args.size > 4 && args[4] == "false")
                return ""
            return res
        }
    }
    object Regex {
        val function = "\\{\\{(.*?)\\((.*?)\\)}}".toRegex()
        val variable = "\\{\\{([\\w.?#]+)}}".toRegex()
    }
}
fun String.parse(map: Map<String,String>): String {
    var result = this
    Template.Regex.variable.findAll(this).apply {
        forEach {
            var name = it.groupValues[1]
            var value = ""
            if(name.contains("?")){
                name.split("?").let {
                    name = it[0]
                    value = it[1]
                }
            }
            if(map.containsKey(name)){
                value = map[name]!!
            }
            result = result.replace(it.value, value)
        }
    }
    return result
}
fun String.parse(): String {
    var result = this
    Template.Regex.function.findAll(result).apply {
        forEach {
            val name = it.groupValues[1]
            val args = it.groupValues[2].split(",")
            if(Template.functions.containsKey(name)) {
                result = result.replace(it.value, Template.functions[name]?.exec(args)?:"")
            }
        }
    }
    return result
}
object Markdown2Html {
    val parseMap = listOf(
        Regex("(?m)^#\\s+(.*?)$", RegexOption.MULTILINE) to "<h1>\$1</h1>",
        Regex("(?m)^##\\s+(.*?)$", RegexOption.MULTILINE) to "<h2>\$1</h2>",
        Regex("(?m)^###\\s+(.*?)$", RegexOption.MULTILINE) to "<h3>\$1</h3>",
        Regex("(?m)^####\\s+(.*?)$", RegexOption.MULTILINE) to "<h4>\$1</h4>",
        Regex("(?m)^#####\\s+(.*?)$", RegexOption.MULTILINE) to "<h5>\$1</h5>",
        Regex("(?m)^######\\s+(.*?)$", RegexOption.MULTILINE) to "<h6>\$1</h6>",
        Regex("(?m)\\n(?!<\\/?\\w+>|\\s?\\*|\\s?[0-9]+|>|\\&gt;|-{5,})([^\\n]+)", RegexOption.MULTILINE) to "<p>\$1</p>",
        Regex("(?m)\\n(?:&gt;|\\>)\\W*(.*)", RegexOption.MULTILINE) to "<blockquote><p>\$1</p></blockquote>",
        Regex("(?m)\\n\\s?\\*\\s*(.*)", RegexOption.MULTILINE) to "<ul>\\n\\t<li>\$1</li>\\n</ul>",
        Regex("(?m)\\n\\s?[0-9]+\\.\\s*(.*)", RegexOption.MULTILINE) to "<ol>\\n\\t<li>\$1</li>\\n</ol>",
        Regex("(\\*\\*|__)(.*?)\\1") to "<strong>\$2</strong>",
        Regex("(\\*|_)(.*?)\\1") to "<em>\$2</em>",
        Regex("([^!])\\[([^\\[]+)\\]\\(([^\\)]+)\\)") to "\$1<a href=\\\"\$3\\\">\$2</a>",
        Regex("!\\[([^\\[]+)\\]\\(([^\\)]+)\\)") to "<img src=\\\"\$2\\\" alt=\\\"\$1\\\" />",
        Regex("\\~\\~(.*?)\\~\\~") to "<del>\$1</del>",
        Regex("/`(.*?)`") to "<code>\$1</code>",
        Regex("(?m)\\n-{5,}\\n", RegexOption.MULTILINE) to "<hr/>",
        Regex("<\\/([uo]l)>\\s*<\\1>") to "",
        Regex("(<\\/\\w+>)<\\/(blockquote)>\\s*<\\2>") to "\$1",
        Regex("[\\n]{1,}") to "\n",
    )
    val splitLineRegex = Regex("\r?\n")
    val dataBlockRegex = Regex("^---\n(.*?^)---", RegexOption.MULTILINE)
    val keyValueRegex = Regex("^\\s*(\\w+):\\s*(.*)$", RegexOption.MULTILINE)
    fun getDataBlock(md: String):Map<String,String> {
        val map = mutableMapOf<String,String>()
        val st = md.indexOf("---")
        if(st<0) return map
        val end = md.indexOf("---",st+3)
        val dataBlock = md.substring(st+3,end)
        dataBlock.split(splitLineRegex).forEach {
            if(it.isNotEmpty()){
                val kv = it.split(": ", limit = 2)
                if(kv.size>1){
                    map[kv[0]] = kv[1]
                }else{
                    map[kv[0]] = ""
                }
            }
        }
        return map
        /*val match = dataBlockRegex.find(md)
        val dataBlock = match?.value?:""
        val kv = keyValueRegex.findAll(dataBlock)
        val data = kv.associate {
            val (k,v) = it.destructured
            k to v
        }
        return data*/
    }
    fun removeDataBlock(md: String): String {
        val st = md.indexOf("---")
        if(st<0) return md
        val end = md.indexOf("---",st+3)
        return md.substring(end+4)
//        val match = dataBlockRegex.find(md)
//        val dataBlock = match?.value?:""
//        return md.replace(dataBlock, "")
    }
    fun convert(md: String): String {
        var res = md
        parseMap.forEach {
            runCatching {
                res = res.replace(it.first, it.second)
            }.onFailure {_->
                println("${it.first},${it.second}")
            }
        }
        return res
    }
}
interface TemplateFunction {
    fun exec(args: List<String>): String
}