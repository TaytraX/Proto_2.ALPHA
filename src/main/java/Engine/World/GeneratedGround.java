package Engine.World;

import Engine.AABB;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.List;

public class GeneratedGround {
    private final List<AABB> generatedPlatforms = new ArrayList<>();
    private final Perlin noise;

    public static final int CHUNK_WIDTH = 16;
    private static final int CHUNK_HEIGHT = 64;

    public GeneratedGround(long seed, int chunkX) {
        noise = new Perlin(seed);
        generateChunk(chunkX);
    }

    private void generateChunk(int chunkX) {
        for (int localX = 0; localX < CHUNK_WIDTH; localX++) {
            int worldX = chunkX * CHUNK_WIDTH + localX;
            float heightNoise = noise.fbm(worldX * 0.02f, 1, 1.0f, 0.1f);
            int surfaceHeight = (int)(heightNoise * 2 + 25);

            // Générer de bas en haut
            for (int y = -80; y <= surfaceHeight; y++) {
                    generatedPlatforms.add(new AABB(
                            new Vector2f(worldX, y),
                            new Vector2f(0.5f, 0.5f)
                    ));
            }
        }
    }


    public List<AABB> getPlatforms() {
        return generatedPlatforms;
    }

    public static int getChunkWidth() {
        return CHUNK_WIDTH;
    }

    public static int getChunkHeight() {
        return CHUNK_HEIGHT;
    }
}