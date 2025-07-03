package Entity;

import Engine.AABB;
import Laucher.Main;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {

    private Vector2f FOV;
    private Vector2f position;

    public Camera() {
        FOV = new Vector2f((Main.getWidth() / 16f), (Main.getHeight() / 16f));
        position = new Vector2f(0, 0);
    }

    public void followPlayer() {
        AABB playerPosition = Player.getposition();

        this.position.set(
                playerPosition.getMinX() + playerPosition.size().x,
                playerPosition.getMinY() + playerPosition.size().y
        );
    }

    public Matrix4f getProjectionMatrix() {
        return Matrix4f.orthographic(0, Main.getWidth(), Main.getHeight(), 0, 0, 1);
    }

    public Matrix4f getViewMatrix() {
        return Matrix4f.translation(-position.x, -position.y, 0);
    }
}