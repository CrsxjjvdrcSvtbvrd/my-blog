import json.JSONObject
import kotlin.io.path.Path

fun main(args: Array<String>) {
    Launcher(JvmCore(),AppV1()).start(*args)
//    val json = JSONObject()
//    json.put("name","value")
//    println(json.toString())
//    println(Path("").toAbsolutePath().toString())
//    AppV1().start()
}