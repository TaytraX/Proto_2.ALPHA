package Entity;

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
        boolean jump = jumpPressed && state.isGrounded();

        // 3. Appliquer les mouvements
        Vector2f newVelocity = applyMovement(state, moveLeft, moveRight, jump, deltaTime);

        boolean facingRight = moveRight || (!moveLeft && state.facingRight());

        // Retourner le state avec les actions déterminées
        return new PlayerState(
                state.position(),
                newVelocity,
                state.isGrounded(),
                state.wasGrounded(),
                state.previousVelocity(),
                state.animationState(),
                facingRight,
                moveLeft,      // ← Action déterminée
                moveRight,     // ← Action déterminée
                jump,
                state.moveSpeed(), // ← Action déterminée
                state.moveSpeed(),
                state.jumpForce(),
                System.currentTimeMillis()
        );
    }

    public Vector2f applyMovement(PlayerState state, boolean moveLeft, boolean moveRight, boolean jump, float deltaTime) {
        Vector2f newVelocity = new Vector2f(state.velocity());

        // Constantes d'accélération
        final float ACCELERATION = state.accelerationSpeed();  // Vitesse d'accélération
        final float MAX_SPEED = state.moveSpeed();  // Décélération quand pas d'input

        // Gestion accélération horizontale
        if (moveLeft) {
            newVelocity.x -= ACCELERATION;
            newVelocity.x -= MAX_SPEED * deltaTime; // Limite gauche
        } else if (moveRight) {
            newVelocity.x += ACCELERATION;
            newVelocity.x += MAX_SPEED * deltaTime;  // Limite droite
        }

        // Saut inchangé
        if (jump) {
            newVelocity.y += state.jumpForce() * deltaTime;
        }

        return newVelocity;
    }
}