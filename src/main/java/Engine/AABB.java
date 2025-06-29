package Engine;

import org.joml.Vector2f;

public class AABB {

    private final Vector2f position;
    private final Vector2f size;

    public AABB(Vector2f position, Vector2f size) {
        this.position = position;
        this.size = size;
    }

    public AABB(Vector2f position, float width, float height) {
        this.size = new Vector2f(width, height);
        this.position = position;
    }

    public boolean collidesWith(AABB other) {
        return getMaxX() >= getMinX() &&
               getMinX() <= getMaxX() &&
               getMaxY() >= getMinY() &&
               getMinY() <= getMaxY();
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getSize() {
        return size;
    }

    public float getMinX() {
        return this.position.x - this.size.x/2;
    }

    public float getMaxX() {
        return this.position.x + this.size.x/2;
    }

    public float getMinY() {
        return this.position.y - this.size.y/2;
    }

    public float getMaxY() {
        return this.position.y + this.size.y/2;
    }

}
