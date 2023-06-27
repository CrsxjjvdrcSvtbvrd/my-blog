import okio.FileSystem
import okio.Path.Companion.toPath

fun main(args: Array<String>) {
    Launcher(Mingw64Core(), AppV1()).start(*args)
}