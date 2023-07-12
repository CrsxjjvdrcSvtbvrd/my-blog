package utils

import FileHandle

fun FileHandle.createDir(dir: String): Boolean {
    if(!FileHandle.exist(dir)){
        return FileHandle.mkdir(dir)
    }
    return true
}