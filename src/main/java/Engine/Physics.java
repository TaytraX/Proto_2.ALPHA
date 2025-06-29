package Engine;

import org.joml.Vector2f;

public class Physics {

    public final float gravity = -9.81f;
    public static final float airFriction = 0.001f;
    public static final float groundFriction = 0.5f;

    public Vector2f position;
    public Vector2f platforms;
    public Vector2f velocity;
    public boolean isground = true;


    public Physics(Vector2f position) {

        this.position = new Vector2f(position);
        this.velocity = new Vector2f(0, 0);

    }

    private void update(float deltaTime) {

        if(!isground) {
            velocity.y += gravity * deltaTime;
        }
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;

    }

    public void checkAndResolveCollisions(Vector2f position, Vector2f platforms) {
        this.position = position;
        this.platforms = platforms;

    }

}