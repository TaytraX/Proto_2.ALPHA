#version 400 core

in vec2 fragTextureCoord;
out vec4 fragColour;

uniform sampler2D textureSample;

void main() {
    vec4 textureColor = texture(textureSample, fragTextureCoord);

    // ✅ CORRECTION: Élimination plus précise des couleurs de fond
    // Éliminer le blanc ET le vert clair qui peuvent être des artefacts
    float whiteThreshold = 0.9;
    float greenThreshold = 0.8;

    // Détection du blanc
    bool isWhite = (textureColor.r > whiteThreshold &&
    textureColor.g > whiteThreshold &&
    textureColor.b > whiteThreshold);

    // Détection des couleurs très sombres (parfois des artefacts)
    bool isVeryDark = (textureColor.r < 0.1 &&
    textureColor.g < 0.1 &&
    textureColor.b < 0.1);

    fragColour = textureColor;
}