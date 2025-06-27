package Render;

import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public Shader(String vertexPath, String fragmentPath) {
        try {
            String vertexSource = new String(Files.readAllBytes(Paths.get(vertexPath)));
            String fragmentSource = new String(Files.readAllBytes(Paths.get(fragmentPath)));

            compile(vertexSource, fragmentSource);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des shaders: " + e.getMessage());
        }
    }

    public Shader(String vertexSource, String fragmentSource, boolean fromString) {
        if (fromString) {
            compile(vertexSource, fragmentSource);
        }
    }

    public void compile(String vertexSource, String fragmentSource) {
        // Compiler le vertex shader
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexSource);
        glCompileShader(vertexShaderID);

        if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Erreur vertex shader: " + glGetShaderInfoLog(vertexShaderID));
            return;
        }

        // Compiler le fragment shader
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentSource);
        glCompileShader(fragmentShaderID);

        if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Erreur fragment shader: " + glGetShaderInfoLog(fragmentShaderID));
            return;
        }

        // Créer le programme
        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        glLinkProgram(programID);

        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Erreur linking: " + glGetProgramInfoLog(programID));
            return;
        }

        glValidateProgram(programID);
        if (glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println("Erreur validation: " + glGetProgramInfoLog(programID));
        }
    }

    public void use() {
        glUseProgram(programID);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void setUniformMat4f(String name, FloatBuffer matrix) {
        int location = glGetUniformLocation(programID, name);
        glUniformMatrix4fv(location, false, matrix);
    }

    public void setUniform1i(String name, int value) {
        int location = glGetUniformLocation(programID, name);
        glUniform1i(location, value);
    }

    public void setUniform1f(String name, float value) {
        int location = glGetUniformLocation(programID, name);
        glUniform1f(location, value);
    }

    public void cleanup() {
        stop();
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }
}