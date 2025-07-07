package Engine;

import Entity.PlayerState;
import org.joml.Vector2f;

import java.util.List;

public class Physics {
    public static final float GRAVITY = -9.81f;
    public static final float GROUND_FRICTION = 0.5f;

    public PlayerState update(PlayerState currentState, float deltaTime, List<AABB> platforms) {
        Vector2f newVelocity = new Vector2f(currentState.velocity());
        Vector2f newPosition = new Vector2f(currentState.position());
        boolean isGrounded = false;

        applyPlayerActions(currentState, newVelocity, deltaTime);
        applyPhysics(currentState, newVelocity, deltaTime);

        newPosition.x += newVelocity.x * deltaTime;
        newPosition.y += newVelocity.y * deltaTime;

        AABB playerAABB = new AABB (newPosition, PlayerState.PLAYER_SIZE);

        for (AABB platform : platforms) {
            if (playerAABB.collidesWith(platform)) {

                float overlapX = Math.min(playerAABB.getMaxX() - platform.getMinX(), platform.getMaxX() - playerAABB.getMinX());
                float overlapY = Math.min(playerAABB.getMaxY() - platform.getMinY(), platform.getMaxY() - playerAABB.getMinY());

                if (overlapX < overlapY) {
                    if (newVelocity.x > 0){
                        newPosition.x = platform.getMinX() - PlayerState.PLAYER_SIZE.x;
                    }
                    else newPosition.x = platform.getMaxX() + PlayerState.PLAYER_SIZE.x;

                    newVelocity.x = 0;
                }else {
                    if (newVelocity.y > 0) {

                        newPosition.y = platform.getMinY() - PlayerState.PLAYER_SIZE.y;
                    } else {
                        newPosition.y = platform.getMaxY() + PlayerState.PLAYER_SIZE.y;
                        isGrounded = true;
                    }
                    newVelocity.y = 0;
                }
                playerAABB = new AABB(newPosition, PlayerState.PLAYER_SIZE);
            }
        }

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

    private void applyPlayerActions(PlayerState state, Vector2f velocity, float deltaTime) {
        // Mouvement horizontal basé sur les actions
        if (state.moveLeft()) {
            velocity.x = -state.moveSpeed();
        } else if (state.moveRight()) {
            velocity.x = state.moveSpeed();
        } else if (state.isGrounded()) {
            // Arrêt progressif au sol
            velocity.x *= (1.0f - GROUND_FRICTION * deltaTime);
        }

        // Saut basé sur l'action
        if (state.jump()) {
            velocity.y = state.jumpForce();
        }
    }

    private void applyPhysics(PlayerState state, Vector2f velocity, float deltaTime) {
        // Gravité
        if(!state.isGrounded()) {
            velocity.y += GRAVITY * deltaTime;
        }
    }
}