import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer

class Mingw64Core: Core() {
    override val fileHandle: FileHandle
        get() = Mingw64FileHandle()
    override val serverSocket: ServerSocket?
        get() = null
    override val socket: Socket?
        get() = null
}

class Mingw64FileHandle: FileHandle {
    fun readBuffer(path: String) = FileSystem.SYSTEM.source(path.toPath()).buffer()
    fun buffer(path: String) = FileSystem.SYSTEM.sink(path.toPath()).buffer()
    fun appendBuffer(path: String) = FileSystem.SYSTEM.appendingSink(path.toPath()).buffer()
    override fun readString(path: String): String {
        return readBuffer(path).readUtf8()
    }

    override fun readBinary(path: String): ByteArray {
        return readBuffer(path).readByteArray()
    }

    override fun write(path: String, content: String) {
        buffer(path).writeUtf8(content).flush()
    }

    override fun write(path: String, content: ByteArray) {
        buffer(path).write(content).flush()
    }

    override fun append(path: String, content: String) {
        appendBuffer(path).writeUtf8(content).flush()
    }

    override fun append(path: String, content: ByteArray) {
        appendBuffer(path).write(content).flush()
    }

    override fun exist(path: String): Boolean {
        return FileSystem.SYSTEM.exists(path.toPath())
    }

    override fun mkdir(path: String): Boolean {
        runCatching {
            FileSystem.SYSTEM.createDirectories(path.toPath())
            return true
        }
        return false
    }

    override fun copy(src: String, dst: String): Boolean {
        runCatching {
            FileSystem.SYSTEM.copy(src.toPath(), dst.toPath())
            return true
        }
        return false
    }
}