package io.wollinger.animals.graphics

import io.wollinger.animals.math.Rectangle
import io.wollinger.animals.math.Vector2
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Image

class UIButton(
    private val image: Image,
    private val onResize: (Int, Int) -> Rectangle,
    private val onClick: () -> Unit,
    private val drawCondition: (() -> Boolean)? = null,
) {
    private var rectangle = Rectangle(0.0, 0.0, 0.0, 0.0)

    fun resize(width: Int, height: Int) {
        rectangle.set(onResize.invoke(width, height))
    }

    fun shouldDraw(): Boolean {
        if(drawCondition == null) return true
        return drawCondition.invoke()
    }

    fun isOver(point: Vector2): Boolean {
        if(!shouldDraw()) return false
        return rectangle.contains(point)
    }

    fun click() {
        onClick.invoke()
    }

    fun draw(hud: CanvasRenderingContext2D) {
        if(drawCondition != null && !drawCondition.invoke()) return
        hud.drawImage(image, rectangle.x, rectangle.y, rectangle.width, rectangle.height)
    }
}