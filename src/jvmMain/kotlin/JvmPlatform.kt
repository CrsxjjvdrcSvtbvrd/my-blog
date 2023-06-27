import kotlin.io.path.*

class JvmCore: Core() {
    override val fileHandle: FileHandle
        get() = JvmFileHandle()
    override val serverSocket: ServerSocket?
        get() = null
    override val socket: Socket?
        get() = null
}

class JvmFileHandle: FileHandle {
    override fun readString(path: String): String {
        return Path(path).readText(Charsets.UTF_8)
    }

    override fun readBinary(path: String): ByteArray {
        return Path(path).readBytes()
    }

    override fun write(path: String, content: String) {
        Path(path).writeText(content,Charsets.UTF_8)
    }

    override fun write(path: String, content: ByteArray) {
        Path(path).writeBytes(content)
    }

    override fun append(path: String, content: String) {
        Path(path).appendText(content,Charsets.UTF_8)
    }

    override fun append(path: String, content: ByteArray) {
        Path(path).appendBytes(content)
    }

    override fun exist(path: String): Boolean {
        return Path(path).exists()
    }

    override fun mkdir(path: String): Boolean {
        runCatching {
            Path(path).createDirectories()
            return true
        }
        return false
    }

    override fun copy(src: String, dst: String): Boolean {
        runCatching {
            Path(src).copyTo(Path(dst))
            return true
        }
        return false
    }
}