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
        return position.x - size.x;
    }

    public float getMaxX() {
        return position.x + size.x;
    }

    public float getMinY() {
        return position.y - size.y;
    }

    public float getMaxY() {
        return position.y + size.y;
    }

}
