package me.white.bitsndungeons.engine.component;

public class FontRenderer extends Component {
    @Override
    public void update(double dt) {

    }

    @Override
    public void start() {
        if (entity.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found sprite renderer!");
        }
    }
}
