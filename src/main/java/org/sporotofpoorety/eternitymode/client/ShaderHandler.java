package org.sporotofpoorety.eternitymode.client;


import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;

import org.sporotofpoorety.eternitymode.Tags;




public final class ShaderHandler 
{

    public static int WILLOWISP;

    private static final Map<Integer, Map<String, Integer>> UNIFORM_LOCATIONS = new HashMap<>();

    private static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
    private static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;

    private static final long START_NANOS = System.nanoTime();

    private ShaderHandler() 
    {
    }


    public static void registerShaders() 
    {
        WILLOWISP = createShader("willowisp");
    }


    public static int createShader(String name) 
    {
        final String path = "/assets/" + Tags.MOD_ID + "/shaders/" + name + ".frag";

        int fragment;
        try 
        {
            fragment = compile(path, FRAG);
        }
        catch (final Exception e) 
        {
            System.err.println("[ShaderHandler] Could not compile " + path);
            e.printStackTrace();
            return 0;
        }

        final int program = ARBShaderObjects.glCreateProgramObjectARB();
        if (program == 0) 
        {
            ARBShaderObjects.glDeleteObjectARB(fragment);
            return 0;
        }

        ARBShaderObjects.glAttachObjectARB(program, fragment);
        ARBShaderObjects.glLinkProgramARB(program);

        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) 
        {
            System.err.println("[ShaderHandler] Link failed for " + path + "\n" + getLogInfo(program));
            ARBShaderObjects.glDeleteObjectARB(program);
            ARBShaderObjects.glDeleteObjectARB(fragment);
            return 0;
        }

        ARBShaderObjects.glValidateProgramARB(program);

        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) 
        {
            System.err.println("[ShaderHandler] Validation warning for " + path + "\n" + getLogInfo(program));
        }

        ARBShaderObjects.glDetachObjectARB(program, fragment);
        ARBShaderObjects.glDeleteObjectARB(fragment);

        UNIFORM_LOCATIONS.put(program, new HashMap<>());
        return program;
    }

    private static int compile(String path, int type) throws Exception {

        final int shader = ARBShaderObjects.glCreateShaderObjectARB(type);

        if (shader == 0) 
        {
            throw new IllegalStateException("Driver refused to create a shader object");
        }

        try (InputStream in = ShaderHandler.class.getResourceAsStream(path)) 
        {
            if (in == null) 
            {
                throw new IllegalArgumentException("Missing shader resource: " + path);
            }

            ARBShaderObjects.glShaderSourceARB(shader, IOUtils.toString(in, StandardCharsets.UTF_8));
        }

        ARBShaderObjects.glCompileShaderARB(shader);

        if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) 
        {
            final String log = getLogInfo(shader);
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw new RuntimeException(log);
        }

        return shader;
    }


    public static boolean useShaders() 
    {
        return OpenGlHelper.shadersSupported;
    }


    public static void begin(int program) 
    {
        if (!useShaders() || program == 0) 
        {
            return;
        }

        ARBShaderObjects.glUseProgramObjectARB(program);

        setFloat(program, "time", getTime());
        setVec3i(program, "resolution", Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, 0);
    }

    public static void end() 
    {

        if (!useShaders()) 
        {
            return;
        }

        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    public static void setFloat(int program, String name, float value) 
    {

        final int location = location(program, name);

        if (location != -1) 
        {
            ARBShaderObjects.glUniform1fARB(location, value);
        }
    }

    public static void setInt(int program, String name, int value) 
    {
        final int location = location(program, name);

        if (location != -1) 
        {
            ARBShaderObjects.glUniform1iARB(location, value);
        }
    }

    public static void setVec3f(int program, String name, float x, float y, float z) 
    {

        final int location = location(program, name);

        if (location != -1) 
        {
            ARBShaderObjects.glUniform3fARB(location, x, y, z);
        }
    }

    public static void setVec3i(int program, String name, int x, int y, int z) 
    {

        final int location = location(program, name);

        if (location != -1) 
        {
            ARBShaderObjects.glUniform3iARB(location, x, y, z);
        }
    }

    public static float getTime() 
    {
        return (System.nanoTime() - START_NANOS) / 1_000_000_000.0f;
    }

    private static int location(int program, String name) 
    {

        final Map<String, Integer> cache = UNIFORM_LOCATIONS.computeIfAbsent(program, key -> new HashMap<>());
        final Integer cached = cache.get(name);

        if (cached != null) 
        {
            return cached;
        }

        final int location = ARBShaderObjects.glGetUniformLocationARB(program, name);
        cache.put(name, location);
        return location;
    }

    private static String getLogInfo(int object) 
    {

        return ARBShaderObjects.glGetInfoLogARB(object, ARBShaderObjects.glGetObjectParameteriARB(object, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
}
