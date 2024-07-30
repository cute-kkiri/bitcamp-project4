package command;


import java.util.ArrayList;
import java.util.Collections;

public class Bingo {

  public static void main(String[] args) {
    int size = 4; //
    int[][] bingoBoard = new int[size][size];

    ArrayList<Integer> numbers = new ArrayList<>();
    for (int i = 1; i <= size * size; i++) {
      numbers.add(i);
    }

    Collections.shuffle(numbers);

    int index = 0;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        bingoBoard[i][j] = numbers.get(index);
        index++;
      }
    }

    printBingoBoard(bingoBoard);
  }

  public static void printBingoBoard(int[][] board) {
    int size = board.length;

    printLine(size);

    for (int i = 0; i < size; i++) {
      System.out.print("|");
      for (int j = 0; j < size; j++) {
        System.out.printf(" %2d |", board[i][j]);
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
