package Render;

import Engine.AABB;
import Engine.GameLogger;
import Engine.Renderable;
import Engine.ThreadManager;
import Entity.AnimationController;
import Entity.AnimationState;
import Entity.Camera;
import Entity.PlayerState;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30C;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class BackgroundRenderer implements Renderable {

    private int VAO;

    private final FloatBuffer matrixBuffer = org.lwjgl.BufferUtils.createFloatBuffer(16);
    private final Matrix4f transformationMatrix = new Matrix4f();
    private final Shader shader;

    public BackgroundRenderer() {
        shader = new Shader("background");
    }

    @Override
    public void initialize() {

        VAO = glGenVertexArrays();
        int VBO = glGenBuffers();
        int EBO = glGenBuffers();

        float[] vertices = {
               -1.0f, -1.0f,  // Bas gauche
                1.0f, -1.0f,  // Bas droit
                1.0f,  1.0f,  // Haut droit
               -1.0f,  1.0f   // Haut gauche
        };

        FloatBuffer vertexBuffer = org.lwjgl.BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        // Ordre des indices correct pour le sens antihoraire
        int[] indices = {
                0, 1, 2,  // Premier triangle
                2, 3, 0   // Deuxième triangle
        };

        IntBuffer indexBuffer = org.lwjgl.BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();

        glBindVertexArray(VAO);

        glBindBuffer(GL30C.GL_ARRAY_BUFFER, VBO);
        glBufferData(GL30C.GL_ARRAY_BUFFER, vertexBuffer, GL30C.GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL30C.GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL30C.GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL30C.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL30C.GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    @Override
    public void render(Camera camera, float deltaTime) {
        try {

            shader.use();
            glBindVertexArray(VAO);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);

        } catch (Exception e) {
            GameLogger.error("Erreur dans le rendu du background", e);
        }
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }
}