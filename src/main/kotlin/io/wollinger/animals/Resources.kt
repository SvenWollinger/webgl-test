package io.wollinger.animals

import io.wollinger.animals.utils.download
import org.w3c.dom.Image

object Resources {
    lateinit var lineVertex: String
        private set
    lateinit var lineFragment: String
        private set

    val button2d = Image().apply { src = "/img/2d.png" }
    val button3d = Image().apply { src = "/img/3d.png" }
    val buttonDebug = Image().apply { src = "/img/debug.png" }


    suspend fun load() {
        lineVertex = download("/shaders/line.vertex.glsl").await()
        lineFragment = download("/shaders/line.fragment.glsl").await()
    }
}