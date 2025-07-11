package Engine;

import Entity.PlayerState;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class Physics {
    public static final float GRAVITY = -3.00f;
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

                //Calcule des penetrations
                float penetrationFromLeft = playerAABB.getMaxX() - platform.getMinX();
                float penetrationFromRight = playerAABB.getMaxX() - platform.getMinX();

                // Prendre la plus petite pénétration (= la vraie direction)
                if (penetrationFromLeft < penetrationFromRight) {
                    // Collision par la gauche du joueur
                    newPosition.x -= penetrationFromLeft;
                } else {
                    // Collision par la droite du joueur
                    newPosition.x += penetrationFromRight;
                }

                // ✅ IMPORTANT : Arrêter le mouvement horizontal
                newVelocity.x = 0;

                // Mettre à jour l'AABB après correction
                playerAABB = new AABB(newPosition, PlayerState.PLAYER_SIZE);
            }

            if(playerAABB.collidesWith(platform)) {

                //Calcule des penetrations
                float penetrationFromUp = playerAABB.getMaxX() - platform.getMinX();
                float penetrationFromBound = playerAABB.getMaxX() - platform.getMinX();

                // Prendre la plus petite pénétration (= la vraie direction)
                 if (penetrationFromUp < penetrationFromBound) {
                    // ✅ IMPORTANT : Arrêter le mouvement vertical
                    newVelocity.y = 0;
                } else {
                    isGrounded = true;
                }

                // Mettre à jour l'AABB après correction
                playerAABB = new AABB(newPosition, PlayerState.PLAYER_SIZE);
            }
        }

        if (!isGrounded) {
            // Collision par la droite du joueur
            newPosition.y += GRAVITY * deltaTime;
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