// import java.util.*;

// public class Player {
// private final int MAX_DEPTH = 3;
// int me = 0;
// int opponent = 0;
// boolean f = true;

// /**
// * Performs a move
// *
// * @param gameState the current state of the board
// * @param deadline time before which we must have returned
// * @return the next state the board is in after our move
// */
// public GameState play(final GameState gameState, final Deadline deadline) {
// // Get player
// // this.opponent = gameState.getNextPlayer();
// // this.me = this.opponent ^ (Constants.CELL_X | Constants.CELL_O);

// System.err.println("WHOOSSS TURNNN??? " + this.opponent + " : " + this.me);

// Vector<GameState> states = new Vector<GameState>();
// gameState.findPossibleMoves(states);

// // No more moves
// // if (states.size() == 0) {
// // // Must play "pass" move if there are no other moves possible.
// // return new GameState(gameState, new Move());
// // } else if (states.size() == 1) {
// // // Only one move so nothing to do
// // return states.elementAt(0);
// // }

// // O is almost random
// if (this.opponent == 2 && this.f) {
// this.f = false;
// return states.elementAt(0);
// }
// if (this.opponent == 2) {
// Random random = new Random();
// return states.elementAt(random.nextInt(states.size()));
// }

// /**
// * Here you should write your algorithms to get the best next move, i.e. the
// * best next state. This skeleton returns a random move instead.
// */

// double maxScore = 0.0;
// int index = 0;

// int alpha = Integer.MIN_VALUE;
// int beta = Integer.MAX_VALUE;
// // int alpha = -432143;
// // int beta = 543215;

// for (int i = 0; i < states.size(); i++) {
// int score = miniMax(states.elementAt(i), 1, this.MAX_DEPTH);
// // int score = miniMax(gameState, 1, this.MAX_DEPTH, alpha, beta);
// if (score > maxScore) {
// maxScore = score;
// index = i;
// }
// }

// System.err.println("Best score is " + maxScore + " with index " + index);

// return states.elementAt(index);
// }

// private int miniMax(GameState gameState, int player, int depth) {
// if (gameState.isEOG() || depth == 0) {
// return evaluate(gameState, player);
// }

// Vector<GameState> states = new Vector<GameState>();
// gameState.findPossibleMoves(states);

// if (player == 1) {
// int bestScore = Integer.MIN_VALUE;
// for (int i = 0; i < states.size(); i++) {
// int score = miniMax(states.elementAt(i), 2, depth - 1);
// if (score > bestScore) {
// bestScore = score;
// }
// }
// return bestScore;
// } else {
// int bestScore = Integer.MAX_VALUE;
// for (int i = 0; i < states.size(); i++) {
// int score = miniMax(states.elementAt(i), 1, depth - 1);
// if (score > bestScore) {
// bestScore = score;
// }
// }
// return bestScore;
// }
// }

// private int miniMax(GameState gameState, int player, int depth, int alpha,
// int beta) {
// if (gameState.isEOG() || depth == 0)
// return evaluate(gameState, player);

// Vector<GameState> states = new Vector<GameState>();
// gameState.findPossibleMoves(states);

// int maxValue = Integer.MIN_VALUE;
// int minValue = Integer.MAX_VALUE;

// for (int i = 0; i < states.size(); i++) {
// int score = 0;
// if (player == 1) {
// score = miniMax(states.elementAt(i), 2, depth - 1, alpha, beta);
// maxValue = Math.max(maxValue, score);
// alpha = Math.max(score, alpha);

// // score = miniMax(states.elementAt(i), 2, depth - 1, alpha, beta);
// // alpha = Math.max(alpha, score);
// // if (score > alpha)
// // alpha = score;
// } else {
// score = miniMax(states.elementAt(i), 1, depth - 1, alpha, beta);
// minValue = Math.min(minValue, score);
// alpha = Math.min(score, alpha);

// // score = miniMax(states.elementAt(i), 1, depth - 1, alpha, beta);
// // beta = Math.min(beta, score);
// // if (score < beta)
// // beta = score;
// }

// // Pruning
// // if (alpha >= beta) {
// // break;
// if (score == Integer.MAX_VALUE || score == Integer.MIN_VALUE) {
// // System.err.println("PRUNING!");
// break;
// }
// }

// return (player != 1) ? maxValue : minValue;
// }

