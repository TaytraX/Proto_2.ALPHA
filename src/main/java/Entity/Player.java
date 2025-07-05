package Entity;

import Engine.AABB;
import Laucher.Main;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

public class Player {
    public long windowId;

    public Player() {
        windowId = Main.getWindow().getWindowHandle();
    }

    public PlayerState input() {
        // Détection des touches
        if (glfwGetKey(windowId, GLFW_KEY_A) == GLFW_PRESS) return;
        if (glfwGetKey(windowId, GLFW_KEY_D) == GLFW_PRESS) return;
        if (glfwGetKey(windowId, GLFW_KEY_SPACE) == GLFW_PRESS) return;
    }
}