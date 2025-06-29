package Render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {

    int textureID;
    int width, height;

    public Texture(String filename) {
        textureID = tryLoadTexture(filename);
        if (textureID == 0) {
            textureID = createDefaultTexture();
        }
    }

    private int tryLoadTexture(String filename) {
        String[] extensions = {".png", ".jpg", ".jpeg"};

        for (String ext : extensions) {
            try {
                String path = "src/main/Resources/textures/" + filename + ext;
                return loadTexture(path);
            } catch (Exception e) {
                // Continue avec l'extension suivante
            }
        }
        return 0; // Aucune texture chargée
    }


    private int loadTexture(String filename) throws Exception {
            ByteBuffer buffer;

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer comp = stack.mallocInt(1);

                buffer = STBImage.stbi_load(filename, w, h, comp, 4);
                if (buffer == null) {
                    throw new Exception("Could not load file " + filename + " " + STBImage.stbi_failure_reason());
                }

                this.width = w.get();
                this.height = h.get();
            }

            int textureID = GL11.glGenTextures();
            GL11.glBindTexture(GL_TEXTURE_2D, textureID);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            // Paramètres de filtrage
            GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            STBImage.stbi_image_free(buffer);
            return textureID;

    }

    public int createDefaultTexture() {
        // Texture 2x2 pixels blancs
        ByteBuffer data = org.lwjgl.BufferUtils.createByteBuffer(16);
        for (int i = 0; i < 16; i++) {
            data.put((byte) 255); // Blanc opaque
        }
        data.flip();

        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL_TEXTURE_2D, textureID);
        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA, 2, 2, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);

        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        this.width = 2;
        this.height = 2;

        return textureID;
    }

    public void cleanUp(){
            GL11.glDeleteTextures(textureID);
    }
}