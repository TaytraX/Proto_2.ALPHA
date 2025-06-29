package Entity;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class Player {

    private final float SPEED = 5.0f;
    private final float JUMP_FORCE = 10.0f;
    public Vector2f position = new Vector2f(0, 0);

    public Player(Vector2f position) {
        this.position = position;
    }

    public void update(float deltaTime) {
    }

    public void input(float deltaTime) {
        if (GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
            position.y += JUMP_FORCE * deltaTime;
        }
    }
}