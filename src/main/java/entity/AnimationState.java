package entity;

public enum AnimationState {
    // Priorité 1 - Animations critiques (temporelles)
    LANDING(1, 300),    // 300ms
    JUMPING(1, 0),

    // Priorité 2 - Physique (basées sur l'état)
    FALLING(2, 0),      // Pas de durée fixe
    RISING(2, 0),
    JUMPING_LEFT(2,0),
    JUMPING_RIGHT(2,0),

    // Priorité 3 - Mouvement (basées sur l'état)
    SKIDDING(3, 0),
    WALKING_LEFT(3, 0),
    WALKING_RIGHT(3, 0),

    // Priorité 4 - Par-Défaut
    IDLE(4, 0);

    private final int priority;
    private final long duration; // 0 = pas de durée fixe

    AnimationState(int priority, long duration) {
        this.priority = priority;
        this.duration = duration;
    }

    public long getDuration() { return duration; }
    public boolean hasFixedDuration() { return duration > 0; }
    public int getPriority() {
        return priority;
    }
}