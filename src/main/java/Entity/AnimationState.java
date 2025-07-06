package Entity;

public enum AnimationState {

    IDLE,
    WALKLEFT,
    WALKRIGHT,
    JUMPING,
    FALLING,
    BRAKE,
    LANDING;

    // Optionnel : propriétés pour chaque état
    public float getAnimationSpeed() {
        return switch (this) {
            case IDLE -> 0.5f;
            case WALKLEFT, WALKRIGHT -> 1.0f;
            case JUMPING -> 0.8f;
            case FALLING -> 0.6f;
            case BRAKE -> 0.3f;
            case LANDING -> 1.2f;
        };
    }
}