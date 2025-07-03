package Entity;

import Engine.AABB;
import Laucher.Main;
import Render.Window;
import org.joml.Vector2f;

import static Laucher.Main.window;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Player {
    private final AABB position = new AABB(new Vector2f(0, 0), new Vector2f(1, 1));
    public Window window;


    public AABB getposition(){
        return position;
    }

    public void input() {
        glfwGetKey(window, GLFW_KEY_SPACE);
    }

    public void update(float deltaTime) {
    }

}