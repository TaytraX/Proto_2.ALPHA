package Entity;

import Laucher.Main;
import Math.Matrix4f;
import org.joml.Vector2f;

public class Camera {

    private Vector2f FOV;
    private Vector2f position;

    public Camera(int fov) {
        this.FOV = new Vector2f(((float) Main.getWidth() / 16), ((float) Main.getHeight() / 16));
        this.position = new Vector2f(Player.getposition().getMinX(), Player.getposition().getMinY());
    }

    public Matrix4f getProjectionMatrix() {
        return Matrix4f.orthographic(0, Main.getWidth(), Main.getHeight(), 0, 0, 1);
    }

    public Matrix4f getViewMatrix() {
        return Matrix4f.orthographic(0, Main.getWidth(), Main.getHeight(), 0, 0, 1);
    }
}