package Engine;

import Entity.PlayerState;
import org.joml.Vector2f;

import java.util.List;

public class Physics {
    public static final float GRAVITY = -8.00f;

    public PlayerState update(PlayerState currentState,  List<AABB> horizontalPlatforms, List<AABB> verticalWalls, float deltaTime) {
        Vector2f newVelocity = new Vector2f(currentState.velocity());
        Vector2f newPosition = new Vector2f(currentState.position());
        boolean isGrounded = false;

        // Calcul de la vitesse max basée uniquement sur la gravité
        float maxSpeed = (float) Math.sqrt(Math.abs(GRAVITY) * 3.0f);

        // Appliquer la gravité
        newVelocity.y += GRAVITY * deltaTime;

        // Limiter la vitesse horizontale
        newVelocity.x = Math.max(-maxSpeed, Math.min(maxSpeed, newVelocity.x));
        newPosition.y += newVelocity.y * deltaTime;

        AABB playerAABB = new AABB(newPosition, PlayerState.PLAYER_SIZE);
        // Avant les corrections de collision
        float impactVelocity = newVelocity.y;
        boolean wasGrounded = currentState.isGrounded();

        for (AABB platform : horizontalPlatforms) {

            if(playerAABB.collidesWith(platform)) {
                if (newVelocity.y < 0) {
                    // Chute ou stationnaire → atterrissage
                    newPosition.y = platform.getMaxY() + PlayerState.PLAYER_SIZE.y;
                    newVelocity.y = 0;
                    isGrounded = true;
                } else {
                    // Collision par le haut (coup de tête)
                    newPosition.y = platform.getMinY() - PlayerState.PLAYER_SIZE.y;
                    newVelocity.y = 0;
                }
                break;// une collision à la fois.
            }
        }

        newPosition.x += newVelocity.x * deltaTime;

        // Décélération exponentielle basée sur la gravité
        if (isGrounded && !currentState.moveLeft() && !currentState.moveRight()) {
            float frictionRate = Math.abs(GRAVITY) * 0.30f; // 30% de la gravité
            newVelocity.x *= Math.max(0, 1.0f - frictionRate * deltaTime);
        }

        for (AABB wall : verticalWalls) {
            if(playerAABB.collidesWith(wall)) {
                if (newVelocity.x >= 0) {
                    // Collision par le droit
                    newPosition.x = wall.getMaxX() + PlayerState.PLAYER_SIZE.x;
                    newVelocity.x = 0;
                } else if (newVelocity.x < 0) {
                    // Collision par le gauche
                    newPosition.x = wall.getMinX() - PlayerState.PLAYER_SIZE.x;
                    newVelocity.x = 0;
                }
                break;// une collision à la fois.
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
                currentState.accelerationSpeed(),
                currentState.moveSpeed(),
                currentState.jumpForce(),
                currentState.timestamp()
        );
    }
/*
    public PlayerState update(PlayerState currentState, float deltaTime) {
        Vector2f newVelocity = new Vector2f(currentState.velocity());
        Vector2f newPosition = new Vector2f(currentState.position());
        boolean isGrounded = false;

        // Appliquer la vélocité à la position
        newPosition.x += newVelocity.x * deltaTime;
        newPosition.y += newVelocity.y * deltaTime;

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
    }*/
}