package Engine.World;

public record GroundGenRequest(int chunkX, long seed) {

    public static GroundGenRequest forChunk(int chunkX) {
        return new GroundGenRequest(chunkX, System.currentTimeMillis());
    }
}