package Engine.World;

import java.util.Random;

public class Perlin {
    private int[] permutation;

    public Perlin(long seed) {
        permutation = new int[512];
        int[] p = new int[256];
        Random rand = new Random(seed);
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }
        for (int i = 255; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }
        for (int i = 0; i < 512; i++) {
            permutation[i] = p[i & 255];
        }
    }

    private float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    private float grad(int hash, float x) {
        int h = hash & 15;
        float grad = 1.0f + (h & 7);
        return (h & 8) == 0 ? grad * x : -grad * x;
    }

    public float noise(float x) {
        int X = (int) Math.floor(x) & 255;
        x -= Math.floor(x);
        float u = fade(x);
        return lerp(u, grad(permutation[X], x), grad(permutation[X + 1], x - 1));
    }

    public float fbm(float x, int octaves, float lacunarity, float gain) {
        float total = 0;
        float amplitude = 1;
        float frequency = 1;
        float maxAmplitude = 0;

        for (int i = 0; i < octaves; i++) {
            total += noise(x * frequency) * amplitude;
            maxAmplitude += amplitude;
            amplitude *= gain;
            frequency *= lacunarity;
        }

        return total / maxAmplitude;
    }
}