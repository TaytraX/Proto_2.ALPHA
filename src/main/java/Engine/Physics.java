package Engine;

import Entity.PlayerState;

import java.util.List;

public class Physics {

    public static final float gravity = -9.81f;
    public static final float airFriction = 0.001f;
    public static final float groundFriction = 0.5f;
    private PlayerState currentState;

    List<AABB> platforms; // List of platforms in the game.

    public Physics() {

    }

    public void update(float deltaTime) {

    }



}