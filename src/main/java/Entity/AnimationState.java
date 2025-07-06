package Entity;

public enum AnimationState {

    IDLE,
    WALKING_LEFT,
    WALKING_RIGHT,
    JUMPING,
    FALLING,
    SKIDDING,
    RISING,
    LANDING;

    // Optionnel : propriétés pour chaque état
    public float getAnimationSpeed() {
        return switch (this) {
            case IDLE -> 0.5f;
            case WALKING_LEFT, WALKING_RIGHT -> 1.0f;
            case JUMPING -> 0.8f;
            case RISING -> 0.1f;
            case FALLING -> 0.6f;
            case SKIDDING -> 0.3f;
            case LANDING -> 1.2f;
        };
    }
}