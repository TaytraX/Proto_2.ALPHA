package Engine;

import Entity.Camera;

public interface Renderable {
    void initialize();
    void render(Camera camera, float deltaTime);
    void cleanup();
}