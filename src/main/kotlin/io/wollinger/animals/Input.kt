package io.wollinger.animals

import kotlinx.browser.window

object Input {
    private val keyMap = HashMap<String, Boolean>()

    init {
        window.onkeydown = {
            if(!keyMap.contains(it.key)) {
                keyMap[it.key] = false
            }
        }
        window.onkeyup = { keyMap.remove(it.key) }
    }

    fun isPressed(key: String) = keyMap.contains(key)
    fun isJustPressed(key: String): Boolean {
        val checked = keyMap[key] ?: return false
        if(!checked) {
            keyMap[key] = true
            return true
        }
        return false
    }
}