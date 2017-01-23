varying vec4 color;
uniform vec2 lightPosition;
varying vec3 light = vec3(lightPosition.x,lightPosition.y,3);
vec3 normal = -gl_Normal;
float ambient = 0.1;
float specular = 0.5;
float ideal = 0.4;
void main(void){
	vec4 position = gl_ModelViewMatrix*gl_Vertex;
	vec3 L = normalize(position.xyz - light);
	vec3 N = normalize(gl_NormalMatrix*(normal));
	vec3 R = normalize(-reflect(N,L));
	vec3 V = normalize(-position.xyz);
	float Id = max(dot(L,N),0.0)*ideal;
	float Is = pow(dot(R,V),18.0)*specular;
	color = gl_Color*(Id+Is+ambient);


	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
}
