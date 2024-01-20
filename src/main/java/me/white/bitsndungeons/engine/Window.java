package me.white.bitsndungeons.engine;

import me.white.bitsndungeons.engine.listener.KeyListener;
import me.white.bitsndungeons.engine.listener.MouseListener;
import me.white.bitsndungeons.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Window {
    private int width;
    private int height;
    private String title;
    private boolean isRunning;
    private long glfwWindow;

    private static Window instance = null;
    private static Scene currentScene = null;

    private Window() {
        width = 780;
        height = 430;
        title = "Bits&Dungeons";
        isRunning = false;
    }

    public static Window getInstance() {
        if (instance == null) {
            instance = new Window();
        }
        return instance;
    }

    public void changeScene(Scene scene) {
        currentScene = scene;
        if (isRunning()) {
            scene.init();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void run() {
        System.out.println("Hello, LWJGL " + Version.getVersion() + "!");

        init();
        isRunning = true;
        if (currentScene != null) {
            currentScene.init();
        }
        loop();
        terminate();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        glfwWindow = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (glfwWindow == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        GLFW.glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        GLFW.glfwMakeContextCurrent(glfwWindow);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    private void loop() {
        double beginTime = Time.getTime();
        double dt = 0.0;
        while (!GLFW.glfwWindowShouldClose(glfwWindow)) {
            GLFW.glfwPollEvents();

            GL30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            GL30.glClear(GL30.GL_COLOR_BUFFER_BIT);

            if (currentScene != null) {
                currentScene.update(dt);
            }

            GLFW.glfwSwapBuffers(glfwWindow);

            double endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    private void terminate() {
        Callbacks.glfwFreeCallbacks(glfwWindow);
        GLFW.glfwDestroyWindow(glfwWindow);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
