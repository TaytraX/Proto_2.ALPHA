package Entity;

import Laucher.Main;
import Render.Window;
import org.joml.Vector2f;

public class Camera {

    private Vector2f FOV;
    private Vector2f position;
    private Vector2f target = new Vector2f(position.x, position.y);

    public Camera() {
        this.FOV = new Vector2f(Main.getWidth(), Main.getHeight());
        this.position = new Vector2f(0, 0);
    }
}