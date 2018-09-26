import java.util.*;

public class Player {
  private final int MAX_DEPTH = 2;
  private int hero = 0;
  private int villain = 0;
  private boolean isFirstTurn = true;

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
    this.hero = gameState.getNextPlayer();
    this.villain = this.hero == Constants.CELL_O ? Constants.CELL_X : Constants.CELL_O;

    if (this.hero == Constants.CELL_O) {
      System.err.println("Random value for player O");
      return states.elementAt(random.nextInt(length));
    }

    if (this.isFirstTurn && this.hero == Constants.CELL_X) {
      System.err.println("Is this happening??");
      this.isFirstTurn = false;
      return states.elementAt(random.nextInt(length));
    }

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
      int score = miniMaxPrune(state, this.hero, this.MAX_DEPTH, alpha, beta);
      // int score = miniMax(state, this.hero, this.MAX_DEPTH);
      if (score > bestScore) {
        bestScore = score;
        bestState = state;
      }
    }

    System.err.println("Best score is " + bestScore);
    return bestState;
  }

  private int miniMax(GameState gameState, int player, int depth) {
    if (gameState.isEOG() || depth == 0) {
      return estimate(gameState, player);
    }

    Vector<GameState> states = new Vector<GameState>();
    gameState.findPossibleMoves(states);

    if (player == this.hero) {
      int bestScore = Integer.MIN_VALUE;
      for (int i = 0; i < states.size(); i++) {
        int score = miniMax(states.elementAt(i), this.villain, depth - 1);
        if (score > bestScore) {
          bestScore = score;
        }
      }
      return bestScore;
    } else {
      int bestScore = Integer.MAX_VALUE;
      for (int i = 0; i < states.size(); i++) {
        int score = miniMax(states.elementAt(i), this.hero, depth - 1);
        if (score > bestScore) {
          bestScore = score;
        }
      }
      return bestScore;
    }
  }

  private int miniMaxPrune(GameState state, int player, int depth, int alpha, int beta) {
    if (alpha >= beta) {
      // System.err.println("Pruning at depth = " + depth);
      if (player == this.hero)
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
        currentScore = miniMaxPrune(states.elementAt(i), this.villain, depth - 1, alpha, beta);
        maxValue = Math.max(maxValue, currentScore);
        // Set alpha
        alpha = Math.max(currentScore, alpha);
      } else {
        currentScore = miniMaxPrune(states.elementAt(i), this.hero, depth - 1, alpha, beta);
        minValue = Math.min(minValue, currentScore);
        // Set beta
        beta = Math.min(currentScore, beta);
      }

      // If a pruning has been done, don't evaluate the rest of the sibling states
      if (currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE)
        break;
    }

    return player == this.hero ? maxValue : minValue;
  }

  private int estimate(GameState state, int player) {
    // Game is not over so max depth must be reached
    // if (!state.isEOG()) {
    return evaluateBoard(state, player);
    // }

    // if ((state.isXWin() && (this.hero == Constants.CELL_X)) || (state.isOWin() &&
    // (this.hero == Constants.CELL_O)))
    // return 1000;
    // else if ((state.isXWin() && (this.hero == Constants.CELL_O)) ||
    // (state.isOWin() && (this.hero == Constants.CELL_X)))
    // return -1000;
    // else
    // return 0;
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

  public int evaluateBoard2(GameState state, int player) {
    int mine = 0;
    int opponents = 0;
    int value = 0;

    for (int i = 0; i < 10; i++) {
      mine = opponents = 0;
      for (int j = 0; j < 4; j++) {
        int piece = state.at(wins[i][j]);
        if (piece == this.hero)
          mine++;
        else if (piece == this.villain)
          opponents++;
      }
      value += Heuristic_Array[mine][opponents];
    }
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
    for (int i = 3, j = 0; i > -1; i--, j++) {
      if (state.at(i, j) == Constants.CELL_X)
        X++;
      else if (state.at(i, j) == Constants.CELL_O)
        O++;
    }

    score += changeInScore(X, O);

    // return player == this.hero ? score :
    return score;
  }

  private int changeInScore(int X, int O) {
    int score;

    if (X == 4) {
      score = 1000;
    } else if (X == 3 && O == 0) {
      score = 100;
    } else if (X == 2 && O == 0) {
      score = 10;
    } else if (X == 1 && O == 0) {
      score = 1;
    } else if (O == 4) {
      score = -1000;
    } else if (O == 3 && X == 0) {
      score = -100;
    } else if (O == 2 && X == 0) {
      score = -10;
    } else if (O == 1 && X == 0) {
      score = -1;
    } else {
      score = 0;
    }

    return score;
  }

  int[][] wins =

      { { 0, 1, 2, 3 }, { 4, 5, 6, 7 }, { 8, 9, 10, 11 }, { 12, 13, 14, 15 }, { 0, 4, 8, 12 }, { 1, 5, 9, 13 },
          { 2, 6, 10, 14 }, { 3, 7, 11, 15 }, { 0, 5, 10, 15 }, { 3, 6, 9, 12 } };

  int[][] Heuristic_Array = { { 0, -10, -100, -1000, -10000 }, { 10, 0, 0, 0, 0 }, { 100, 0, 0, 0, 0 },
      { 1000, 0, 0, 0, 0 }, { 10000, 0, 0, 0, 0 } };
}
