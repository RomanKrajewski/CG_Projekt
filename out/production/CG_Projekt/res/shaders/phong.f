varying vec4 phongColor;
varying vec2 uv;
uniform sampler2D texture;
uniform float useProcedure;
uniform float time;

void main(void){
    if(useProcedure == 0.){
    vec4 texColor =  texture2D(texture,uv);
   	gl_FragColor = texColor*phongColor;
   	}
   	else{
   	    if(mod(uv.y,0.05)>0.025){
   	            gl_FragColor = phongColor * vec4(1,0.5,0.5,1);
   	    }
   	    else{
   	            gl_FragColor = phongColor * vec4(1,0.7,0.7,1);
   	        }
   	    }

}