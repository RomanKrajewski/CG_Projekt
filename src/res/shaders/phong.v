uniform vec3 light;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 normalMatrix;

varying vec4 phongColor;
varying vec2 uv;

uniform float ambient ;
uniform float specular ;
uniform float ideal ;




void main(void){
    vec4 color =gl_Color;

    uv = gl_MultiTexCoord0.xy;

    vec4 position = modelViewMatrix*gl_Vertex;

    vec3 N = normalize(vec3(normalMatrix*vec4(gl_Normal,0)));
    vec3 L = normalize(light - position.xyz);
    vec3 R = normalize(reflect(-L,N));
    vec3 V = normalize(-position.xyz);

    float Id = max(dot(L,N),0.0)*ideal;
    float Is = pow(max(0.0,dot(R,V)),5.0)*specular;

	phongColor = color*(Id+Is+ambient);
	gl_Position = projectionMatrix*modelViewMatrix*gl_Vertex;
}
