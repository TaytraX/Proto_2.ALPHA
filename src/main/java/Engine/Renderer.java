package Engine;

import Entity.Camera;
import Entity.PlayerState;
import Render.*;

import java.util.List;

public class Renderer {

    private List<Renderable> renderers;

    private final BackgroundRenderer BackgroundRenderer;
    private final GroundRenderer GroundRenderer;
    private final RenderPlayer RenderPlayer;

    public Renderer(Window window) {
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
        PlayerState currentPlayerState = ThreadManager.playerState.get();

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
