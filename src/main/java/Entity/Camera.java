package Entity;

import Engine.ThreadManager;
import Laucher.Main;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {
    private Vector2f position;
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();

    // ✅ Paramètres de suivi
    private float followSpeed = 2.0f;        // Vitesse de suivi
    private Vector2f offset = new Vector2f(0, 1.0f); // Décalage (joueur un peu en bas)
    private Vector2f deadZone = new Vector2f(2.0f, 1.5f); // Zone morte

    public Camera() {
        Vector2f FOV = new Vector2f((Main.getWidth() / 16f), (Main.getHeight() / 16f));
        position = new Vector2f(0, 0);
    }

    public void followPlayer(float deltaTime) {
        PlayerState currentState = ThreadManager.playerState.get();
        if (currentState == null) return;

        Vector2f playerPos = currentState.position();

        // ✅ Position cible avec offset
        Vector2f targetPos = new Vector2f(
                playerPos.x + offset.x,
                playerPos.y + offset.y
        );

        // ✅ Calcul de la distance
        Vector2f distance = new Vector2f(
                targetPos.x - position.x,
                targetPos.y - position.y
        );

        // ✅ Dead zone : ne pas bouger si le joueur est proche du centre
        if (Math.abs(distance.x) > deadZone.x) {
            float moveX = distance.x > 0 ?
                    (distance.x - deadZone.x) :
                    (distance.x + deadZone.x);
            position.x += moveX * followSpeed * deltaTime;
        }

        if (Math.abs(distance.y) > deadZone.y) {
            float moveY = distance.y > 0 ?
                    (distance.y - deadZone.y) :
                    (distance.y + deadZone.y);
            position.y += moveY * followSpeed * deltaTime;
        }
    }

    // ✅ Méthode pour téléporter (utile pour spawn/respawn)
    public void snapToPlayer() {
        PlayerState currentState = ThreadManager.playerState.get();
        if (currentState == null) return;

        Vector2f playerPos = currentState.position();
        this.position.set(
                playerPos.x + offset.x,
                playerPos.y + offset.y
        );
    }

    // ✅ Configuration publique
    public void setFollowSpeed(float speed) {
        this.followSpeed = speed;
    }

    public void setOffset(float x, float y) {
        this.offset.set(x, y);
    }

    public void setDeadZone(float x, float y) {
        this.deadZone.set(x, y);
    }

    // Reste du code inchangé...
    public Matrix4f getProjectionMatrix() {
        float worldWidth = 40.0f;
        float worldHeight = 25.0f;
        return projectionMatrix.identity().ortho(
                -worldWidth/2, worldWidth/2,
                -worldHeight/2, worldHeight/2,
                0, 1
        );
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().translation(-position.x, -position.y, 0);
    }
}