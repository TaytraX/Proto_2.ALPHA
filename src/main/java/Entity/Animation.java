package Entity;

public class Animation {

    public AnimationState processTransiteAnimation(PlayerState currentState, AnimationState previousAnimation){

        boolean isGrounded = currentState.isGrounded();
        boolean isMoveLeft = currentState.moveLeft();
        boolean isMoveRight = currentState.moveRight();
        boolean isRising = currentState.velocity().y > 0;
        boolean hasVerticalVelocity = Math.abs(currentState.velocity().y) > 0.1f; // Seuil pour BRAKE
        boolean isJumping = currentState.jump();
        boolean isFalling = currentState.velocity().y < 0 && !isGrounded;
        boolean isLanding = isGrounded && hasVerticalVelocity;
        boolean isSkidding = !isMoveLeft && !isMoveRight && isGrounded && Math.abs(currentState.velocity().x) > 0.1f;

        if (isJumping) return AnimationState.JUMPING;
        if (isFalling) return AnimationState.FALLING;
        if (isLanding) return AnimationState.LANDING;
        if (isSkidding) return AnimationState.SKIDDING;
        if (isRising) return AnimationState.RISING;
        if (isMoveLeft) return AnimationState.WALKING_LEFT;
        if (isMoveRight) return AnimationState.WALKING_RIGHT;
        if (previousAnimation == AnimationState.SKIDDING) return AnimationState.SKIDDING;
        return AnimationState.IDLE;
    }
}