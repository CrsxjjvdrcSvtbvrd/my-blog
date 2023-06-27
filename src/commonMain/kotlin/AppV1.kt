
class AppV1: Application {
    override fun start(vararg args: String) {
        Application.core.fileHandle?.let {fileHandle->
            println(args.joinToString(",") { it })
            if(args.isEmpty()) {
                fileHandle.write("public/index.html", fileHandle.readString("data/index.html").parse())
                println("生成完成")
            } else {
                var skip = false
                for(i in args.indices) {
                    if(skip){
                        skip = false
                        continue
                    }
                    when(args[i]) {
                        "post"-> {
                            val name = if (i + 1 < args.size) {
                                skip = true
                                args[i + 1]
                            } else {
                                println("输入文章名:")
                                readln()
                            }
                            if (!fileHandle.exist("data/post")) fileHandle.mkdir("data/post")
                            fileHandle.write(
                                "data/post/$name.html", """
                                    ---
                                    title: $name
                                    author: 
                                    description: 
                                    image: 
                                    ---
                                    <p>$name</p>
                                """.trimIndent()
                            )
                        }
                    }
                }
            }
        }
    }
}
