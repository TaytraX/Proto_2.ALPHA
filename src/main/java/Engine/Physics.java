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

        AABB playerAABB = new AABB(newPosition, PlayerState.PLAYER_SIZE);

        newVelocity.y += GRAVITY * deltaTime;
        newPosition.y += newVelocity.y * deltaTime;

        for (AABB platform : horizontalPlatforms) {
            if(playerAABB.collidesWith(platform)) {
                if (newVelocity.y <= 0) {
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