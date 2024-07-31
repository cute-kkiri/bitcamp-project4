package command;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Bingo {
  private final static int SIZE = 4; //
  boolean[][] bingoCheck = new boolean[SIZE][SIZE];

  int[][] bingoBoard = new int[SIZE][SIZE];

  public static void main(String[] args) {
    new Bingo().play();
  }

  public void play() {
    int count = 0;
    ArrayList<Integer> numbers = new ArrayList<>();
    for (int i = 1; i <= 25; i++) {
      numbers.add(i);
    }

    Collections.shuffle(numbers);

    int index = 0;
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        bingoBoard[i][j] = numbers.get(index);
        index++;
      }
    }

    printBingoBoard();

    Scanner scanner = new Scanner(System.in);
    while (!bingoChecker()) {
      System.out.print("빙고 숫자 입력 > ");
      int checkNo = scanner.nextInt();

      for (int col = 0; col < SIZE; col++) {
        for (int row = 0; row < SIZE; row++) {
          if (checkNo == bingoBoard[col][row]) {
            bingoCheck[col][row] = true;
          }
        }
      }

      printBingoBoard();

    }
  }

  private boolean bingoChecker() {
    // 총 빙고 카운트
    int userBingoCounter = 0;

    // 가로
    for (int col = 0; col < SIZE; col++) {
      boolean isUserRowBingo = true;
      for (int row = 0; row < SIZE; row++) {
        if (!bingoCheck[col][row]) {
          isUserRowBingo = false;
          break;
        }
      }
      if (isUserRowBingo) userBingoCounter++;
    }

    // 세로
    for (int row = 0; row < SIZE; row++) {
      boolean isUserColBingo = true;

      for (int col = 0; col < SIZE; col++) {
        if (!bingoCheck[col][row]) {
          isUserColBingo = false;
          break;
        }
      }
      if (isUserColBingo) userBingoCounter++;
    }

    boolean isUserDiagonal1 = true;
    boolean isUserDiagonal2 = true;
    for (int rc = 0; rc < SIZE; rc++) {
      if (!bingoCheck[rc][rc]) isUserDiagonal1 = false;
      if (!bingoCheck[rc][SIZE - 1 - rc]) isUserDiagonal2 = false;
    }

    if (isUserDiagonal1) userBingoCounter++;
    if (isUserDiagonal2) userBingoCounter++;

    return userBingoCounter >= 3;
  }

  public void printBingoBoard() {
    String redAnsi = "\033[31m";
    String endAnsi = "\033[0m";
    int size = bingoBoard.length;

    printLine(size);

    for (int i = 0; i < size; i++) {
      System.out.print("|");
      for (int j = 0; j < size; j++) {
        if(bingoCheck[i][j]){
          System.out.printf("%s %2d%s |", redAnsi, bingoBoard[i][j], endAnsi);
        }else {
          System.out.printf(" %2d |", bingoBoard[i][j]);
        }
      }
      System.out.println();
      printLine(size); // 각 행 사이의 테두리
    }
  }

  public static void printLine(int size) {
    for (int i = 0; i < size; i++) {
      System.out.print("+----");
    }
    System.out.println("+");
  }
}
