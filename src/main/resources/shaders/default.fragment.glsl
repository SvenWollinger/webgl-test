precision mediump float;
varying highp vec2 vTextureCoord;
varying vec3 vColor;
uniform sampler2D uSampler;

void main() {
    //gl_FragColor = texture2D(uSampler, vTextureCoord) * vec4(vColor, 1.0);
    vec4 pixel = texture2D(uSampler, vTextureCoord) * vec4(vColor, 1.0);
    if (pixel.a <= 0.0) discard;
    gl_FragColor = pixel;
}