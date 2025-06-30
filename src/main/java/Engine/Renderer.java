package Engine;

import Engine.World.PlatformGenRequest;
import Entity.Camera;
import Laucher.Main;
import Render.Shader;
import Render.Texture;
import Render.Window;
import Entity.Player;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Renderer {

    private final Shader backgroundShader = new Shader("background");
    private final Shader playerShader = new Shader("player");
    private final Shader platformShader = new Shader("platform");

    private final Texture playerTexture = new Texture("player");
    private final Texture platformTexture = new Texture("platform");
    private int backgroundVAO, backgroundVBO;
    private int playerVAO, playerVBO;
    private int platformVAO, platformVBO;

    public Window window;

    public Renderer() {
        window = Main.getWindow();
    }

    public void renderBackground(float time, float width, float height) {
        backgroundShader.use();
        backgroundShader.stop();
    }

    public void renderPlayer(Player player, Camera camera) {
        playerShader.use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, playerTexture.getTextureID());
        playerShader.setUniform1i("textureSample", 0);
        playerShader.stop();
    }

    public void renderPlatform(List<PlatformGenRequest> platforms, Camera camera) {
        platformShader.use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, platformTexture.getTextureID());
        platformShader.setUniform1i("texturePlatformSample", 0);
        platformShader.stop();
    }

    public void cleanUp() {
        backgroundShader.cleanup();
        playerShader.cleanup();
        platformShader.cleanup();
        playerTexture.cleanUp();
        platformTexture.cleanUp();
    }

}
