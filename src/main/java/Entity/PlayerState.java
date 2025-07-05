package Entity;

import Engine.AABB;
import org.joml.Vector2f;

public record PlayerState(
        Vector2f position,
        Vector2f velocity,
        boolean isGrounded,
        AnimationState animationState,
        boolean facingRight,
        boolean moveLeft,
        boolean moveRight,
        boolean jump,
        float moveSpeed,         // ✅ AJOUTÉ (optionnel)
        float jumpForce,
        long timestamp
) {
    public static final Vector2f PLAYER_SIZE = new Vector2f(0.4f, 0.6f);

    public AABB getAABB() {
        return new AABB(position, PLAYER_SIZE);
    }
}