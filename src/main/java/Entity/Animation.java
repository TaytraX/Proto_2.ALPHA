package Entity;

public class Animation {

    public AnimationState animation(PlayerState state) {

        boolean isMoveLeft = false, isMoveRight = false, isJump = false, isFalling = false, isStop = false, landing = false, isIdle = false;

        if (state.moveLeft()) isMoveLeft = true;
        if (state.moveRight()) isMoveRight = true;
        if (state.jump()) isJump = true;
        if (state.isGrounded()) isStop = true;
        if (state.velocity().y < 0) isFalling = true;
        if (state.velocity().y == 0) landing = true;
        if (!isMoveLeft && !isMoveRight && !isJump && !isFalling && !isStop && !landing) isIdle = true;

        return switch (state.animationState()) {
            case IDLE ->  AnimationState.IDLE;
            case WALKLEFT -> AnimationState.WALKLEFT;
            case WALKRIGHT -> AnimationState.WALKRIGHT;
            case JUMPING -> AnimationState.JUMPING;
            case FALLING -> AnimationState.FALLING;
            case BRAKE -> AnimationState.BRAKE;
            case LANDING -> AnimationState.LANDING;
        };
    }
}