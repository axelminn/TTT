import java.util.*;

public class Player {
  private final int MAX_DEPTH = 2;
  private final int PLAYER_MAX = Constants.CELL_X;
  private final int PLAYER_MIN = Constants.CELL_O;
  private int hero = 0;
  private int villain = 0;

  /**
   * Performs a move
   *
   * @param gameState the current state of the board
   * @param deadline  time before which we must have returned
   * @return the next state the board is in after our move
   */
  public GameState play(final GameState gameState, final Deadline deadline) {
    Vector<GameState> states = new Vector<GameState>();
    gameState.findPossibleMoves(states);
    int length = states.size();

    // No more moves
    if (length == 0) {
      // Must play "pass" move if there are no other moves possible.
      return new GameState(gameState, new Move());
    } else if (length == 1) {
      // Only one move so nothing to do
      return states.elementAt(0);
    }

    Random random = new Random();
    int index = random.nextInt(length);

    // O is random
    // int player = gameState.getNextPlayer() == Constants.CELL_X ? Constants.CELL_X
    // : Constants.CELL_O;
    // System.err.println("Player is " + player);
    this.villain = gameState.getNextPlayer();
    this.hero = this.villain = Constants.CELL_O ? Constants.CELL_X : Constants.CELL_O;

    // if (player == Constants.CELL_O) {
    // System.err.println("Random value for player O");
    // return states.elementAt(random.nextInt(length));
    // }

    /**
     * Here you should write your algorithms to get the best next move, i.e. the
     * best next state. This skeleton returns a random move instead.
     */

    GameState bestState = new GameState();
    int bestScore = 0;
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;
    // int player = this.PLAYER_MAX;

    for (GameState state : states) {
      int score = miniMaxPrune(state, this.PLAYER_MAX, this.MAX_DEPTH, alpha, beta);
      if (score > bestScore) {
        bestScore = score;
        bestState = state;
      }
    }

    System.err.println("Best score is " + bestScore);
    return bestState;

    // String player = gameState.getNextPlayer() == Constants.CELL_X ? "O is player"
    // : "X is player";
    // System.err.println(player);

    // return states.elementAt(0);
  }

  private int miniMaxPrune(GameState state, int player, int depth, int alpha, int beta) {
    if (alpha >= beta) {
      // System.err.println("Pruning at depth = " + depth);
      if (player == this.PLAYER_MAX)
        return Integer.MAX_VALUE;
      else
        return Integer.MIN_VALUE;
    }

    if (depth == 0 || state.isEOG())
      return estimate(state, player);

    Vector<GameState> states = new Vector<GameState>();
    state.findPossibleMoves(states);

    int maxValue = Integer.MIN_VALUE;
    int minValue = Integer.MAX_VALUE;

    for (int i = 0; i < states.size(); i++) {
      int currentScore = 0;

      if (player == this.hero) {
        currentScore = miniMaxPrune(state, this.villain, depth - 1, alpha, beta);
        maxValue = Math.max(maxValue, currentScore);
        // Set alpha
        alpha = Math.max(currentScore, alpha);
      } else {
        currentScore = miniMaxPrune(state, this.hero, depth - 1, alpha, beta);
        minValue = Math.min(minValue, currentScore);
        // Set beta
        beta = Math.min(currentScore, beta);
      }

      // If a pruning has been done, don't evaluate the rest of the sibling states
      if (currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE)
        break;
    }

    return player == this.PLAYER_MAX ? maxValue : minValue;
  }

  private int estimate(GameState state, int player) {
    boolean isPlayerX = state.getNextPlayer() == Constants.CELL_O ? true : false;

    // Game is not over so max depth must be reached
    if (!state.isEOG()) {
      // return evaluateSimple(state, isPlayerX ? Constants.CELL_X :
      // Constants.CELL_O);
      return evaluateBoard(state, player);
    }

    if ((state.isXWin() && isPlayerX) || (state.isOWin() && !isPlayerX))
      return 1000;
    else if ((state.isXWin() && !isPlayerX) || (state.isOWin() && isPlayerX))
      return -1000;
    else
      return 0;
  }

  private int evaluateSimple(GameState state, int playerIdx) {
    int value = 0;
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (state.at(i, j) == playerIdx)
          value = value + 2;
      }
    }

    if (state.at(0, 0) == playerIdx)
      value++;
    if (state.at(1, 1) == playerIdx)
      value++;
    if (state.at(2, 2) == playerIdx)
      value++;
    if (state.at(3, 3) == playerIdx)
      value++;

    if (state.at(3, 0) == playerIdx)
      value++;
    if (state.at(2, 1) == playerIdx)
      value++;
    if (state.at(1, 2) == playerIdx)
      value++;
    if (state.at(0, 3) == playerIdx)
      value++;

    return value;
  }

  public int evaluateBoard(GameState state, int player) {
    int score = 0;
    int X = 0;
    int O = 0;

    // Check all rows
    for (int i = 0; i < 4; i++) {
      X = 0;
      O = 0;
      for (int j = 0; j < 4; j++) {
        if (state.at(i, j) == Constants.CELL_X)
          X++;
        else if (state.at(i, j) == Constants.CELL_O)
          O++;
      }
      score += changeInScore(X, O);
    }

    // Check all columns
    for (int i = 0; i < 4; i++) {
      X = 0;
      O = 0;
      for (int j = 0; j < 4; j++) {
        if (state.at(j, i) == Constants.CELL_X)
          X++;
        else if (state.at(j, i) == Constants.CELL_O)
          O++;
      }
      score += changeInScore(X, O);
    }

    X = 0;
    O = 0;
    // Check diagonal (first)
    for (int i = 0, j = 0; i < 4; i++, ++j) {
      if (state.at(i, j) == Constants.CELL_X)
        X++;
      else if (state.at(i, j) == Constants.CELL_O)
        O++;
    }

    score += changeInScore(X, O);

    X = 0;
    O = 0;
    // Check Diagonal (Second)
    for (int i = 2, j = 0; i > -1; --i, ++j) {
      if (state.at(i, j) == Constants.CELL_X)
        X++;
      else if (state.at(i, j) == Constants.CELL_O)
        O++;
    }

    score += changeInScore(X, O);

    // return (player == 1) ? -score : score;
    return score;
  }

  private int changeInScore(int X, int O) {
    int change;
    if (X == 4) {
      change = 1000;
    } else if (X == 3 && O == 0) {
      change = 100;
    } else if (X == 2 && O == 0) {
      change = 10;
    } else if (X == 1 && O == 0) {
      change = 1;
    } else if (O == 4) {
      change = -1000;
    } else if (O == 3 && X == 0) {
      change = -100;
    } else if (O == 2 && X == 0) {
      change = -10;
    } else if (O == 1 && X == 0) {
      change = -1;
    } else {
      change = 0;
    }
    return change;
  }
}
