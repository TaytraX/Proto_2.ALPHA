package Engine;

import Entity.PlayerState;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class Physics {
    public static final float GRAVITY = -8.00f;
    public static final float GROUND_FRICTION = 5.0f;
    float deltaTime = 0.016f;

    public PlayerState update(PlayerState currentState,  List<AABB> platforms) {
        Vector2f newVelocity = new Vector2f(currentState.velocity());
        Vector2f newPosition = new Vector2f(currentState.position());
        boolean isGrounded = false;

        applyPlayerActions(currentState, newVelocity, deltaTime);

        newPosition.x += newVelocity.x * deltaTime;

        AABB playerAABB = new AABB(newPosition, PlayerState.PLAYER_SIZE);
        for (AABB platform : platforms) {
            if(playerAABB.collidesWith(platform)) {
                // Si le joueur se déplace vers la droite et collision → collision par la gauche
                if (newVelocity.x > 0) {
                    // Collision par la gauche du joueur
                    newPosition.x = platform.getMinX() - PlayerState.PLAYER_SIZE.x;
                } else {
                    // Collision par la droite du joueur
                    newPosition.x = platform.getMaxX() + PlayerState.PLAYER_SIZE.x;
                }

                // ✅ IMPORTANT : Arrêter le mouvement horizontal
                newVelocity.x = 0;
                break;// une collision à la fois.
            }
        }

        newVelocity.y += GRAVITY * deltaTime;

        newPosition.y += newVelocity.y * deltaTime;

        playerAABB = new AABB(newPosition, PlayerState.PLAYER_SIZE);
        for (AABB platform : platforms) {
            if(playerAABB.collidesWith(platform)) {
                if (newVelocity.y <= 0) {
                    // Chute ou stationnaire → atterrissage
                    newPosition.y = platform.getMaxY() - PlayerState.PLAYER_SIZE.y;
                    newVelocity.y = 0;
                    isGrounded = true;
                } else {
                    newPosition.y = platform.getMinY() + PlayerState.PLAYER_SIZE.y;
                    newVelocity.y = 0;
                }
                break;// une collision à la fois.
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

    private void applyPlayerActions(PlayerState currentState, Vector2f velocity, float deltaTime) {

        // Mouvement horizontal basé sur les actions

        if (currentState.moveLeft()) {
            velocity.x = -currentState.moveSpeed();
        } else if (currentState.moveRight()) {
            velocity.x = currentState.moveSpeed();
        } else if (currentState.isGrounded()) {
            velocity.x *= (1.0f - GROUND_FRICTION * deltaTime);
        }

        // Saut (simple impulsion)
        if (currentState.jump()) {
            velocity.y = currentState.jumpForce();
        }
    }
}