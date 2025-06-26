package Render;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL.*;
import Math.Matrix4f;

public class Window {

    private static final String VERTEX_SHADER = """
        #version 330 core
        
        layout(location = 0) in vec2 position;
        
        uniform mat4 projection;
        
        void main() {
            gl_Position = projection * vec4(position, 0.0, 1.0);
        }
        """;
    private static final String FRAGMENT_SHADER = """
        #version 330 core

        out vec4 fragColor;

        void main() {
            fragColor = vec4(1.0, 1.0, 1.0, 1.0); // Blanc
        }
       """;


    private final String title;

    private int width, height;
    private long window;
    private final boolean vSync;

    private boolean isFullscreen = false;
    private int windowedWidth, windowedHeight;
    private int windowedPosX, windowedPosY;
    private Shader shader;

    public Window(String title, int width, int height, boolean vSync) {
        this.vSync = vSync;
        this.height = height;
        this.width = width;
        this.title = title;
        this.windowedWidth = width;
        this.windowedHeight = height;
    }

    public void init(){
        // Configuration des callbacks d'erreur
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3); // Changé de 2 à 3
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        boolean maximised = false;
        if(width == 0 || height == 0){
            width = 100;
            height = 100;
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            maximised = true;
        }

        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if(window == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create GLFW window");

        // Définir le contexte AVANT createCapabilities()
        glfwMakeContextCurrent(window);

        // MAINTENANT on peut créer les capacités OpenGL
        createCapabilities();

        // Vérification que OpenGL fonctionne
        if (glGetString(GL_VERSION) == null) {
            throw new RuntimeException("Failed to initialize OpenGL context");
        }

        // Callbacks après avoir créé le contexte
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            setupOrthographicProjection();
        });

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
            if(key == GLFW_KEY_F11 && action == GLFW_RELEASE)
                toggleFullscreen();
        });

        // Reste du code...
        if(maximised){
            glfwMaximizeWindow(window);
        } else {
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidMode != null;
            glfwSetWindowPos(window, (vidMode.width() - width) / 2,
                    (vidMode.height() - height) / 2);
        }

        if(isvSync())
            glfwSwapInterval(1);

        glfwShowWindow(window);
        setupOpenGL2D();
    }

    private void toggleFullscreen() {
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = glfwGetVideoMode(monitor);

        if (!isFullscreen) {
            // Sauvegarder la position et taille actuelle
            int[] xPos = new int[1];
            int[] yPos = new int[1];
            glfwGetWindowPos(window, xPos, yPos);
            windowedPosX = xPos[0];
            windowedPosY = yPos[0];

            int[] w = new int[1];
            int[] h = new int[1];
            glfwGetWindowSize(window, w, h);
            windowedWidth = w[0];
            windowedHeight = h[0];

            // Passer en fullscreen
            assert vidMode != null;
            glfwSetWindowMonitor(window, monitor, 0, 0,
                    vidMode.width(), vidMode.height(),
                    vidMode.refreshRate());
            isFullscreen = true;
        } else {
            // Revenir en mode fenêtré
            glfwSetWindowMonitor(window, MemoryUtil.NULL,
                    windowedPosX, windowedPosY,
                    windowedWidth, windowedHeight, 0);
            isFullscreen = false;
        }

        // Réactiver vSync si nécessaire
        if(isvSync())
            glfwSwapInterval(1);
    }


    private void setupOpenGL2D() {
        // Désactiver le test de profondeur
        glDisable(GL_DEPTH_TEST);

        // Activer le blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Créer le shader
        shader = new Shader(VERTEX_SHADER, FRAGMENT_SHADER, true);

        // Configuration de la projection
        setupOrthographicProjection();

        // Couleur de fond
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    private void setupOrthographicProjection() {
        // Créer la matrice de projection orthographique
        Matrix4f projectionMatrix = Matrix4f.orthographic(0, width, height, 0, -1, 1);

        // Envoyer la matrice au shader
        shader.use();
        shader.setUniformMat4f("projection", projectionMatrix.getBuffer());
        shader.stop();

        // Mise à jour du viewport
        glViewport(0, 0, width, height);
    }

    public void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void cleanup(){
        if (shader != null) {
            shader.cleanup();
        }
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }

    public boolean isKeyPressed(int keycode){
        return glfwGetKey(window, keycode) == GLFW_PRESS;
    }

    public boolean windowShouldClose(){
        return glfwWindowShouldClose(window);
    }

    private boolean isvSync() {
        return vSync;
    }

}