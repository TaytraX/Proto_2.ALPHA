package Render;

import Engine.Renderable;
import Entity.Camera;
import Entity.Player;
import Laucher.Main;
import org.lwjgl.opengl.GL30C;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class RenderPlayer implements Renderable {

    private int VAO, VBO, EBO, textureVBO;

    private final Shader shader;
    private final Texture texture;
    private final Window window;
    private Camera camera;
    private Player player;

    public RenderPlayer(Shader shader, Texture texture) {
        this.shader = shader;
        this.texture = texture;
        this.window = Main.getWindow();
    }

    @Override
    public void initialize() {

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();
        textureVBO = glGenBuffers();

        // ✅ Géométrie du joueur (quad 2D)
        float[] vertices = {
               -0.4f,  -0.6f,  // Bas gauche
               -0.4f,   0.6f,  // Haut gauche
                0.4f,   0.6f,  // Haut droit
                0.4f,  -0.6f   // Bas droit
        };

        int[] indices = {
                0, 1, 3,  // Premier triangle
                3, 1, 2   // Deuxième triangle
        };

        float[] textureCoords = {
                0.0f, 1.0f,  // Bas gauche -> correspond au haut de l'image
                0.0f, 0.0f,  // Haut gauche -> correspond au bas de l'image
                1.0f, 0.0f,  // Haut droit -> correspond au bas de l'image
                1.0f, 1.0f   // Bas droit -> correspond au haut de l'image
        };

        glBindVertexArray(VAO);

        glBindBuffer(GL30C.GL_ARRAY_BUFFER, VBO);
        glBufferData(GL30C.GL_ARRAY_BUFFER, vertices, GL30C.GL_STATIC_DRAW);

        glVertexAttribPointer(0, 2, GL30C.GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL30C.GL_ARRAY_BUFFER, EBO);
        glBufferData(GL30C.GL_ARRAY_BUFFER, indices, GL30C.GL_STATIC_DRAW);

        glBindBuffer(GL30C.GL_ARRAY_BUFFER, textureVBO);
        glBufferData(GL30C.GL_ARRAY_BUFFER, textureCoords, GL30C.GL_STATIC_DRAW);

        glBindVertexArray(0);
        glBindBuffer(GL30C.GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL30C.GL_ELEMENT_ARRAY_BUFFER, 0);

    }

    @Override
    public void render(Camera camera, float deltaTime) {

    }

    @Override
    public void cleanup() {

    }
}