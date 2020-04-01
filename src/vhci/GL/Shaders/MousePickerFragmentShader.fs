#version 330 core

in vec3 WorldPos0;
in vec2 uv0;
in vec3 normal0;
in vec3 color0;

out vec4 color;

float rand(vec2 n)
{
  return 0.5 + 0.5 * 
     fract(sin(dot(n.xy, vec2(12.9898, 78.233)))* 43758.5453);
}

void main() {
    color = vec4(color0, 1.0);
}