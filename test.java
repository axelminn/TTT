
//*******************************************************************
// Welcome to CompileJava!
// If you experience any issues, please contact us ('More Info')  -->
// Also, sorry that the "Paste" feature no longer works! GitHub broke
// this (so we'll switch to a new provider): https://blog.github.com\
// /2018-02-18-deprecation-notice-removing-anonymous-gist-creation/
//*******************************************************************

import java.lang.Math; // headers MUST be above the first class

// one class needs to have a main() method
public class test {
  // arguments are passed using the text field below this editor
  public static void main(String[] args) {
    int output = evaluateBoard(the_state);
    System.out.println(output);
  }

  public static int[][] the_state = { { 1, 0, 0, 0 }, { 0, 1, 0, 2 }, { 0, 0, 0, 2 }, { 0, 0, 0, 0 } };

  public static int evaluateBoard(int[][] state) {
    int score = 0;
    int X = 0;
    int O = 0;

    // // Check all rows
    // for (int i = 0; i < 4; i++) {
    // for (int j = 0; j < 4; j++) {
    // if (state[i][j] == 1)
    // X++;
    // else if (state[i][j] == 2)
    // O++;
    // }
    // score += changeInScore(X, O);
    // }

    // Check all rows
    for (int i = 0; i < 4; ++i) {
      X = 0;
      O = 0;
      for (int j = 0; j < 4; ++j) {
        if (state[i][j] == 1)
          X++;
        else if (state[i][j] == 2)
          O++;
      }
      score += changeInScore(X, O);
      System.out.println("Row " + i + " got score " + score);
    }

    System.out.println("After rows score is " + score);

    // Check all columns
    for (int i = 0; i < 4; i++) {
      X = 0;
      O = 0;
      for (int j = 0; j < 4; j++) {
        if (state[j][i] == 1)
          X++;
        else if (state[j][i] == 2)
          O++;
      }
      score += changeInScore(X, O);
      System.out.println("Col " + i + " got score " + score);
    }

    System.out.println("After cols score is " + score);

    X = 0;
    O = 0;
    // Check diagonal (first)
    for (int i = 0, j = 0; i < 4; i++, ++j) {
      if (state[i][j] == 1)
        X++;
      else if (state[i][j] == 2)
        O++;
    }

    score += changeInScore(X, O);
    System.out.println("After first diag score is " + score);

    X = 0;
    O = 0;
    // Check Diagonal (Second)
    for (int i = 2, j = 0; i > -1; --i, ++j) {
      if (state[i][j] == 1)
        X++;
      else if (state[i][j] == 2)
        O++;
    }

    score += changeInScore(X, O);
    System.out.println("After second diog score is " + score);

    // return (player == 1) ? -score : score;
    return score;
  }

  private static int changeInScore(int X, int O) {
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
