package Render;

import Engine.AABB;
import Engine.Engine;
import Engine.Renderable;
import Engine.GameLogger;
import Entity.Camera;
import Laucher.Main;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class GroundRenderer implements Renderable {

    private int VAO;
    private int EBO;
    private int VBO;
    private final Shader shader;

    private final FloatBuffer matrixBuffer = org.lwjgl.BufferUtils.createFloatBuffer(16);
    private final Matrix4f transformationMatrix = new Matrix4f();

    Engine engine = Main.getEngine();
    List<AABB> platforms = engine.getHorizontalPlatforms();
    List<AABB> walls = engine.getVerticalWalls();

    public GroundRenderer() {
        shader = new Shader("platforms");
    }

    @Override
    public void initialize() {
        // Quad unitaire centré en (0,0) avec taille 1x1
        float[] vertices = {
                -0.5f, -0.5f,  // Bas gauche
                -0.5f,  0.5f,  // Haut gauche
                0.5f,  0.5f,  // Haut droit
                0.5f, -0.5f   // Bas droit
        };

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        // Setup basique du VAO (sera mis à jour dynamiquement)
        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // Indices pour 2 triangles (quad)
        int[] indices = {0, 1, 2, 2, 3, 0};
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    @Override
    public void render(Camera camera, float deltaTime) {
        try {
            List<AABB> platforms = Main.getEngine().getHorizontalPlatforms();

            shader.use();

            // Matrices communes
            matrixBuffer.clear();
            camera.getProjectionMatrix().get(matrixBuffer);
            shader.setUniformMat4f("projectionMatrix", matrixBuffer);

            matrixBuffer.clear();
            camera.getViewMatrix().get(matrixBuffer);
            shader.setUniformMat4f("viewMatrix", matrixBuffer);

            glBindVertexArray(VAO);

            for (AABB platform : platforms) {
                // Transformation : Position + Scale
                transformationMatrix.identity()
                        .translation(platform.position().x, platform.position().y, 0.0f)
                        .scale(platform.halfSize().x * 2.0f, platform.halfSize().y * 2.0f, 1.0f);

                matrixBuffer.clear();
                transformationMatrix.get(matrixBuffer);
                shader.setUniformMat4f("transformationMatrix", matrixBuffer);

                glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
            }

            glBindVertexArray(0);
        } catch (Exception e) {
            GameLogger.error("Erreur dans le rendu du joueur", e);
            // Ne pas faire crash le jeu, juste ignorer ce frame
        }
    }

    private float[] createQuadVertices(AABB aabb) {
        float minX = aabb.getMinX();
        float maxX = aabb.getMaxX();
        float minY = aabb.getMinY();
        float maxY = aabb.getMaxY();

        return new float[] {
                minX, minY,
                minX, maxY,
                maxX, maxY,
                maxX, minY
        };
    }

    @Override
    public void cleanup() {

    }
}