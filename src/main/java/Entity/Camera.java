package Entity;

import Engine.AABB;
import Laucher.Main;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {

    private Vector2f FOV;
    private Vector2f position;
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();

    public Camera() {
        FOV = new Vector2f((Main.getWidth() / 16f), (Main.getHeight() / 16f));
        position = new Vector2f(0, 0);
    }

    public void followPlayer() {
        AABB playerPosition = new AABB(new Vector2f(0, 0), new Vector2f(1, 1));

        this.position.set(
                playerPosition.getMinX() + playerPosition.size().x,
                playerPosition.getMinY() + playerPosition.size().y
        );
    }

    public Matrix4f getProjectionMatrix() {
        float worldWidth = 40.0f;  // 40 unités monde
        float worldHeight = 25.0f; // 25 unités monde
        return projectionMatrix.identity().ortho(
                -worldWidth/2, worldWidth/2,
                -worldHeight/2, worldHeight/2,
                0, 1
        );
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().translation(-position.x, -position.y, 0);
    }
}