package Engine.World;

public record GroundGenRequest(int chunkX, long seed) {

    public GroundGenRequest forChunk(int chunkX) {
        return new GroundGenRequest(chunkX, seed);
    }
}