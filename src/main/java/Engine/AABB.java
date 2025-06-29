package Engine;

import org.joml.Vector2f;

public class AABB {

    private final Vector2f position;
    private final Vector2f size;

    public AABB(Vector2f position, Vector2f size) {
        this.position = position;
        this.size = size;
    }

}
