package me.white.bitsndungeons.engine.listener;

import org.lwjgl.glfw.GLFW;

public class KeyListener {
    private static KeyListener instance = null;
    private boolean[] keyPressed = new boolean[350];

    private KeyListener() { }

    public static KeyListener getInstance() {
        if (instance == null) {
            instance = new KeyListener();
        }
        return instance;
    }

    public static void keyCallback(long window, int key, int scanCode, int action, int mods) {
        KeyListener instance = getInstance();
        if (action == GLFW.GLFW_PRESS) {
            instance.keyPressed[key] = true;
        } else if (action == GLFW.GLFW_RELEASE) {
            instance.keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int key) {
        KeyListener instance = getInstance();
        return key < instance.keyPressed.length && instance.keyPressed[key];
    }
}
