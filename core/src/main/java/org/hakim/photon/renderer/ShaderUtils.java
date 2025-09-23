package org.hakim.photon.renderer;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils {

    public static int createVertexShader(URL sourceFile) {
        return createShader(GL_VERTEX_SHADER, sourceFile);
    }

    public static int createFragmentShader(URL sourceFile) {
        return createShader(GL_FRAGMENT_SHADER, sourceFile);
    }

    private static int createShader(int type, URL sourceFile) {
        // read file contents
        String shaderSource;
        try (InputStream input = sourceFile.openStream()) {
            shaderSource = IOUtils.toString(input, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error reading shader file " + sourceFile);
            return -1;
        }

        int shaderLocation = glCreateShader(type);
        glShaderSource(shaderLocation, shaderSource);
        glCompileShader(shaderLocation);

        // check for successful compilation
        int success = glGetShaderi(shaderLocation, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            String info = glGetShaderInfoLog(shaderLocation);
            System.err.println("Error compiling shader: " + info);
            return -1;
        }

        return shaderLocation;
    }
}
