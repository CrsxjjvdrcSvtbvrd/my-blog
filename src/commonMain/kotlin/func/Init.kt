package func

import FileHandle
import utils.InternalFiles
import utils.createDir

/**
 * file tree:
 * |---posts/
 * |---themes/
 * |------chota/
 * |---------assets/
 * |---------template/
 * |---public/
 * |---template/
 * |-config.json
 * */
object Init : Func {
    override fun eval(args: Array<String>) {
        initDefaultFiles()
//        println(Import.eval(arrayOf("layout","logo=loli")))
//        Parser.init()
    }
    private fun initDefaultFiles(){
        FileHandle.createDir("posts")
        FileHandle.createDir("themes")
        FileHandle.createDir("themes/chota")
        FileHandle.createDir("themes/chota/assets")
        FileHandle.createDir("themes/chota/template")
        FileHandle.createDir("public")
        FileHandle.createDir("public")
        FileHandle.createDir("template")
        FileHandle.write("themes/chota/assets/chota.min.css", InternalFiles.chotaCSS)
        FileHandle.write("themes/chota/template/layout.html", InternalFiles.layoutHtml)
        FileHandle.write("config.json", InternalFiles.configJson)
        FileHandle.write("index.html", InternalFiles.layoutHtml)
    }
}