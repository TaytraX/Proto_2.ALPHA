package Engine.World;

import Engine.AABB;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.List;

public class GeneratedGround {
    private final List<AABB> generatedPlatforms = new ArrayList<>();
    private final Perlin noise;

    // Dimensions fixes du chunk
    public static final int CHUNK_WIDTH = 16;  // X = largeur (horizontal)
    private static final int CHUNK_HEIGHT = 64; // Y = hauteur (vertical)

    public GeneratedGround(long seed, int chunkX) {
        noise = new Perlin(seed);
        generateChunk(chunkX);
    }

    private void generateChunk(int chunkX) {
        // Pour chaque colonne X dans le chunk
        for (int localX = 0; localX < CHUNK_WIDTH; localX++) {
            int worldX = chunkX * CHUNK_WIDTH + localX;

            // Hauteur du terrain pour cette colonne
            float heightNoise = noise.fbm(worldX * 0.02f, 0, 3, 2.0f, 0.5f);
            int surfaceHeight = (int)(heightNoise * 15 + 25); // Hauteur entre 10 et 40

            // Limiter la hauteur au chunk
            surfaceHeight = Math.min(surfaceHeight, CHUNK_HEIGHT - 1);

            // Générer la colonne Y de bas en haut
            for (int y = 0; y <= surfaceHeight && y < CHUNK_HEIGHT; y++) {
                boolean shouldPlaceBlock = false;

                // Surface : 3 blocs de terre/herbe
                if (y >= surfaceHeight - 2) {
                    shouldPlaceBlock = true;
                }
                // Sous-sol : roche avec cavernes
                else if (y < surfaceHeight - 2) {
                    float caveNoise = noise.fbm(worldX * 0.05f, y * 0.05f, 2, 2.0f, 0.6f);
                    shouldPlaceBlock = caveNoise < 0.3f; // Caverne si > 0.3f
                }

                if (shouldPlaceBlock) {
                    generatedPlatforms.add(new AABB(
                            new Vector2f(worldX, y),
                            new Vector2f(0.5f, 0.5f)
                    ));
                }
            }
        }
    }

    public List<AABB> getPlatforms() {
        return generatedPlatforms;
    }

    // Getters utiles
    public static int getChunkWidth() { return CHUNK_WIDTH; }
    public static int getChunkHeight() { return CHUNK_HEIGHT; }
}