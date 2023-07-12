import kotlin.io.path.*

actual object FileHandle {
    actual fun readString(path: String): String {
        return Path(path).readText(Charsets.UTF_8)
    }

    actual fun readBinary(path: String): ByteArray {
        return Path(path).readBytes()
    }

    actual fun write(path: String, content: String) {
        Path(path).writeText(content, Charsets.UTF_8)
    }

    actual fun write(path: String, content: ByteArray) {
        Path(path).writeBytes(content)
    }

    actual fun append(path: String, content: String) {
        Path(path).appendText(content, Charsets.UTF_8)
    }

    actual fun append(path: String, content: ByteArray) {
        Path(path).appendBytes(content)
    }

    actual fun exist(path: String): Boolean {
        return Path(path).exists()
    }

    actual fun mkdir(path: String): Boolean {
        runCatching {
            Path(path).createDirectories()
            return true
        }
        return false
    }

    actual fun copy(src: String, dst: String): Boolean {
        runCatching {
            Path(src).copyTo(Path(dst))
            return true
        }
        return false
    }
     actual fun copyDir(src: String, dst: String): Boolean {
         runCatching {
             Path(src).copyTo(Path(dst))
             return true
         }
         return false
     }
}