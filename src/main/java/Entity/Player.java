package Entity;

import Engine.AABB;
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

    public PlayerState update(@NotNull PlayerState state, float deltaTime) {
        // Détection des touches
        boolean leftPressed = glfwGetKey(windowId, GLFW_KEY_A) == GLFW_PRESS;
        boolean rightPressed = glfwGetKey(windowId, GLFW_KEY_D) == GLFW_PRESS;
        boolean jumpPressed = glfwGetKey(windowId, GLFW_KEY_SPACE) == GLFW_PRESS ||
                               glfwGetKey(windowId, GLFW_KEY_W) == GLFW_PRESS;

        boolean moveLeft = leftPressed && !rightPressed;
        boolean moveRight = rightPressed && !leftPressed;
        boolean jump = jumpPressed;

        // 3. Appliquer les mouvements
        Vector2f newPosition = applyMovement(state, moveLeft, moveRight, jump, deltaTime);

        boolean facingRight = moveRight || (!moveLeft && state.facingRight());

        // Retourner le state avec les actions déterminées
        return new PlayerState(
                newPosition,
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

    public Vector2f applyMovement(PlayerState state, boolean moveLeft, boolean moveRight, boolean jump, float deltaTime) {

        // Calculer nouvelle vitesse selon les inputs
        Vector2f newPosition = new Vector2f(state.position());

        if (moveLeft) {
            newPosition.x -= state.moveSpeed() * deltaTime;
        } else if (moveRight) {
            newPosition.x += state.moveSpeed() * deltaTime;
        }
        // Pas de friction ici - Physics s'en charge

        if (jump) {
            newPosition.y += state.jumpForce() * deltaTime;
        }
        return newPosition;
    }
}