package Entity;

import Laucher.Main;
import org.jetbrains.annotations.NotNull;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    public long windowId;

    public Player() {
        windowId = Main.getWindow().getWindowHandle();
    }

    public PlayerState input(@NotNull PlayerState state) {
        // Détection des touches
        boolean leftPressed = glfwGetKey(windowId, GLFW_KEY_A) == GLFW_PRESS;
        boolean rightPressed = glfwGetKey(windowId, GLFW_KEY_D) == GLFW_PRESS;
        boolean jumpPressed = glfwGetKey(windowId, GLFW_KEY_SPACE) == GLFW_PRESS ||
                               glfwGetKey(windowId, GLFW_KEY_W) == GLFW_PRESS;

        boolean moveLeft = leftPressed && !rightPressed;
        boolean moveRight = rightPressed && !leftPressed;
        boolean jump = jumpPressed && state.isGrounded();

        boolean facingRight = moveRight || (!moveLeft && state.facingRight());

        // Retourner le state avec les actions déterminées
        return new PlayerState(
                state.position(),
                state.velocity(),
                state.isGrounded(),
                state.animationState(),
                facingRight,
                moveLeft,      // ← Action déterminée
                moveRight,     // ← Action déterminée
                jump,          // ← Action déterminée
                state.moveSpeed(),
                state.jumpForce(),
                System.currentTimeMillis()
        );
    }
}