package Engine;

import Entity.Camera;
import Entity.PlayerState;
import Render.*;

import java.util.List;

public class Renderer {

    private List<Renderable> renderers;

    private final BackgroundRenderer BackgroundRenderer;
    private final GroundRenderer PlatformRenderer;
    private final RenderPlayer RenderPlayer;

    public Renderer(Window window) {
        BackgroundRenderer = new BackgroundRenderer();
        PlatformRenderer = new GroundRenderer();
        RenderPlayer = new RenderPlayer();
    }

    public void initialize() {
        BackgroundRenderer.initialize();
        PlatformRenderer.initialize();
        RenderPlayer.initialize();
    }

    public void renderFrame(Camera camera, float deltaTime){
        PlayerState currentPlayerState = ThreadManager.playerState.get();

        BackgroundRenderer.render(camera, deltaTime);
        PlatformRenderer.render(camera, deltaTime);

        RenderPlayer.render(camera, deltaTime);
    }

    public void cleanUp() {
        BackgroundRenderer.cleanup();
        PlatformRenderer.cleanup();
        RenderPlayer.cleanup();
    }
}
