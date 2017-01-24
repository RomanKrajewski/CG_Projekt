uniform vec2 lightPosition;
varying vec3 light;
varying vec3 N;
varying vec3 L;
varying vec3 R;
varying vec3 V;
varying vec4 position;
vec3 normal = gl_Normal;
varying vec4 phongColor;
float ambient = 0.1;
float specular = 0.5;
float ideal = 0.4;


void main(void){
    vec4 color =gl_Color;
    light = vec3(lightPosition.x,lightPosition.y,3);
    gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;

    position = gl_ModelViewMatrix*gl_Vertex;

    N = normalize(gl_NormalMatrix*(normal));
    L = normalize(light - position.xyz);
    R = normalize(reflect(N,-L));
    V = normalize(-position.xyz);

    float Id = max(dot(L,N),0.0)*ideal;
    float Is = pow(dot(R,V),18.0)*specular;

	phongColor = color*(Id+Is+ambient);
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
}
