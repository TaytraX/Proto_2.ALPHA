package Render;

import Engine.AABB;
import Engine.Engine;
import Engine.Renderable;
import Engine.GameLogger;
import Entity.Camera;
import Laucher.Main;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30C;

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
            for (AABB platform : platforms) {

                float[] vertices = createQuadVertices(platform);
                transformationMatrix.identity().translation(
                    platform.getMinX() + platform.halfSize().x,
                    platform.getMinY() + platform.halfSize().y,
                    0.0f
                );

                shader.use();

                matrixBuffer.clear();
                transformationMatrix.get(matrixBuffer);
                shader.setUniformMat4f("transformationMatrix", matrixBuffer);

                matrixBuffer.clear();
                camera.getProjectionMatrix().get(matrixBuffer);
                shader.setUniformMat4f("projectionMatrix", matrixBuffer);

                matrixBuffer.clear();
                camera.getViewMatrix().get(matrixBuffer);
                shader.setUniformMat4f("viewMatrix", matrixBuffer);

                GL30C.glBindVertexArray(VAO);
                glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
                GL30C.glBindVertexArray(0);
            }
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