package Engine;

import org.joml.Vector2f;

public class AABB {

    Vector2f position, size;

    public AABB(Vector2f position, float width, float height) {
        this.position = position;
        this.size = new Vector2f(width, height);
    }

    public AABB(Vector2f position, Vector2f size) {
        this.position = position;
        this.size = size;
    }

    public boolean collidesWith(AABB other) {
        return getMaxX() >= (other.getMinX() * 2) &&
               getMinX() <= (other.getMaxX() * 2) &&
               getMaxY() >= (other.getMinY() * 2) &&
               getMinY() <= (other.getMaxY() * 2);
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
