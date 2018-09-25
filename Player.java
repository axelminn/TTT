import java.util.*;

public class Player {
    private final int MAX_DEPTH = 2;
    int me = 0;
    int opponent = 0;

    /**
     * Performs a move
     *
     * @param gameState the current state of the board
     * @param deadline  time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        // Get player
        this.opponent = gameState.getNextPlayer();
        this.me = this.opponent ^ (Constants.CELL_X | Constants.CELL_O);

        System.err.println("WHOOSSS TURNNN??? " + this.opponent + " : " + this.me);

        Vector<GameState> states = new Vector<GameState>();
        gameState.findPossibleMoves(states);

        // No more moves
        if (states.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        } else if (states.size() == 1) {
            // Only one move so nothing to do
            return states.elementAt(0);
        }

        /**
         * Here you should write your algorithms to get the best next move, i.e. the
         * best next state. This skeleton returns a random move instead.
         */

        double maxScore = 0.0;
        Random random = new Random();
        int index = random.nextInt(states.size());

        for (int i = 0; i < states.size(); i++) {
            // int score = miniMax(states.elementAt(i), this.me, this.MAX_DEPTH);
            int score = miniMax(gameState, this.me, this.MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > maxScore) {
                maxScore = score;
                index = i;
            }
        }

        System.err.println("Best score is " + maxScore + " with index " + index);

        return states.elementAt(index);
    }

    private int miniMax(GameState gameState, int player, int depth) {
        if (gameState.isEOG() || depth == 0) {
            return getScore(gameState);
        }

        Vector<GameState> states = new Vector<GameState>();
        gameState.findPossibleMoves(states);

        if (player == this.me) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < states.size(); i++) {
                int score = miniMax(states.elementAt(i), this.opponent, depth - 1);
                if (score > bestScore) {
                    bestScore = score;
                }
                return bestScore;
            }
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < states.size(); i++) {
                int score = miniMax(states.elementAt(i), this.me, depth - 1);
                if (score > bestScore) {
                    bestScore = score;
                }
                return bestScore;
            }
        }

        return 0;
    }

    private int miniMax(GameState gameState, int player, int depth, int alpha, int beta) {
        if (gameState.isEOG() || depth == 0)
            return getScore(gameState);

        Vector<GameState> states = new Vector<GameState>();
        gameState.findPossibleMoves(states);

        for (int i = 0; i < states.size(); i++) {
            if (player == this.me) {
                int score = miniMax(states.elementAt(i), this.opponent, depth - 1, alpha, beta);
                if (score > alpha)
                    alpha = score;
            } else {
                int score = miniMax(states.elementAt(i), this.me, depth - 1);
                if (score < beta)
                    beta = score;
            }

            // Pruning
            if (alpha >= beta)
                break;
        }

        return (player == this.me) ? alpha : beta;
    }

    private int getScore(GameState gameState) {
        if ((gameState.isXWin() && this.me == Constants.CELL_X)
                || (gameState.isOWin() && this.me == Constants.CELL_O)) {
            System.err.println("X won ( ͡° ͜ʖ ͡°)");
            // If "we" won +10
            return 100;
        } else if ((gameState.isXWin() && this.opponent == Constants.CELL_X)
                || (gameState.isOWin() && this.opponent == Constants.CELL_O)) {
            System.err.println("O won (ಠ◞౪◟ಠ)");
            // if "they" won -10
            return -100;
        } else {
            System.err.println("Only a draw ಠ_ಠ");
            // else 0
            return 0;
        }
    }
}

class BestMove {
    int score;
    int index;

    public BestMove(int score, int index) {
        this.score = score;
        this.index = index;
    }
}
