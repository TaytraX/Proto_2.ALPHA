package Engine;

import disable.BackgroundRenderer;
import disable.Entity.Camera;
import Laucher.Main;
import Render.*;
import disable.PlatformRenderer;

import java.util.List;

public class Renderer {

    private List<Renderable> renderers;

    public Window window;
    private final BackgroundRenderer BackgroundRenderer;
    private final PlatformRenderer PlatformRenderer;
    private final RenderPlayer RenderPlayer;

    public Renderer() {
        window = Main.getWindow();
        BackgroundRenderer = new BackgroundRenderer();
        PlatformRenderer = new PlatformRenderer();
        RenderPlayer = new RenderPlayer();
    }

    public void initialize() {
        BackgroundRenderer.initialize();
        PlatformRenderer.initialize();
        RenderPlayer.initialize();
    }

    public void renderFrame(Camera camera, float deltaTime){
        BackgroundRenderer.render(camera, deltaTime);
        PlatformRenderer.render(camera, deltaTime);
        RenderPlayer.render(camera, deltaTime);
    }

    public void cleanUp() {
    }
}
