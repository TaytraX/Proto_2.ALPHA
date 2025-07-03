package Entity;

import Engine.AABB;
import org.joml.Vector2f;

public class Player {
    public static AABB position = new AABB(new Vector2f(0, 0), new Vector2f(1, 1));


    public static AABB getposition(){
        return position;
    }

    public void input() {
    }

    public void update(float deltaTime) {
    }

}