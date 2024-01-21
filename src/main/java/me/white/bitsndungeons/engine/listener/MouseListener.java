package me.white.bitsndungeons.engine.listener;

import org.lwjgl.glfw.GLFW;

public class MouseListener {
    private static MouseListener instance = null;
    private double scrollX;
    private double scrollY;
    private double posX;
    private double posY;
    private double lastX;
    private double lastY;
    private boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        scrollX = 0;
        scrollY = 0;
        posX = 0;
        posY = 0;
        lastX = 0;
        lastY = 0;
    }

    public static MouseListener getInstance() {
        if (instance == null) {
            instance = new MouseListener();
        }
        return instance;
    }

    public static void cursorPosCallback(long windowId, double posX, double posY) {
        MouseListener instance = getInstance();
        instance.lastX = instance.posX;
        instance.lastY = instance.posY;
        instance.posX = posX;
        instance.posY = posY;
        for (boolean isPressed : instance.mouseButtonPressed) {
            if (isPressed) {
                instance.isDragging = true;
                break;
            }
        }
    }

    public static void mouseButtonCallback(long windowId, int button, int action, int modifiers) {
        MouseListener instance = getInstance();
        if (action == GLFW.GLFW_PRESS) {
            if (button < instance.mouseButtonPressed.length) {
                instance.mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW.GLFW_RELEASE) {
            if (button < instance.mouseButtonPressed.length) {
                instance.mouseButtonPressed[button] = false;
                instance.isDragging = false;
            }
        }
    }

    public static void scrollCallback(long windowId, double scrollX, double scrollY) {
        MouseListener instance = getInstance();
        instance.scrollX = scrollX;
        instance.scrollY = scrollY;
    }

    public static void endFrame(long windowId) {
        MouseListener instance = getInstance();
        instance.scrollX = 0;
        instance.scrollY = 0;
        instance.lastX = instance.posX;
        instance.lastY = instance.posY;
    }

    public static double getX() {
        return getInstance().posX;
    }

    public static double getY() {
        return getInstance().posY;
    }

    public static double getDx() {
        MouseListener instance = getInstance();
        return instance.lastX - instance.posX;
    }

    public static double getDy() {
        MouseListener instance = getInstance();
        return instance.lastY - instance.posY;
    }

    public static double getScrollX() {
        return getInstance().scrollX;
    }

    public static double getScrollY() {
        return getInstance().scrollY;
    }

    public static boolean isDragging() {
        return getInstance().isDragging;
    }

    public static boolean isMouseButtonPressed(int button) {
        MouseListener instance = getInstance();
        return button < instance.mouseButtonPressed.length && instance.mouseButtonPressed[button];
    }
}