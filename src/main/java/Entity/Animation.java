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
            case IDLE -> isIdle ? AnimationState.IDLE : AnimationState.WALKLEFT;
            case WALKLEFT -> isMoveLeft ? AnimationState.WALKLEFT : AnimationState.WALKRIGHT;
            case WALKRIGHT -> isMoveRight ? AnimationState.WALKRIGHT : AnimationState.JUMPING;
            case JUMPING -> isJump ? AnimationState.JUMPING : AnimationState.FALLING;
            case FALLING -> isFalling ? AnimationState.FALLING : AnimationState.BRAKE;
            case BRAKE -> isStop ? AnimationState.BRAKE : AnimationState.LANDING;
            case LANDING -> isIdle ? AnimationState.LANDING : AnimationState.IDLE;
        };
    }
}