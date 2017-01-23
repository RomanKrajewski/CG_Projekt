varying vec4 color;
varying vec3 light;
varying vec3 N;
varying vec3 L;
varying vec3 R;
varying vec3 V;
varying vec4 position;
float ambient = 0.1;
float specular = 0.5;
float ideal = 0.4;

void main(void){

   	float Id = max(dot(L,N),0.0)*ideal;
   	float Is = pow(dot(R,V),18.0)*specular;
   	gl_FragColor = color*(Id+Is+ambient);
}