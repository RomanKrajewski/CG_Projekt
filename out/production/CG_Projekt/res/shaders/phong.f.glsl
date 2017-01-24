varying vec4 phongColor;
uniform sampler2D texture;

void main(void){
    vec4 texColor = texture2D(texture,gl_TexCoord[0].xy);
   	gl_FragColor = texColor*phongColor;
}