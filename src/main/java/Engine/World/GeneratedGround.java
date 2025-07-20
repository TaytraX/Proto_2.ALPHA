package Engine.World;

import java.util.Random;

public class GeneratedGround {

    private static Noise noise;
    private static Random seed;

    public GeneratedGround() {
        seed = new Random();
        noise = new Noise(seed.nextInt());
    }

    public void init() {

    }

}