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
    private long windowId;
    private Scene scene = null;

    private static Window instance = null;

    private Window() {
        width = 780;
        height = 430;
        title = "Window";
        isRunning = false;
    }

    public static Window getInstance() {
        if (instance == null) {
            instance = new Window();
        }
        return instance;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        if (isRunning()) {
            scene.init();
            scene.start();
        }
    }

    public void setTitle(String title) {
        this.title = title;
        if (isRunning) {
            GLFW.glfwSetWindowTitle(windowId, title);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void run() {
        if (isRunning) {
            throw new IllegalStateException("Cannot run window twice.");
        }
        System.out.println("Hello, LWJGL " + Version.getVersion() + "!");

        init();
        isRunning = true;
        if (scene != null) {
            scene.init();
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

        windowId = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowId == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        GLFW.glfwSetCursorPosCallback(windowId, MouseListener::cursorPosCallback);
        GLFW.glfwSetMouseButtonCallback(windowId, MouseListener::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(windowId, MouseListener::scrollCallback);
        GLFW.glfwSetKeyCallback(windowId, KeyListener::keyCallback);

        GLFW.glfwMakeContextCurrent(windowId);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowId);

        GL.createCapabilities();
    }

    private void loop() {
        double beginTime = Time.getTime();
        double dt = 0.0;
        while (!GLFW.glfwWindowShouldClose(windowId)) {
            GLFW.glfwPollEvents();

            GL30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            GL30.glClear(GL30.GL_COLOR_BUFFER_BIT);

            if (scene != null) {
                scene.update(dt);
            }

            GLFW.glfwSwapBuffers(windowId);

            MouseListener.endFrame(windowId);

            double endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    private void terminate() {
        Callbacks.glfwFreeCallbacks(windowId);
        GLFW.glfwDestroyWindow(windowId);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
