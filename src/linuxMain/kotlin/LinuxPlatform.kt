import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer

actual object FileHandle {
    fun readBuffer(path: String) = FileSystem.SYSTEM.source(path.toPath()).buffer()
    fun buffer(path: String) = FileSystem.SYSTEM.sink(path.toPath()).buffer()
    fun appendBuffer(path: String) = FileSystem.SYSTEM.appendingSink(path.toPath()).buffer()
    actual fun readString(path: String): String {
        return FileSystem.SYSTEM.read(path.toPath()){
            readUtf8()
        }
//        return readBuffer(path).readUtf8()
    }

    actual fun readBinary(path: String): ByteArray {
        return FileSystem.SYSTEM.read(path.toPath()){
            readByteArray()
        }
//        return readBuffer(path).readByteArray()
    }

    actual fun write(path: String, content: String) {
        FileSystem.SYSTEM.write(path.toPath()) {
            writeUtf8(content)
        }
//        buffer(path).writeUtf8(content).flush()
    }

    actual fun write(path: String, content: ByteArray) {
        FileSystem.SYSTEM.write(path.toPath()) {
            write(content)
        }
//        buffer(path).write(content).flush()
    }

    actual fun append(path: String, content: String) {
        appendBuffer(path).writeUtf8(content).flush()
    }

    actual fun append(path: String, content: ByteArray) {
        appendBuffer(path).write(content).flush()
    }

    actual fun exist(path: String): Boolean {
        return FileSystem.SYSTEM.exists(path.toPath())
    }

    actual fun mkdir(path: String): Boolean {
        runCatching {
            FileSystem.SYSTEM.createDirectories(path.toPath())
            return true
        }
        return false
    }

    actual fun copy(src: String, dst: String): Boolean {
        runCatching {
            FileSystem.SYSTEM.copy(src.toPath(), dst.toPath())
            return true
        }
        return false
    }

    actual fun copyDir(src: String, dst: String): Boolean {
        runCatching {
            val fs = FileSystem.SYSTEM
            val sourcePath = src.toPath()
            val targetPath = dst.toPath()

            fs.createDirectory(targetPath)

            fs.list(sourcePath).forEach { entry ->
                val sourceFile = sourcePath / entry
                val targetFile = targetPath / entry

                if (fs.metadata(sourceFile).isDirectory) {
                    copyDir(sourceFile.toString(), targetFile.toString())
                } else {
                    fs.copy(sourceFile, targetFile)
                }
            }
            return true
        }
        return false
    }
}