package Engine;

import Entity.Camera;
import Render.*;

public class Renderer {

    private final BackgroundRenderer BackgroundRenderer;
    private final GroundRenderer GroundRenderer;
    private final RenderPlayer RenderPlayer;

    public Renderer() {
        BackgroundRenderer = new BackgroundRenderer();
        GroundRenderer = new GroundRenderer();
        RenderPlayer = new RenderPlayer();
    }

    public void initialize() {
        BackgroundRenderer.initialize();
        GroundRenderer.initialize();
        RenderPlayer.initialize();
    }

    public void renderFrame(Camera camera, float deltaTime){

        BackgroundRenderer.render(camera, deltaTime);
        GroundRenderer.render(camera, deltaTime);

        RenderPlayer.render(camera, deltaTime);
    }

    public void cleanUp() {
        BackgroundRenderer.cleanup();
        GroundRenderer.cleanup();
        RenderPlayer.cleanup();
    }
}
