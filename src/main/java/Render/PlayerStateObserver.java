package Render;

import Entity.PlayerState;

public interface PlayerStateObserver {
    void onPlayerStateChanged(PlayerState newState);
}