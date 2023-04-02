package encryptdecrypt

import java.io.File

fun main(args: Array<String>) {

    var mode = "enc"
    var key = 0
    var data = ""
    var file_in = ""
    var file_out = ""
    var algorithm = "shift"

    for (i in 0 until args.size step 2) {
        if(args[i].equals("-mode"))
            mode = args[i+1]
        if(args[i].equals("-key"))
            key = args[i+1].toInt()
        if(args[i].equals("-data"))
            data = args[i+1]
        if(args[i].equals("-in"))
            file_in = args[i+1]
        if(args[i].equals("-out"))
            file_out = args[i+1]
        if(args[i].equals("-alg"))
            algorithm = args[i+1]
    }

    if (file_in.isNotBlank()) data = File(file_in).readText()

    //println("mode$mode key$key data$data in$file_in out$file_out alg$algorithm")

       val outputData = if(algorithm.equals("unicode")) {
            if (mode.equals("enc")) encryptData(data, key, true)
            else if (mode.equals("dec")) encryptData(data, key, false) else "Error"
        } else if (algorithm.equals("shift")) {
            if (mode.equals("enc")) encryptData(data, key)
            else if (mode.equals("dec")) encryptData(data, -key) else "Error"
        } else "ERROR"

    //println("input:$data output:$outputData fileIn$file_in fileOut:$file_out")

    if (file_out.isBlank()) println(outputData) else File(file_out).writeText(outputData)
}

fun encryptData(data: String, key: Int, isEncode: Boolean = false) : String {
    var encryptedData = ""

    when (isEncode) {
        true -> {
            for(ch in data) {
                encryptedData += encode(ch, key)
            }
            return encryptedData
        }
        false -> {
            for(ch in data) {
                encryptedData += decode(ch, key)
            }
            return encryptedData
        }
    }
    return encryptedData
}

fun encryptData(data: String, key: Int) : String {
    var encryptedData = ""
    val regex: Regex = "[a-zA-Z]".toRegex()

    for(ch in data) {
        encryptedData += if (regex.matches(ch.toString())) reverseChOrderWithKey(ch, key) else ch
    }
    return encryptedData
}


fun reverseChOrderWithKey(ch: Char, key: Int): Char {
    val keyval = ch.code + key
    if (ch.code in 97..122 ) {
        return if(keyval >= 123)  (96 + (keyval - 122)).toChar() else if(keyval < 97 ) (122 - (96 - keyval)).toChar() else keyval.toChar()
    }
    else if (ch.code in 65..90 ) {
        return if(keyval >= 91)  (64 + (keyval - 90)).toChar() else if(keyval < 65 ) (90 - (64 - keyval)).toChar() else keyval.toChar()
    }

    return keyval.toChar()
}

fun encode(ch: Char, key: Int): Char {
    return (ch.code + key).toChar()
}

fun decode(ch: Char, key: Int): Char {
    return (ch.code - key).toChar()
}
