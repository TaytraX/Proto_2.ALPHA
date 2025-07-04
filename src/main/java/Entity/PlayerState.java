package Entity;

import org.joml.Vector2f;

public record PlayerState(
        Vector2f position,
        Vector2f velocity,
        boolean isGrounded,
        AnimationState animationState,
        boolean facingRight,
        long timestamp
) {}