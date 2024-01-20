package me.white.bitsndungeons;

import me.white.bitsndungeons.engine.Window;
import me.white.bitsndungeons.scene.LevelScene;

public class Main {
    public static void main(String[] args) {
        Window window = Window.getInstance();
        window.changeScene(new LevelScene());
        window.run();
    }
}
