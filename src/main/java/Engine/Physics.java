package Engine;

import Entity.PlayerState;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class Physics {
    public static final float GRAVITY = -12.00f;
    public static final float GROUND_FRICTION = 5.0f;
    float deltaTime = 0.016f;

    public PlayerState update(PlayerState currentState,  List<AABB> platforms) {

        applyPlayerActions(currentState, currentState.velocity(), deltaTime);

        PlayerState newState = resolveHorizontalCollisions(currentState, deltaTime, platforms, currentState.isGrounded());
        return applyPhysics(newState, newState.velocity(), deltaTime, platforms, newState.isGrounded());
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

    private PlayerState resolveHorizontalCollisions(PlayerState currentState, float deltaTime, List<AABB> platforms, boolean isGrounded){

        Vector2f newVelocity = new Vector2f(currentState.velocity());
        Vector2f newPosition = new Vector2f(currentState.position());

        newPosition.x += newVelocity.x * deltaTime;

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

    private PlayerState applyPhysics(PlayerState currentState, Vector2f velocity, float deltaTime, List<AABB> platforms, boolean isGrounded) {

        Vector2f newPosition = new Vector2f(currentState.position());
        // Gravité
        if(!isGrounded) {
            velocity.y += GRAVITY * deltaTime;
        }

        newPosition.y += velocity.y * deltaTime;

        AABB playerAABB = new AABB (newPosition, PlayerState.PLAYER_SIZE);

        for (AABB platform : platforms) {
            if (playerAABB.collidesWith(platform)) {

                    if (velocity.y > 0) {

                        newPosition.y = platform.getMinY() - PlayerState.PLAYER_SIZE.y;
                    } else {
                        newPosition.y = platform.getMaxY() + PlayerState.PLAYER_SIZE.y;
                        isGrounded = true;
                    }
                    velocity.y = 0;
                }
                playerAABB = new AABB(newPosition, PlayerState.PLAYER_SIZE);
            }

        return new PlayerState(
                newPosition,
                velocity,
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
}