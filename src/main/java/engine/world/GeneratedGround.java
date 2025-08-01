package engine.world;

import engine.AABB;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.List;

public class GeneratedGround {
    private final List<AABB> generatedGround = new ArrayList<>();
    private final Perlin noise;

    public static final int CHUNK_WIDTH = 16;

    public GeneratedGround(long seed, int chunkX) {
        noise = new Perlin(seed);
        generateChunk(chunkX);
    }

    private void generateChunk(int chunkX) {
        for (int localX = 0; localX < CHUNK_WIDTH; localX++) {
            int worldX = chunkX * CHUNK_WIDTH + localX;
            float heightNoise = noise.fbm(worldX * 0.02f, 1, 1.0f, 0.1f);
            int surfaceHeight = (int)(heightNoise * 2 + 25);

            // Calculs pour UNE colonne
            float columnHeight = surfaceHeight - (-80) + 1; // hauteur totale
            float centerY = (-80 + surfaceHeight) / 2.0f;   // centre Y

            // Générer de bas en haut
            generatedGround.add(new AABB(
                  new Vector2f(worldX, centerY),
                  new Vector2f(1f, columnHeight / 2)
            ));
        }
    }


    public List<AABB> getPlatforms() {
        return generatedGround;
    }
}