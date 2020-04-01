#version 330 core

uniform vec3 LightDirection;
uniform vec3 LightColor;
uniform float LightAmbientIntensity;
uniform float LightDiffuseIntensity;
uniform float ColorFade;

uniform vec3 CameraWorldLocation;

in vec3 WorldPos0;
in vec2 uv0;
in vec3 normal0;
in vec3 color0;

out vec4 color;

uniform sampler2D AlbedoMap;

float rand(vec2 n)
{
  return 0.5 + 0.5 * 
     fract(sin(dot(n.xy, vec2(12.9898, 78.233)))* 43758.5453);
}

void main() {
    vec3 normal = normalize(normal0);
    vec3 surfacePos = WorldPos0;
    vec4 surfaceColor = vec4(color0, 1.0f);

    vec4 textureColor = texture(AlbedoMap, uv0 );

    vec3 surfaceToLight = normalize(-LightDirection);
    vec3 surfaceToCamera = normalize(CameraWorldLocation - surfacePos);
    
    vec3 materialSpecularColor = LightColor;
    float materialShininess = 1024;

    //ambient
    vec3 ambient = LightAmbientIntensity * surfaceColor.rgb * LightColor;

    //diffuse
    float diffuseCoefficient = max(0.0, dot(normal, surfaceToLight));
    vec3 diffuse = diffuseCoefficient * surfaceColor.rgb * LightDiffuseIntensity;
    
    //specular
    float specularCoefficient = 0.0;
    if(diffuseCoefficient > 0.0)
    {
        specularCoefficient = pow(max(0.0, dot(surfaceToCamera, reflect(surfaceToLight, normal))), materialShininess);
    }
    vec3 specular = specularCoefficient * materialSpecularColor * LightColor;
    
    vec3 linearColor = ambient + (diffuse + specular);

    if( textureColor.a != 0 ){
        color = mix(textureColor,vec4(linearColor, surfaceColor.a), ColorFade);
    }
    else
    {      
        color =  vec4(linearColor, surfaceColor.a);
    }
}