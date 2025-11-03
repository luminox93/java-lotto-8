package lotto.engine;

import lotto.game.Game;

public class GameEngine {
    private final Game game;

    public GameEngine(Game game) {
        this.game = game;
    }

    public void run() {
        game.play();
    }
}
