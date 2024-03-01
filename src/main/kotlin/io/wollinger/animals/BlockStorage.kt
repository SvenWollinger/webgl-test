package io.wollinger.animals

class BlockStorage(randomBlocks: Boolean = false) {
    private val size = 16
    private val array = Array<Block?>(size * size * size) { if(randomBlocks) Block.entries.random() else null }

    private fun getIndex(x: Int, y: Int, z: Int): Int {
        return x + size * (y + size * z)
    }

    fun forEach(action: (Int, Int, Int, Block?) -> Unit) {
        for(x in 0 until size) {
            for(y in 0 until size) {
                for (z in 0 until size) {
                    action.invoke(x, y, z, get(x, y, z))
                }
            }
        }
    }

    fun get(x: Int, y: Int, z: Int): Block? {
        if(0 > x) throw Exception("nah")
        if(x >= size) throw Exception("still nah")
        val index = getIndex(x, y, z)
        if (index in array.indices) {
            return array[index]
        } else {
            throw IndexOutOfBoundsException("Index out of bounds")
        }
    }

    fun set(x: Int, y: Int, z: Int, block: Block) {
        val index = getIndex(x, y, z)
        if (index in array.indices) {
            array[index] = block
        } else {
            throw IndexOutOfBoundsException("Index out of bounds")
        }
    }
}
