package func

import FileHandle
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object Parser: SFunc{
    override fun eval(args: Array<String>): String {
        // TODO: not completed
        return ""
    }
    fun eval(html: String, map: Map<String, String>): String {
        return parse(parse(html, variables(map)))
    }
    private fun parse(html: String, map: Map<String,String>): String {
        var res = html
        variable.findAll(html).apply {
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
                res = res.replace(it.value, value)
            }
        }
        return res
    }
    private fun parse(html: String): String {
        var res = html
        function.findAll(html).apply {
            forEach {
                val name = it.groupValues[1]
                val args = it.groupValues[2].split(split).map { it.replace("\\,",",") }
                if(functions.containsKey(name)){
                    res = res.replace(it.value, functions[name]?.eval(args.toTypedArray())?:"")
                }
            }
        }
        return res
    }
    private val functions = mutableMapOf<String, SFunc>()
    private val config = mutableMapOf<String,String>()
    private val function = "\\{\\{(.*?)\\((.*?)\\)}}".toRegex()
    private val variable = "\\{\\{([\\w.?#]+)}}".toRegex()
    private val split = "(?<!\\\\),".toRegex()
    init {
        loadConfig()
//        println(config)
//        FileHandle.write("config1.json", Json.encodeToString(config))
        // TODO: setup functions
        functions["import"] = Import
        functions["build"] = Build
    }
    private fun loadConfig(){
        runCatching {
            val json = Json.parseToJsonElement(FileHandle.readString("config.json"))
            json.jsonObject.forEach {
                config[it.key] = it.value.jsonPrimitive.content
            }
        }.onFailure {
            it.printStackTrace()
        }
    }
    val theme: String
        get() {
            if(config.isEmpty()) loadConfig()
            return config["theme"]?:"chota"
        }
    fun variables(map: Map<String, String>): Map<String,String> {
        val res = mutableMapOf<String,String>()
        res.putAll(config)
        res.putAll(map)
        return res
    }
}