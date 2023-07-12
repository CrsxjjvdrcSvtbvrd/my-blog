package func

import FileHandle

object Build : SFunc {
    override fun eval(args: Array<String>): String {
        if(args.size<4) {
            println("usage: build(target.html.path, html.template, return.template, data.html.template")
            return ""
        }
        val targetPath = args[0]
        val layout = args[1]
        val ret = args[2]
        val data = args[3]
        if(!FileHandle.exist(data)){
            println("data file $data not exist")
            return ""
        }
        val dataBlock = getDataBlock(data)
        dataBlock["content"] = removeDataBlock(data)
        val layoutTemplate = Import.template(layout)
        val retTemplate = Import.template(ret)
        if(layoutTemplate.isNotEmpty()) {
            FileHandle.write("public/$targetPath", Parser.eval(FileHandle.readString(layoutTemplate), dataBlock))
            println("write: public/$targetPath")
        }else {
            println("failed to import: ${layout}, file not exists")
        }
        if(retTemplate.isNotEmpty()) {
            return Parser.eval(FileHandle.readString(retTemplate), dataBlock)
        }else{
            println("failed to import: ${ret}, file not exists")
        }
        return ""
//        val targetHtml = Import.eval()
    }
    fun eval(){
        if(FileHandle.exist("index.html")) {
            FileHandle.copyDir("themes/${Parser.theme}/assets","public/assets")
            FileHandle.write("public/index.html",Parser.eval(FileHandle.readString("index.html"), mapOf()))
            println("write: public/index.html")
        }else{
            println("index.html not exist")
        }
    }
    private val splitLineRegex = Regex("\r?\n")
    fun getDataBlock(md: String):MutableMap<String,String> {
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
    }
    fun removeDataBlock(md: String): String {
        val st = md.indexOf("---")
        if(st<0) return md
        val end = md.indexOf("---",st+3)
        return md.substring(end+4)
    }
}