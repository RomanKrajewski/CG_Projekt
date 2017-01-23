uniform vec2 lightPosition;
varying vec3 light;
varying vec3 N;
varying vec3 L;
varying vec3 R;
varying vec3 V;
varying vec4 position;
vec3 normal = -gl_Normal;
varying vec4 color;

void main(void){
    color =gl_Color;
    position = gl_ModelViewMatrix*gl_Vertex;
    N = normalize(gl_NormalMatrix*(normal));
    light = vec3(lightPosition.x,lightPosition.y,3);
    L = normalize(position.xyz - light);
    R = normalize(-reflect(N,L));
    V = normalize(-position.xyz);
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
}
