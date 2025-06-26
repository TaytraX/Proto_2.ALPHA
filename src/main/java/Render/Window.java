package Render;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL.*;

public class Window {

    private String title;

    private int width, height;
    private long window;

    private boolean resize;
    private final boolean vSync;

    private boolean isFullscreen = false;
    private int windowedWidth, windowedHeight;
    private int windowedPosX, windowedPosY;

    public Window(String title, int width, int height, boolean vSync) {
        this.vSync = vSync;
        this.height = height;
        this.width = width;
        this.title = title;
        this.windowedWidth = width;
        this.windowedHeight = height;
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
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

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResize(true);
            // Mise à jour de la projection orthographique lors du redimensionnement
            setupOrthographicProjection();
        });

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);

            // Toggle fullscreen avec F11
            if(key == GLFW_KEY_F11 && action == GLFW_RELEASE)
                toggleFullscreen();
        });

        if(maximised){
            glfwMaximizeWindow(window);
        }else{
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidMode != null;
            glfwSetWindowPos(window, (vidMode.width() - width) / 2,
                    (vidMode.height() - height) / 2);
        }

        glfwMakeContextCurrent(window);

        // Créer les capacités OpenGL - TRÈS IMPORTANT
        createCapabilities();

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
        // Désactiver le test de profondeur (pas nécessaire en 2D)
        glDisable(GL_DEPTH_TEST);

        // Activer le blending pour la transparence
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Désactiver le culling des faces (pas utile en 2D)
        glDisable(GL_CULL_FACE);

        // Configuration de la projection orthographique
        setupOrthographicProjection();

        // Couleur de fond
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    private void setupOrthographicProjection() {
        // Configurer la matrice de projection orthographique
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        // Projection orthographique : (0,0) en haut à gauche, (width, height) en bas à droite
        // Typique pour les jeux 2D comme Minecraft 2D
        glOrtho(0, width, height, 0, -1, 1);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

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
        glfwDestroyWindow(window);
    }

    public boolean isKeyPressed(int keycode){
        return glfwGetKey(window, keycode) == GLFW_PRESS;
    }

    public boolean windowShouldClose(){
        return glfwWindowShouldClose(window);
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
        glfwSetWindowTitle(window, newTitle);
    }

    private boolean isvSync() {
        return vSync;
    }

    public boolean isResize(){
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}