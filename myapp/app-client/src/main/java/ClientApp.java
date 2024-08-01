

import context.ApplicationContext;
import listener.ApplicationListener;
import listener.InitApplicationListener;
import util.Prompt;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientApp {
    private static final int SIZE = 4;
    private static final String SUCCESS = "success";
    private int[][] board = new int[SIZE][SIZE];
    private boolean[][] checkBoard = new boolean[SIZE][SIZE];

    public static void main(String[] args) {
        new ClientApp().init();
    }

    private void init(){
        Scanner scanner = new Scanner(System.in);
        try (Socket socket = new Socket("localhost", 8888);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            // 빙고 보드 수신
            receiveBoard(in, out);
            out.writeUTF(SUCCESS);
            out.flush();

            printBingoBoard();

            int bingoNo;
            String result;
            while (true) {
                System.out.print("빙고 번호를 입력하세요. > ");
                String no = scanner.nextLine();
                try {
                    bingoNo = Integer.parseInt(no);

                    out.writeInt(bingoNo);

                    receiveCheckBoard(in, out);
                    printBingoBoard();

                    result = in.readUTF();
                    if (resultChecking(out, result)) {
                        break;
                    }

                    System.out.printf("컴퓨터 > %d\n", in.readInt());
                    receiveCheckBoard(in, out);
                    printBingoBoard();

                    result = in.readUTF();
                    if (resultChecking(out, result)) {
                        break;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("숫자로 입력해주세요.");
                }
            }
        } catch (IOException e) {
            System.out.println("종료");
        }
    }

    private boolean resultChecking(DataOutputStream out, String result) {
        try {
            switch (result) {
                case "draw":
                    out.writeUTF("end");
                    System.out.println("무승부입니다");
                    return true;
                case "user win":
                    out.writeUTF("end");
                    System.out.println("유저가 승리했습니다");
                    return true;
                case "compute win":
                    out.writeUTF("end");
                    System.out.println("컴퓨터가 승리했습니다");
                    return true;
                default:
                    out.writeUTF(SUCCESS);
            }
        } catch (IOException e) {
            System.out.println("결과 체크 중 오류 발생");
        }
        return false;
    }

    private void receiveBoard(DataInputStream in, DataOutputStream out) throws IOException {
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

    private void receiveCheckBoard(DataInputStream in, DataOutputStream out) {
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

    public void printBingoBoard() {
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

    public void printLine(int size) {
        for (int i = 0; i < size; i++) {
            System.out.print("+----");
        }
        System.out.println("+");
    }
}
