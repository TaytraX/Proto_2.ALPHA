package Engine;

import Render.Shader;
import Render.Texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Renderer {

    private final Shader backgroundShader = new Shader("background");
    private final Shader playerShader = new Shader("player");
    private final Shader platformShader = new Shader("platform");

    private final Texture playerTexture = new Texture("player");
    private final Texture platformTexture = new Texture("platform");

    public Renderer() {

    }

    public void renderBackground() {
        backgroundShader.use();
        backgroundShader.stop();
    }

    public void renderPlayer() {
        playerShader.use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, playerTexture.getTextureID());
        playerShader.setUniform1i("textureSample", 0);
        playerShader.stop();
    }

    public void renderPlatform() {
        platformShader.use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, platformTexture.getTextureID());
        platformShader.setUniform1i("texturePlatformSample", 0);
        platformShader.stop();
    }

}
