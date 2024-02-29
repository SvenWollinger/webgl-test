attribute vec3 aPosition;
attribute vec2 aTexCoord;
attribute vec3 aColor;
varying highp vec2 vTextureCoord;
varying vec3 vColor;
uniform mat4 uModelViewMatrix;
uniform mat4 uProjectionMatrix;

void main() {
    gl_Position = uProjectionMatrix * uModelViewMatrix * vec4(aPosition, 1.0);
    vTextureCoord = aTexCoord;
    vColor = aColor;
}