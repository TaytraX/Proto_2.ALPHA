package Engine;

import Entity.Camera;
import Laucher.Main;
import Render.*;

import java.util.List;

public class Renderer {

    private List<Renderable> renderers;

    public Window window;
    private BackgroundRenderer BackgroundRenderer;
    private PlatformRenderer PlatformRenderer;
    private RenderPlayer RenderPlayer;

    public Renderer() {
        window = Main.getWindow();
        BackgroundRenderer = new BackgroundRenderer();
        PlatformRenderer = new PlatformRenderer();
        RenderPlayer = new RenderPlayer();
    }

    public void renderFrame(Camera camera, float deltaTime){
        BackgroundRenderer.render(camera, deltaTime);
        PlatformRenderer.render(camera, deltaTime);
        RenderPlayer.render(camera, deltaTime);
    }

    public void cleanUp() {
    }
}
