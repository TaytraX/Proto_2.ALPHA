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
        if (position.x + size.x/2 >= other.position.x - other.size.x/2 &&
                position.x - size.x/2 <= other.position.x + other.size.x/2 &&
                position.y + size.y/2 >= other.position.y - other.size.y/2 &&
                position.y - size.y/2 <= other.position.y + other.size.y/2) {
            return true;
        }
        return false;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getSize() {
        return size;
    }

    public float getMinX() {
        return position.x - size.x/2;
    }

    public float getMaxX() {
        return position.x + size.x/2;
    }

    public float getMinY() {
        return position.y - size.y/2;
    }

    public float getMaxY() {
        return position.y + size.y/2;
    }

}
