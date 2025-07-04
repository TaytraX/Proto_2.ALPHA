package Entity;

public enum AnimationState {

    IDLE,
    RUNNING,
    JUMPING,
    FALLING,
    LANDING;

    // Optionnel : propriétés pour chaque état
    public float getAnimationSpeed() {
        return switch (this) {
            case IDLE -> 0.5f;
            case RUNNING -> 1.0f;
            case JUMPING -> 0.8f;
            case FALLING -> 0.6f;
            case LANDING -> 1.2f;
        };
    }
}