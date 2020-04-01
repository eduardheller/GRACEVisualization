#version 330 core

uniform mat4 MVP;
uniform mat4 M;

layout(location=0) in vec3 vertex;
layout(location=1) in vec2 uv;
layout(location=2) in vec3 normal;

out vec2 uv0;
out vec3 normal0;
out vec3 color0;
out vec3 WorldPos0;

void main() {
    gl_Position = MVP * vec4(vertex, 1.0);
    WorldPos0 = (M * vec4(vertex, 1.0)).xyz;
    color0 = vec3(1.0,0.0,0.0);
    uv0 = uv;
    normal0 = (M*vec4(normal, 1.0)).xyz;
}