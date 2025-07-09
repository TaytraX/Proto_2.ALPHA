package Engine;

import Entity.PlayerState;

public class StateValidator {
    public static boolean validatePlayerState(PlayerState state) {
        if (state == null) {
            GameLogger.error("PlayerState est null", null);
            return false;
        }

        if (state.position() == null) {
            GameLogger.error("Position du joueur est null", null);
            return false;
        }

        if (state.velocity() == null) {
            GameLogger.error("Vélocité du joueur est null", null);
            return false;
        }

        // Vérifications de cohérence
        if (Float.isNaN(state.position().x) || Float.isNaN(state.position().y)) {
            GameLogger.error("Position contient NaN: " + state.position(), null);
            return false;
        }

        return true;
    }
}