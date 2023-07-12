import func.Build
import func.Import
import func.Init
import func.Post

fun start(args: Array<String>) {
    if(args.isEmpty()){
        Build.eval()
    }else {
        when(args[0]){
            "init"->{
                Init.eval(args)
            }
            "build"->{
                Build.eval(args)
            }
            "post"->{
                Post.eval(args)
            }
            "gen"->{
                if(args.size<2){
                    println("usage: gen file")
                }else{
                    val of = "public/"+if(args[1].endsWith(".html")) args[1] else args[1]+".html"
                    FileHandle.write(of,Import.eval(arrayOf(args[1],"")))
                }
            }
        }
    }
}