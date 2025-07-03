package disable;

import Engine.AABB;
import org.joml.Vector2f;

public class Physics {

    public static final float gravity = -9.81f;
    public static final float airFriction = 0.001f;
    public static final float groundFriction = 0.5f;

    public AABB position;
    public AABB platforms;
    public Vector2f velocity;
    public boolean isgrounded = true;


    public Physics(AABB position, AABB platforms) {
        this.position = position;
        this.platforms = platforms;
        this.velocity = new Vector2f();

    }

    public void update(float deltaTime) {

    }



}