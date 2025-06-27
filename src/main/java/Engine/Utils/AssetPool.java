package Engine.Utils;

import Render.Shader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static final Map<String, Shader> shaders = new HashMap<>();

    public static Shader getShader(String Fragment_Shader, String Vertex_Shader) {
        File file = new File(Fragment_Shader, Vertex_Shader);
        if(shaders.containsKey(file.getAbsolutePath())) {
            return shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(Fragment_Shader, Vertex_Shader);
            shader.compile(Fragment_Shader, Vertex_Shader);
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }
}