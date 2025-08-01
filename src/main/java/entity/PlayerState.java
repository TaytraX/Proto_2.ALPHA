package entity;

import engine.AABB;
import org.joml.Vector2f;

public record PlayerState(
        Vector2f position,
        Vector2f velocity,
        Vector2f jumpVelocity,
        boolean isGrounded,
        boolean wasGrounded,
        float previousVelocity,  // pour capturer la vélocité d'impact
        AnimationState animationState,
        boolean facingRight,
        boolean moveLeft,
        boolean moveRight,
        boolean jump,
        float force,
        long timestamp
) {
    public static final Vector2f PLAYER_SIZE = new Vector2f(0.4f, 0.6f);

    public AABB getAABB() {
        return new AABB(position, PLAYER_SIZE);
    }
}