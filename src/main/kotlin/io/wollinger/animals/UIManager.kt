package io.wollinger.animals

import io.wollinger.animals.graphics.UIButton
import io.wollinger.animals.math.Vector2
import io.wollinger.animals.utils.addAll
import org.w3c.dom.CanvasRenderingContext2D

object UIManager {
    private val buttons = ArrayList<UIButton>()

    fun resize(width: Int, height: Int) {
        buttons.forEach {
            it.resize(width, height)
        }
    }

    fun isOverUI(point: Vector2): Boolean {
        return buttons.any { it.shouldDraw() && it.isOver(point)}
    }

    fun add(vararg button: UIButton) {
        buttons.addAll(button)
    }

    fun clickEvent(point: Vector2) {
        buttons.filter { it.isOver(point) }.forEach { it.click() }
    }

    fun draw(hud: CanvasRenderingContext2D) {
        buttons.forEach { it.draw(hud) }
    }
}