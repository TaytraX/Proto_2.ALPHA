package Engine;

import Entity.PlayerState;
import org.joml.Vector2f;

import java.util.List;

public class Physics {
    public static final float GRAVITY = -8.00f;

    public PlayerState update(PlayerState currentState,  List<AABB> platforms, float deltaTime) {
        Vector2f newVelocity = new Vector2f(currentState.velocity());
        Vector2f newPosition = new Vector2f(currentState.position());
        boolean isGrounded = false;

        // Calcul de la vitesse max basée uniquement sur la gravité
        float maxSpeed = (float) Math.sqrt(Math.abs(GRAVITY) * 3.0f);

        // Appliquer la gravité
        newVelocity.y += GRAVITY * deltaTime;

        // 2. ÉTAPE Y : Appliquer le mouvement Y puis tester toutes les plateformes
        newPosition.y += newVelocity.y * deltaTime;
        AABB playerAABB = new AABB(newPosition, PlayerState.PLAYER_SIZE);

        for (AABB platform : platforms) {
            if(playerAABB.collidesWith(platform)) {
                float overlapX = playerAABB.getOverlapX(platform);
                float overlapY = playerAABB.getOverlapY(platform);
                float tolerance = 0.01f;

                if (overlapY < overlapX - tolerance) {
                    // Traiter collision verticale
                    if (newVelocity.y < 0) {
                        newVelocity.y = 0;
                        newPosition.y = platform.getMaxY() + PlayerState.PLAYER_SIZE.y;
                        isGrounded = true;
                    } else if (newVelocity.y > 0) {
                        newVelocity.y = 0;
                        newPosition.y = platform.getMinY() - PlayerState.PLAYER_SIZE.y;
                    }
                }
            }
        }

        // Décélération exponentielle basée sur la gravité
        if (isGrounded && !currentState.moveLeft() && !currentState.moveRight()) {
            float frictionRate = Math.abs(GRAVITY) * 0.30f; // 30% de la gravité
            newVelocity.x *= Math.max(0, 1.0f - frictionRate * deltaTime);
        }

        newPosition.x += newVelocity.x * deltaTime;

        playerAABB = new AABB(newPosition, PlayerState.PLAYER_SIZE);

        float impactVelocity = newVelocity.y;
        boolean wasGrounded = currentState.isGrounded();

        newVelocity.x = Math.max(-maxSpeed, Math.min(maxSpeed, newVelocity.x));

        // 1. ÉTAPE X : Traiter toutes les plateformes pour X
        for (AABB platform : platforms) {
            if(playerAABB.collidesWith(platform)) {
                float overlapX = playerAABB.getOverlapX(platform);
                float overlapY = playerAABB.getOverlapY(platform);
                float tolerance = 0.01f;

                if (overlapX < overlapY - tolerance) {
                    // Traiter collision horizontale
                    if (newVelocity.x > 0) {
                        newVelocity.x = 0;
                        newPosition.x = platform.getMinX() - PlayerState.PLAYER_SIZE.x;
                    } else if (newVelocity.x < 0) {
                        newVelocity.x = 0;
                        newPosition.x = platform.getMaxX() + PlayerState.PLAYER_SIZE.x;
                    }
                }
            }
        }

        return new PlayerState(
                newPosition,
                newVelocity,
                isGrounded,
                wasGrounded,
                impactVelocity,
                currentState.animationState(),
                currentState.facingRight(),
                currentState.moveLeft(),
                currentState.moveRight(),
                currentState.jump(),
                currentState.force(),
                currentState.timestamp()
        );
    }
}