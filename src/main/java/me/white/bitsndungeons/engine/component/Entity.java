package me.white.bitsndungeons.engine.component;

import me.white.bitsndungeons.engine.render.Transform;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private List<Component> components = new ArrayList<>();
    public Transform transform = new Transform();

    public Entity() { }

    public Entity(Transform transform) {
        this.transform = transform;
    }

    public <T extends Component> T getComponent(Class<T> clazz) {
        for (Component component : components) {
            if (clazz.isAssignableFrom(component.getClass())) {
                try {
                    return clazz.cast(component);
                } catch (ClassCastException e) {
                    throw new IllegalStateException("Error casting component: " + e);
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> clazz) {
        for (int i = 0; i < components.size(); ++i) {
            if (clazz.isAssignableFrom(components.get(i).getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component) {
        if (component.entity != null) {
            throw new IllegalStateException("Component cannot have multiple entities");
        }
        component.entity = this;
        components.add(component);
    }

    public void update(double dt) {
        for (Component component : components) {
            component.update(dt);
        }
    }

    public void start() {
        for (Component component : components) {
            component.start();
        }
    }
}
