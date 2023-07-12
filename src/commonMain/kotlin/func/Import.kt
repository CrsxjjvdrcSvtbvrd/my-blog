package func

import FileHandle

object Import: SFunc {
    //import(layout,a=Hello\,World!,b=emm)
    override fun eval(args: Array<String>): String {
        if(args.isEmpty()) return ""
        val t = template(args[0])
        if(t.isNotEmpty()){
            val layout = FileHandle.readString(t)
            val map = mutableMapOf<String,String>()
            for(i in 1 until args.size){
                if(args[i].contains('=')){
                    val j = args[i].indexOf('=')
                    map[args[i].substring(0, j)] = args[i].substring(j+1)
                }
            }
            return Parser.eval(layout, map)
        }else{
            println("failed to import: ${args[0]}, file not exists")
        }
        return ""
    }
    fun template(name: String): String {
        val fs = arrayOf(name,"template/$name","themes/${Parser.theme}/$name")
        fs.forEach {
            if (FileHandle.exist(it)) return it
            if (FileHandle.exist("$it.html")) return "$it.html"
        }
        return ""
    }
}