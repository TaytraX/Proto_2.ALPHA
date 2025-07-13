package Engine;

import Entity.PlayerState;
import org.joml.Vector2f;

import java.util.List;

public class Physics {
    public static final float GRAVITY = -8.00f;
    public static final float GROUND_FRICTION = 5.0f;

    public PlayerState update(PlayerState currentState,  List<AABB> horizontalPlatforms, List<AABB> verticalWalls, float deltaTime) {
        Vector2f newVelocity = new Vector2f(currentState.velocity());
        Vector2f newPosition = new Vector2f(currentState.position());
        boolean isGrounded = false;

        newVelocity.y += GRAVITY * deltaTime;
        newPosition.y += newVelocity.y * deltaTime;

        AABB playerAABB = new AABB(newPosition, PlayerState.PLAYER_SIZE);

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
        for (AABB wall : verticalWalls) {
            if(playerAABB.collidesWith(wall)) {
                if (newVelocity.x >= 0) {
                    // Collision par le droit
                    newPosition.x = wall.getMaxX() + PlayerState.PLAYER_SIZE.x;
                    newVelocity.x = 0;
                } else {
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
    }
}