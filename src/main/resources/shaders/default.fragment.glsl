precision mediump float;
varying highp vec2 vTextureCoord;
varying vec3 vColor;
uniform sampler2D uSampler;

void main() {
    gl_FragColor = texture2D(uSampler, vTextureCoord) * vec4(vColor, 1.0);
}