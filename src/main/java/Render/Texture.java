package Render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {

    private final List<Integer> textures = new ArrayList<>();

    public Texture(String filename){
        int width, height;
        ByteBuffer pixels;

        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            ByteBuffer p = stack.malloc(4);

            pixels = stbi_load(filename, w, h, p.asIntBuffer(), 4);
            if(pixels == null)
                throw new RuntimeException("Failed to load texture");
            width = w.get(0);
            height = h.get(0);
            pixels = p.asReadOnlyBuffer();

        }

        int textureID = glGenTextures();
        textures.add(textureID);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);

        //paramètres de texture
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        STBImage.stbi_image_free(pixels);
    }

    public void cleanUp(){
        for(int texture : textures){
            GL11.glDeleteTextures(texture);
        }
    }
}