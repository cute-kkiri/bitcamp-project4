import util.Prompt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static util.Tui.*;

public class ClientApp2 {
    private static final int SIZE = 4;
    private static final String SUCCESS = "success";
    private static int[][] board = new int[SIZE][SIZE];
    private static boolean[][] checkBoard = new boolean[SIZE][SIZE];
    private static boolean start = true;

    public static void main(String[] args) {
        printLogo();

        System.out.println();
        new ClientApp2().init();
    }

    private void init(){
        try (Socket socket = new Socket("localhost", 8888);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            String serverMessage;
            int no;
            String name = Prompt.input("닉네임을 입력해주세요:");
            System.out.println();
            out.writeUTF(GREEN + BOLD + name + RESET);
            out.flush();
            while (true) {
                serverMessage = in.readUTF();

                if (serverMessage.startsWith("Welcome") || serverMessage.contains("진행중")) {
                    System.out.println(serverMessage);

                    if (serverMessage.contains("진행중")) {
                        System.out.println();
                    }
                }

                if (start) {
                    receiveBoard(in, board);
                    receiveMark(in, checkBoard);
                    printBingoBoard(board, checkBoard, GREEN);
                    System.out.println("잠시만 기다려주세요...");
                    System.out.println();
                    start = false;
                }

                if (serverMessage.startsWith("당신 차례입니다.")) {
                    while (true) {
                        System.out.println(GREEN + BOLD + name + RESET + "님의 차례입니다.");
                        String input = Prompt.input("번호를 입력해주세요>");
                        System.out.println();
                        try {
                            no = Integer.parseInt(input);
                            out.writeInt(no);
                            out.flush();

                            if(in.readBoolean()){
                                receiveBoard(in, board);
                                receiveMark(in, checkBoard);
                                printBingoBoard(board, checkBoard, GREEN);
                            } else {
                                System.out.println("이미 입력했던 번호입니다");
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("숫자로 입력해주세요");
                        }
                    }
                } else if (serverMessage.contains("wins") || serverMessage.contains("무승부")) {
                    endBoard(in);
                    System.out.println(serverMessage);
                    break;
                } else if(serverMessage.contains("선택")){
                    no = in.readInt();
                    System.out.printf("%s%d\n", serverMessage, no);
                    System.out.println();

                    receiveBoard(in, board);
                    receiveMark(in, checkBoard);
                    printBingoBoard(board, checkBoard, GREEN);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void endBoard(DataInputStream in) {
        int[][] otherBoard = new int[SIZE][SIZE];
        boolean[][] otherMark = new boolean[SIZE][SIZE];
        receiveBoard(in, otherBoard);
        receiveMark(in, otherMark);

        printEndBoard(otherBoard, otherMark);
    }

    private void printEndBoard(int[][] otherBoard, boolean[][] otherMark) {
        int size = board.length;

        StringBuilder stringBuilder = new StringBuilder();

        System.out.println();

        stringBuilder.append(String.format("%s내 빙고판%s                 %s상대 빙고판%s\n", GREEN, RESET, MAGENTA, RESET));

        stringBuilder.append(appendLine(size));

        for (int i = 0; i < size; i++) {
            stringBuilder.append("|");
            for (int j = 0; j < size; j++) {
                if (checkBoard[i][j]) {
                    stringBuilder.append(String.format("%s%s %2d%s |", GREEN, BOLD, board[i][j], RESET));
                }else {
                    stringBuilder.append(String.format(" %2d |", board[i][j]));
                }

            }
            stringBuilder.append("     |");
            for(int x = 0; x < size; x++){
                if (otherMark[i][x]) {
                    stringBuilder.append(String.format("%s%s %2d%s |", MAGENTA, BOLD, otherBoard[i][x], RESET));
                } else {
                    stringBuilder.append(String.format(" %2d |", otherBoard[i][x]));
                }
            }
            stringBuilder.append("\n");
            stringBuilder.append(appendLine(size));
        }

        System.out.println(stringBuilder);
    }

    private String appendLine(int size){
        StringBuilder  stringBuilder = new StringBuilder();

        String line1 = "+----";
        String line2 = "+";

        stringBuilder.append(line1.repeat(Math.max(0, size)));
        stringBuilder.append(line2);

        stringBuilder.append("     ");

        stringBuilder.append(line1.repeat(Math.max(0, size)));
        stringBuilder.append(line2);

        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private void receiveBoard(DataInputStream in, int[][] board) {
        try {
            for (int col = 0; col < SIZE; col++) {
                for (int row = 0; row < SIZE; row++) {
                    board[col][row] = in.readInt();
                }
            }
        } catch (IOException e) {
            System.out.println("보드 받는 중 에러");
        }
    }

    private void receiveMark(DataInputStream in, boolean[][] mark) {
        try {
            for (int col = 0; col < SIZE; col++) {
                for (int row = 0; row < SIZE; row++) {
                    mark[col][row] = in.readBoolean();
                }
            }
        } catch (IOException e) {
            System.out.println("체크 보드 받는 중 에러");
        }
    }

    public void printBingoBoard(int[][] board, boolean[][] mark, String color) {
        int size = board.length;

        printLine(size);

        for (int i = 0; i < size; i++) {
            System.out.print("|");
            for (int j = 0; j < size; j++) {
                if (mark[i][j]) {
                    System.out.printf("%s%s %2d%s |", color, BOLD, board[i][j], RESET);
                } else {
                    System.out.printf(" %2d |", board[i][j]);
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
