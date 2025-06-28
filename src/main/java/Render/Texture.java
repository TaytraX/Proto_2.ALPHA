package Render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {

    int textureID;
    int width, height;
    ByteBuffer pixels;
    private final List<Integer> textures = new ArrayList<>();

    public Texture(String filename) throws Exception {
        try {
        String texturePath = "src/main/resources/textures/" + filename + ".pnj";
        textureID = loadTexture(texturePath);
    } catch (IOException e) {
        e.printStackTrace();
    }

        try {
            String texturePath = "src/main/resources/textures/" + filename + ".jpg";
            textureID = loadTexture(texturePath);
    } catch (IOException e) {
            e.printStackTrace();
    }
    }


    private int loadTexture(String filename) throws Exception {

            int width, height;
            ByteBuffer buffer;

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer comp = stack.mallocInt(1);

                buffer = STBImage.stbi_load(filename, w, h, comp, 4);
                if (buffer == null) {
                    throw new Exception("Could not load file " + filename + " " + STBImage.stbi_failure_reason());
                }

                width = w.get();
                height = h.get();
            }

            int textureID = GL11.glGenTextures();
            textures.add(textureID);
            GL11.glBindTexture(GL_TEXTURE_2D, textureID);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            // Paramètres de filtrage
            GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            STBImage.stbi_image_free(buffer);
            return textureID;

    }

    public synchronized int createDefaultTexture() {
        // Texture 2x2 pixels blancs
        ByteBuffer data = org.lwjgl.BufferUtils.createByteBuffer(16);
        for (int i = 0; i < 16; i++) {
            data.put((byte) 255); // Blanc opaque
        }
        data.flip();

        int textureID = GL11.glGenTextures();
        textures.add(textureID);
        GL11.glBindTexture(GL_TEXTURE_2D, textureID);
        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA, 2, 2, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);

        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        return textureID;
    }

    public void cleanUp(){
        GL11.glDeleteTextures(textureID);
    }
}