package func

interface Func {
    fun eval(args: Array<String>)
}

interface SFunc {
    fun eval(args: Array<String>): String
}

// import(layout,logo=loli,description=Hello\,World!)
// build(target.html.path, html.template, return.template, content.html.path)