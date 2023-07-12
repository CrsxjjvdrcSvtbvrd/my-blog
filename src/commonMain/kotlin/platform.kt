expect object FileHandle {
    fun readString(path: String): String
    fun readBinary(path: String): ByteArray
    fun write(path: String, content: String)
    fun write(path: String, content: ByteArray)
    fun append(path: String, content: String)
    fun append(path: String, content: ByteArray)
    fun exist(path: String): Boolean
    fun mkdir(path: String): Boolean
    fun copy(src: String, dst: String): Boolean
    fun copyDir(src: String, dst: String): Boolean
}