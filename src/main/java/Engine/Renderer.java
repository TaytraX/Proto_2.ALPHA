package Engine;

import Laucher.Main;
import Render.Shader;
import Render.Texture;
import Render.Window;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Renderer {

    public Shader shader;
    public Window window;
    public Texture texture;

    public Renderer(Shader shader) {
        this.shader = shader;
        window = Main.getWindow();
    }

    public Renderer(Shader shader, Texture texture) {
        window = Main.getWindow();
        this.shader = shader;
        this.texture = texture;
    }

    public void render() {
        shader.use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform1i("texturePlatformSample", 0);
        shader.stop();
    }

    public void cleanUp() {
        shader.cleanup();
        texture.cleanUp();
    }

}
