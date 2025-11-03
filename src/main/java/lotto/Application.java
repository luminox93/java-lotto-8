package lotto;

import lotto.engine.GameEngine;
import lotto.game.Game;
import lotto.game.classicLotto.LottoGame;

public class Application {
    public static void main(String[] args) {
        Game game = new LottoGame();
        GameEngine engine = new GameEngine(game);
        engine.run();
    }
}
