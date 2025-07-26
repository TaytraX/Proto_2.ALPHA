package Engine.World;

import Engine.AABB;
import org.joml.Vector2f;

import java.util.Random;
import static Laucher.Main.width;
import static Laucher.Main.height;

public class GeneratedGround {

    private final Perlin noise;
    Random seed = new Random();
    float nx;
    float ny;

    public GeneratedGround() {
        noise = new Perlin(seed.nextLong());
        setupMesh();
    }

    private void setupMesh() {
        int cols = 200;
        int rows = 150;

        for (int x =0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {

                nx = x + 0.8f;
                ny = y + 0.8f;

                float grand = noise.fbm(nx, ny, 4, 2, 0.5f) * 40f;

                float textureGround = noise.fbm(nx, ny, 4, 2, 0.5f) * 40f;
            }
        }
        new AABB(new Vector2f(nx, ny), new Vector2f(nx, ny));
    }

    private float applyLandBias(float noise) {
        // Normaliser vers [0,1]
        float normalized = noise * 0.5f + 0.5f;

        // Plus l'exposant est petit, plus il y aura d'eau
        float curved = (float)Math.pow(normalized, 1.17f);

        // Retourner vers [-1,1]
        return curved * 2.0f - 1.0f;
    }

}