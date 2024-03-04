package io.wollinger.animals

import kotlinx.browser.document
import kotlinx.browser.window

object Input {
    private val keyMap = ArrayList<String>()

    init {
        window.onkeydown = { if(!keyMap.contains(it.key)) keyMap.add(it.key) }
        window.onkeyup = { keyMap.remove(it.key) }
    }

    fun isPressed(key: String) = keyMap.contains(key)
    fun isJustPressed(key: String): Boolean {
        if(keyMap.contains(key)) {
            keyMap.remove(key)
            return true
        }
        return false
    }
}