/*
 * The MIT License (MIT)
 *
 * Copyright © 2014-2016, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import Map.*;

/**
 * This class is a simple quick starting guide. This is mainly a java conversion
 * of the
 * <a href=http://www.glfw.org/docs/latest/quick.html>Getting started guide</a>
 * from the official GLFW3 homepage.
 *
 * @author Heiko Brumme
 */
public class Introduction {

    public static int WIDTH = 640;
    public static int HEIGHT = 480;
    /**
     * This error callback will simply print the error to
     * <code>System.err</code>.
     */
    private static GLFWErrorCallback errorCallback
            = GLFWErrorCallback.createPrint(System.err);
    private static long window;
    private static IntBuffer width;
    private static IntBuffer height;
    private static Map map;
    private static GLFWWindowSizeCallback windowSizeCallback;
    private static float aspectRatio;

    /**
     * The main function will create a 640x480 window and renders a rotating
     * triangle until the window gets closed.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        init();

        gameElementsInit();

        loop();
        close();

    }

    private static void gameElementsInit() {
        map = new Map();
    }

    private static void close() {
        /* Free buffers */
        MemoryUtil.memFree(width);
        MemoryUtil.memFree(height);

        /* Release window and its callbacks */
        glfwDestroyWindow(window);

        /* Terminate GLFW and release the error callback */
        glfwTerminate();
        errorCallback.free();
    }

    private static void loop() {
        /* Loop until window gets closed */
        while (!glfwWindowShouldClose(window)) {

            /* Get width and height to calcualte the ratio */
            glfwGetFramebufferSize(window, width, height);
            aspectRatio = width.get() / (float) height.get();

            /* Rewind buffers for next get */
            width.rewind();
            height.rewind();

            /* Set viewport and clear screen */
            glViewport(0, 0, width.get(), height.get());
            // Clear the screen and depth buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            //Set Background color
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);


            /* Rewind buffers for next get */
            width.rewind();
            height.rewind();
            glfwGetFramebufferSize(window, width, height);
            map.draw(width.get(),height.get());




            /* Swap buffers and poll Events */
            glfwSwapBuffers(window);
            glfwPollEvents();

            /* Flip buffers for next loop */
            width.flip();
            height.flip();
        }
    }

    private static void init() {
        /* Set the error callback */
        glfwSetErrorCallback(errorCallback);

        /* Initialize GLFW */
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        /* Create window */
        window = glfwCreateWindow(WIDTH, HEIGHT, "Lord's Domain", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        /* Center the window on screen */
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window,
                (vidMode.width() - WIDTH) / 2,
                (vidMode.height() - HEIGHT) / 2
        );


        glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback(){
            @Override
            public void invoke(long window, int width, int height){
                Introduction.WIDTH = width;
                Introduction.HEIGHT = height;
                Introduction.aspectRatio = (float)Introduction.WIDTH / (float)Introduction.HEIGHT;
                glViewport(0, 0, Introduction.WIDTH, Introduction.HEIGHT);
            }
        });

        /* Create OpenGL context */
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        /* Enable vertical synchronization */
        glfwSwapInterval(1);

        /* Declare buffers for using inside the loop */
        width = MemoryUtil.memAllocInt(1);
        height = MemoryUtil.memAllocInt(1);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 0, 600, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

}