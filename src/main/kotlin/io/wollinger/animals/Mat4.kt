@JsModule("gl-matrix")
@JsNonModule
external val glMatrix: dynamic

val mat4: dynamic = glMatrix.mat4
val quat: dynamic = glMatrix.quat