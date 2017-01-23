varying vec4 color;
varying vec3 light = vec3(0.2,0.2,0);
void main(void){
	vec4 position = gl_ModelViewMatrix*gl_Vertex;
	vec3 L = normalize(position.xyz - light);
	vec3 N = normalize(gl_NormalMatrix*(-gl_Normal));
	vec3 R = normalize(-reflect(N,L));
	vec3 V = normalize(-position.xyz);
	float Id = max(dot(L,N),0.0);
	float Is = pow(dot(R,V),10.0);
	color = gl_Color*(Is+Id+0.5);
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	}
