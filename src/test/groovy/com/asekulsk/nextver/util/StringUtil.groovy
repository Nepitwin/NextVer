package com.asekulsk.nextver.util

class StringUtil {
    static String GenerateRandomString(int length) {
        def letters = ('A'..'Z') + ('a'..'z')
        def name = (1..length).collect { letters[new Random().nextInt(letters.size())] }.join()
        name.capitalize()
    }
}
