package Math;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

public class Matrix4f {
    public static final int SIZE = 4;
    private float[][] matrix;
    private FloatBuffer buffer = BufferUtils.createFloatBuffer(SIZE * SIZE);

    public Matrix4f() {
        matrix = new float[SIZE][SIZE];
        setIdentity();
    }

    public void setIdentity() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrix[i][j] = 0;
            }
        }
        matrix[0][0] = 1;
        matrix[1][1] = 1;
        matrix[2][2] = 1;
        matrix[3][3] = 1;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = new Matrix4f();

        result.matrix[0][0] = 2.0f / (right - left);
        result.matrix[1][1] = 2.0f / (top - bottom);
        result.matrix[2][2] = -2.0f / (far - near);
        result.matrix[3][0] = -(right + left) / (right - left);
        result.matrix[3][1] = -(top + bottom) / (top - bottom);
        result.matrix[3][2] = -(far + near) / (far - near);
        result.matrix[3][3] = 1.0f;

        return result;
    }

    public FloatBuffer getBuffer() {
        buffer.clear();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buffer.put(matrix[j][i]); // Transposée pour OpenGL
            }
        }
        buffer.flip();
        return buffer;
    }
}