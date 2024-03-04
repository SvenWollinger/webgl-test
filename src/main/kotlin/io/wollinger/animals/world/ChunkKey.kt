import io.wollinger.animals.math.Vector3
import io.wollinger.animals.world.Chunk

class ChunkKey(val x: Int, val y: Int, val z: Int) {
    private val hashCode: Int = x * 31 + y * 14 + z

    constructor(x: Float, y: Float, z: Float): this(x.toInt(), y.toInt(), z.toInt())

    override fun toString() = "ChunkKey($x/$y/$z)"
    override fun hashCode() = hashCode
    override fun equals(other: Any?) = other != null && other is ChunkKey && other.x == x && other.y == y && other.z == z

    val worldPos: Vector3 = Vector3(x * Chunk.SIZE_X.toFloat(), y * Chunk.SIZE_Y.toFloat(), z * Chunk.SIZE_Z.toFloat())

    companion object {
        fun fromChunk(chunk: Chunk) = ChunkKey(chunk.x, chunk.y, chunk.z)
        //fun fromWorldPosition(x: Float, y: Float, z: Float): ChunkKey = ChunkKey(World.chunkX(x), World.chunkY(y), World.chunkZ(z))
    }
}