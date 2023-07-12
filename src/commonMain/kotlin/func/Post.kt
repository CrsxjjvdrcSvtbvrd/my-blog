package func

import FileHandle

object Post: Func {
    override fun eval(args: Array<String>) {
        if(args.size<2){
            println("usage: post [title]")
        }else{
            FileHandle.write("posts/${args[1]}.html","""---
                title:${args[1]}
                ---
                <h5>${args[1]}</h5>
                <p></p>""".trimIndent())
            println("created")
            println("use: {{build(posts/${args[1]}.html,layout,card,posts/${args[1]}.html}} in html file")
        }
    }
}