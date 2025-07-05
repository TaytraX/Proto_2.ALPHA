package Engine;

import Entity.PlayerState;
import org.joml.Vector2f;

import java.util.List;

public class Physics {

    public static final float gravity = -9.81f;
    public static final float airFriction = 0.001f;
    public static final float groundFriction = 0.5f;

    public PlayerState update(PlayerState currentState, float deltaTime, List<AABB> platforms) {
        Vector2f newVelocity = new Vector2f(currentState.velocity());
        Vector2f newPosition = new Vector2f(currentState.position());
        boolean isGrounded = false;

        if(!currentState.isGrounded()) {
            newVelocity.y += gravity * deltaTime;
        }

        float friction = currentState.isGrounded() ? groundFriction : airFriction;
        newVelocity.y *= (1.0f - friction * deltaTime);

        newPosition.x += newVelocity.x * deltaTime;
        newPosition.y += newVelocity.y * deltaTime;

        AABB playerAABB = new AABB (newPosition, PlayerState.PLAYER_SIZE);

        for (AABB platform : platforms) {
            if (platform.collidesWith(platform)) {

                float overlapX = Math.min(playerAABB.getMaxX() - platform.getMinX(), platform.getMaxX() - playerAABB.getMinX());
                float overlapY = Math.min(playerAABB.getMaxY() - platform.getMinY(), platform.getMaxY() - playerAABB.getMinY());

                if (overlapX < overlapY) {
                    if (newVelocity.x > 0){
                        newPosition.x = platform.getMinX() - PlayerState.PLAYER_SIZE.x;
                    }else {
                        newPosition.x = platform.getMaxX() + PlayerState.PLAYER_SIZE.x;
                    }
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
                currentState.moveSpeed(),
                currentState.jumpForce(),
                currentState.timestamp()
        );
    }
}