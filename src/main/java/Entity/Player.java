package Entity;

import Engine.ThreadManager;
import Laucher.Main;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

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

        // 3. Appliquer les mouvements
        Vector2f newVelocity = applyMovement(state, moveLeft, moveRight, jump);

        boolean facingRight = moveRight || (!moveLeft && state.facingRight());

        // Retourner le state avec les actions déterminées
        return new PlayerState(
                state.position(),
                newVelocity,
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

    public Vector2f applyMovement(PlayerState state, boolean moveLeft, boolean moveRight, boolean jump) {

        // Calculer nouvelle vitesse selon les inputs
        Vector2f newVelocity = new Vector2f(state.velocity());

        if (moveLeft) {
            newVelocity.x = -state.moveSpeed();
        } else if (moveRight) {
            newVelocity.x = state.moveSpeed();
        }
        // Pas de friction ici - Physics s'en charge

        if (jump) {
            newVelocity.y = state.jumpForce();
        }
        return newVelocity;
    }
}