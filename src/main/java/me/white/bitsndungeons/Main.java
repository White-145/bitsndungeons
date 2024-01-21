package me.white.bitsndungeons;

import me.white.bitsndungeons.engine.Window;
import me.white.bitsndungeons.scene.LevelScene;

public class Main {
    public static void main(String[] args) {
        Window window = Window.getInstance();
        window.setTitle("Bits&Dungeons");
        window.setScene(new LevelScene());
        window.run();
    }
}
