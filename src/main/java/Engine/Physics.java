package Engine;

import Engine.AABB;
import Entity.PlayerState;
import org.joml.Vector2f;

import java.util.List;

public class Physics {
    public static final float GRAVITY = -12.00f;
    public static final float GROUND_FRICTION = 5.021f;

    public PlayerState update(PlayerState currentState, float deltaTime, List<AABB> platforms) {
        Vector2f newPosition = new Vector2f(currentState.position());
        Vector2f newVelocity = new Vector2f (currentState.velocity());
        boolean isGrounded = false;

        resolveHorizontalCollisions(currentState, newVelocity, deltaTime);
        resolveVerticalCollisions(currentState, newVelocity, deltaTime);

        return new PlayerState(
                newPosition,
                newVelocity,
                isGrounded,
                currentState.animationState(),
                currentState.facingRight(),
                currentState.moveLeft(),
                currentState.moveRight(),
                currentState.jump(),
                currentState.moveSpeed(),
                currentState.jumpForce(),
                currentState.timestamp()
        );
    }

    private void resolveHorizontalCollisions(PlayerState state, Vector2f velocity, float deltaTime) {
        // Mouvement horizontal basé sur les actions
    }

    private void resolveVerticalCollisions(PlayerState state, Vector2f velocity, float deltaTime) {
        // Gravité
        if(!state.isGrounded()) {
            velocity.y += GRAVITY * deltaTime;
        }
    }
}