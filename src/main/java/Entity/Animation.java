package Entity;

public class Animation {

    public AnimationState processTransiteAnimation(PlayerState currentState, AnimationState previousAnimation){

        boolean isGrounded = currentState.isGrounded();
        boolean isMoveLeft = currentState.moveLeft();
        boolean isMoveRight = currentState.moveRight();
        boolean isRising = currentState.velocity().y > 0;
        boolean hasVerticaleVelocity = Math.abs(currentState.velocity().y) > 0.1f; // Seuil pour BRAKE
        boolean isJumping = currentState.jump();
        boolean isFalling = currentState.velocity().y < 0 && !isGrounded;
        boolean isLanding = isGrounded && isRising && hasVerticaleVelocity;
        boolean isSkidding = !isMoveLeft && !isMoveRight && isGrounded && Math.abs(currentState.velocity().x) > 0.1f;
    }
}