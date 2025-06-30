package Engine;

import org.joml.Vector2f;

public record AABB(Vector2f position, Vector2f size) {

    public boolean collidesWith(AABB other) {
        return getMaxX() >= other.getMinX() &&
                getMinX() <= other.getMaxX() &&
                getMaxY() >= other.getMinY() &&
                getMinY() <= other.getMaxY();
    }

    public float getMinX() {
        return this.position.x - this.size.x / 2;
    }

    public float getMaxX() {
        return this.position.x + this.size.x / 2;
    }

    public float getMinY() {
        return this.position.y - this.size.y / 2;
    }

    public float getMaxY() {
        return this.position.y + this.size.y / 2;
    }

}
