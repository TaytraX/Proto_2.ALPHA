package Engine.World;

/**
 * Requête de génération de terrain pour un chunk spécifique.
 * Contient toutes les informations nécessaires pour générer un chunk de manière asynchrone.
 *
 * @param chunkX Position X du chunk à générer (coordonnée chunk, pas monde)
 * @param seed Graine pour la génération procédurale (garantit la reproductibilité)
 */
public record GroundGenRequest(int chunkX, long seed) {

    /**
     * Validation des paramètres lors de la création
     */
    public GroundGenRequest {
        // Validation optionnelle si nécessaire
        if (seed == 0) {
            throw new IllegalArgumentException("Seed ne peut pas être 0");
        }
    }

    /**
     * Méthode utilitaire pour créer une requête pour un chunk adjacent
     * @param offsetX décalage par rapport au chunk actuel
     * @return nouvelle requête pour le chunk décalé
     */
    public GroundGenRequest offset(int offsetX) {
        return new GroundGenRequest(chunkX + offsetX, seed);
    }

    /**
     * Conversion vers coordonnée monde
     * @return position X de début du chunk dans les coordonnées monde
     */
    public float getWorldStartX() {
        return chunkX * GeneratedGround.CHUNK_WIDTH;
    }

    /**
     * Conversion vers coordonnée monde
     * @return position X de fin du chunk dans les coordonnées monde
     */
    public float getWorldEndX() {
        return (chunkX + 1) * GeneratedGround.CHUNK_WIDTH;
    }

    @Override
    public String toString() {
        return String.format("GroundGenRequest{chunk=%d, seed=%d, worldRange=[%.1f-%.1f]}",
                chunkX, seed, getWorldStartX(), getWorldEndX());
    }
}