import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp2 {
    private static final int SIZE = 4;
    private static final String SUCCESS = "success";
    private static int[][] board = new int[SIZE][SIZE];
    private static boolean[][] checkBoard = new boolean[SIZE][SIZE];
    private static boolean start = true;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8888);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            String serverMessage;
            int no;
            while (true) {
                serverMessage = in.readUTF();

                if (serverMessage.startsWith("Welcome") || serverMessage.contains("진행중")) {
                    System.out.println(serverMessage);
                }

                if (start) {
                    receiveBoard(in);
                    receiveMark(in);
                    printBingoBoard();
                    start = false;
                }

                if (serverMessage.startsWith("당신 차례입니다.")) {
                    while (true) {
                        System.out.print(serverMessage);
                        String input = scanner.nextLine();
                        try {
                            no = Integer.parseInt(input);
                            out.writeInt(no);
                            out.flush();

                            if (in.readBoolean()) {
                                receiveBoard(in);
                                receiveMark(in);
                                printBingoBoard();
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("숫자로 입력해주세요");
                        }
                    }
                } else if (serverMessage.contains("wins") || serverMessage.contains("무승부")) {
                    System.out.println(serverMessage);
                    break;
                } else if (serverMessage.contains("선택")) {
                    no = in.readInt();
                    System.out.print(serverMessage);
                    System.out.println(no);

                    receiveBoard(in);
                    receiveMark(in);
                    printBingoBoard();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveBoard(DataInputStream in) throws IOException {
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

    private static void receiveMark(DataInputStream in) {
        try {
            for (int col = 0; col < SIZE; col++) {
                for (int row = 0; row < SIZE; row++) {
                    checkBoard[col][row] = in.readBoolean();
                }
            }
        } catch (IOException e) {
            System.out.println("체크 보드 받는 중 에러");
        }
    }

    public static void printBingoBoard() {
        String redAnsi = "\033[31m";
        String endAnsi = "\033[0m";
        int size = board.length;

        printLine(size);

        for (int i = 0; i < size; i++) {
            System.out.print("|");
            for (int j = 0; j < size; j++) {
                if (checkBoard[i][j]) {
                    System.out.printf("%s %2d%s |", redAnsi, board[i][j], endAnsi);
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