// private int evaluate(GameState gameState, int player) {
// // if (gameState.isXWin() && player == Constants.CELL_X) {
// // System.err.println("X won ( ͡° ͜ʖ ͡°)");
// // // If "we" won +10
// // return 100000;
// // } else if (gameState.isOWin() && player == Constants.CELL_O) {
// // System.err.println("O won (ಠ◞౪◟ಠ)");
// // // if "they" won -10
// // return -100000;
// // } else {
// // System.err.println("Only a draw ಠ_ಠ");
// // // else 0
// // return 0;
// // }
// // System.err.println(
// // "Getting score with Player = " + player + " Me = " + this.me + " and
// Opponent
// // = " + this.opponent);

// // if (!gameState.isEOG())
// // return evaluateSimple(gameState, player);

// if (player == 1) {
// // System.err.println("Estimating for me");
// if (gameState.isXWin()) {
// System.err.println("X WON THE GAME");
// return 100;
// } else if (gameState.isOWin()) {
// System.err.println("O WON THE GAME");
// return -100;
// } else {
// return 0;
// }
// } else {
// if (gameState.isXWin()) {
// return -100;
// } else if (gameState.isOWin()) {
// return 100;
// } else {
// return 0;
// }
// }

// // if ((gameState.isXWin() && this.me == Constants.CELL_X)
// // || (gameState.isOWin() && this.me == Constants.CELL_O)) {
// // // System.err.println("X won ( ͡° ͜ʖ ͡°)");
// // // If "we" won +10
// // return 100;
// // } else if ((gameState.isXWin() && this.opponent == Constants.CELL_X)
// // || (gameState.isOWin() && this.opponent == Constants.CELL_O)) {
// // // System.err.println("O won (ಠ◞౪◟ಠ)");
// // // if "they" won -10
// // return -100;
// // } else {
// // // System.err.println("Only a draw ಠ_ಠ");
// // // else 0
// // return 0;
// // }
// }

// public int evaluateBoard(GameState state, int player) {
// int score = 0;

// // Check all rows
// for (int i = 0; i < 4; ++i) {
// int X = 0;
// int O = 0;
// for (int j = 0; j < 4; ++j) {
// if (state.at(i, j) == Constants.CELL_X)
// X++;
// else if (state.at(i, j) == Constants.CELL_O)
// O++;
// }
// score += changeInScore(X, O);
// }

// // Check all columns
// for (int j = 0; j < 4; ++j) {
// int blank = 0;
// int X = 0;
// int O = 0;
// for (int i = 0; i < 4; ++i) {
// if (state.at(i, j) == Constants.CELL_X)
// X++;
// else if (state.at(i, j) == Constants.CELL_O)
// O++;
// }
// score += changeInScore(X, O);
// }

// int X = 0;
// int O = 0;

// // Check diagonal (first)
// for (int i = 0, j = 0; i < 4; ++i, ++j) {
// if (state.at(i, j) == Constants.CELL_X)
// X++;
// else if (state.at(i, j) == Constants.CELL_O)
// O++;
// }

// score += changeInScore(X, O);

// X = 0;
// O = 0;

// // Check Diagonal (Second)
// for (int i = 2, j = 0; i > -1; --i, ++j) {
// if (state.at(i, j) == Constants.CELL_X)
// X++;
// else if (state.at(i, j) == Constants.CELL_O)
// O++;
// }

// score += changeInScore(X, O);

// return (player == 1) ? -score : score;
// }

// private int changeInScore(int X, int O) {
// int change;
// if (X == 4) {
// change = 1000;
// } else if (X == 3 && O == 0) {
// change = 100;
// } else if (X == 2 && O == 0) {
// change = 10;
// } else if (X == 1 && O == 0) {
// change = 1;
// } else if (O == 4) {
// change = -1000;
// } else if (O == 3 && X == 0) {
// change = -100;
// } else if (O == 2 && X == 0) {
// change = -10;
// } else if (O == 1 && X == 0) {
// change = -1;
// } else {
// change = 0;
// }
// return change;
// }

// private int evaluateSimple(GameState state, int playerIdx) {
// int value = 0;
// for (int r = 0; r < 4; r++) {
// for (int c = 0; c < 4; c++) {
// if (state.at(r, c) == playerIdx)
// value = value + 2;
// }
// }

// if (state.at(0, 0) == playerIdx)
// value++;
// if (state.at(1, 1) == playerIdx)
// value++;
// if (state.at(2, 2) == playerIdx)
// value++;
// if (state.at(3, 3) == playerIdx)
// value++;

// if (state.at(3, 0) == playerIdx)
// value++;
// if (state.at(2, 1) == playerIdx)
// value++;
// if (state.at(1, 2) == playerIdx)
// value++;
// if (state.at(0, 3) == playerIdx)
// value++;

// return value;

// }
// }
