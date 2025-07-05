package Entity;

import Engine.AABB;
import Laucher.Main;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    private AABB position = new AABB(new Vector2f(0, 0), new Vector2f(1, 1));
    public long windowId;

    public Player() {
        windowId = Main.getWindow().getWindowHandle();
    }

    public void input() {
        // Détection des touches
        boolean leftPressed = glfwGetKey(windowId, GLFW_KEY_A) == GLFW_PRESS;
        boolean rightPressed = glfwGetKey(windowId, GLFW_KEY_D) == GLFW_PRESS;
        boolean jumpPressed = glfwGetKey(windowId, GLFW_KEY_SPACE) == GLFW_PRESS;
    }

    private AnimationState determineAnimationState() {

    }

    public void update(float deltaTime) {
    }

}