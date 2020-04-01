#version 330 core

uniform mat4 MVP;
uniform mat4 M;

uniform float minRange;
uniform float maxRange;

uniform float minColor;
uniform float maxColor;

uniform float Timer;

layout(location=0) in vec3 vertex;
layout(location=1) in vec2 uv;
layout(location=2) in vec3 normal;
layout(location=3) in float weight;

layout(location=4) in vec3 vertexTarget;
layout(location=5) in vec3 normalTarget;
layout(location=6) in float weightTarget;


out vec2 uv0;
out vec3 normal0;
out vec3 color0;
out vec3 WorldPos0;

vec3 rgb2hsv(vec3 c)
{
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

vec3 colorMapping(float minVal, float maxVal, float actual)
{
    vec3 color;
    float hueMin = minColor;
    float hueMax = maxColor;

    float clampedActual = max( actual, minVal );
    clampedActual = min( clampedActual, maxVal );

    float actualDelta = (clampedActual - minVal) / (maxVal - minVal);

    color = hsv2rgb(vec3(( (hueMax * actualDelta + hueMin) /360.0), 1, 1));

    return color;
    //vec3 color;
    //float ratio = 2 * (actual-minVal) / (maxVal - minVal);
    //color.b = int(min(255,max(0, 255*(1 - ratio))));
    //color.r = int(min(255,max(0, 255*(ratio - 1))));
    //color.g = max(0,255 - color.b - color.r);
    //return (color / 255.0);
}

void main() {
    vec3 color = vec3(1.0,1.0,1.0);


    float distRange = abs(minRange)+abs(maxRange);
    float gravitationValue = weight;
    gravitationValue *=-1;

    vec3 colorA;
    if(weight<32000)
    {
        colorA = colorMapping(minRange,maxRange,gravitationValue);
    }
    else
    {
        colorA = vec3(0,1,0);
    }

    float gravitationValueTarget = weightTarget;
    gravitationValueTarget *=-1;

    vec3 colorB;
    if(weightTarget<32000)
    {
        colorB = colorMapping(minRange,maxRange,gravitationValueTarget);
    }
    else
    {
        colorB = vec3(0,1,0);
    }    

    color = mix(colorA,colorB,Timer);

    gl_Position = MVP * vec4(mix(vertex,vertexTarget,Timer), 1.0);
    WorldPos0 = (M * vec4(mix(vertex,vertexTarget,Timer) , 1.0)).xyz;
  
    color0 = color;
    uv0 = uv;
    normal0 = (M*vec4(mix(normal,normalTarget,Timer), 1.0)).xyz;
}