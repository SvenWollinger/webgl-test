package io.wollinger.animals

import io.wollinger.animals.math.Vector2
import io.wollinger.animals.math.Vector3
import io.wollinger.animals.utils.id
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement

object Input {
    private val keyMap = HashMap<String, Boolean>()
    private var dragging = false

    private val startDrag = Vector2()
    var dragDelta = Vector2()
    var scroll = 0.0

    init {
        window.onwheel = {
            scroll = it.deltaY
            null
        }
        window.onpointermove = {
            if(dragging) {
                dragDelta.add(it.clientX - startDrag.x, it.clientY - startDrag.y)
                startDrag.set(x = it.clientX, y = it.clientY)
            }
        }
        window.onpointerdown = {
            dragging = true
            startDrag.set(it.clientX, it.clientY)
            null
        }
        window.onpointerup = {
            dragging = false
            dragDelta.set(0, 0)
            null
        }
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