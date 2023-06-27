import kotlin.native.concurrent.ThreadLocal

abstract class Core {
    abstract val fileHandle: FileHandle?
    abstract val socket: Socket?
    abstract val serverSocket: ServerSocket?
}
interface Application {
    fun start(vararg args: String)

    @ThreadLocal
    companion object {
        lateinit var core: Core
    }
}
class Launcher(val core: Core, val app: Application) {
    fun start(vararg args: String) {
        app.start(*args)
    }
    init {
        Application.core = core
//        app.start()
    }
}
interface FileHandle {
    fun readString(path: String): String
    fun readBinary(path: String): ByteArray
    fun write(path: String, content: String)
    fun write(path: String, content: ByteArray)
    fun append(path: String, content: String)
    fun append(path: String, content: ByteArray)
    fun exist(path: String): Boolean
    fun mkdir(path: String): Boolean
    fun copy(src: String, dst: String): Boolean
}
abstract class Socket() {
    abstract fun connect(host: String,port: Int)
    abstract fun close()
    var state: Int = 1
    abstract fun send(content: String)
    abstract fun send(content: ByteArray)
    companion object {
        const val connected = 1
        const val closed = 0
    }
}
abstract class ServerSocket() {
    abstract fun bind(port: Int)
    abstract fun receive(): Socket
    abstract fun close()
}