package Entity;

public class Animation {

    public AnimationState processTransiteAnimation(){

        boolean isMoveLeft = false, isMoveRight = false, isJump = false, isFalling = false, isStop = false, landing = false, isIdle = false, isgrounded = false;

        if(isMoveLeft && isgrounded) return AnimationState.WALKLEFT;
        if(isMoveRight && isgrounded) return AnimationState.WALKRIGHT;
        if(isJump) return AnimationState.JUMPING;
        if(isFalling && isgrounded) return AnimationState.FALLING;
        if(isStop) return AnimationState.BRAKE;
        if(landing) return AnimationState.LANDING;
        return AnimationState.IDLE;
    }
}