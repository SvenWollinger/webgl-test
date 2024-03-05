package io.wollinger.animals

import io.wollinger.animals.utils.download

object Resources {
    lateinit var lineVertex: String
        private set
    lateinit var lineFragment: String
        private set

    suspend fun load() {
        lineVertex = download("/shaders/line.vertex.glsl").await()
        lineFragment = download("/shaders/line.fragment.glsl").await()
    }
}