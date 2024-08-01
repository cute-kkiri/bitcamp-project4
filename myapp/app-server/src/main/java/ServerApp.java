
import context.ApplicationContext;
import dao.skel.UserDaoSkel;
import listener.ApplicationListener;
import listener.InitApplicationListener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ServerApp {
    private static final int SIZE = 4; // 빙고 보드 크기
    private int[][] board1;
    private int[][] board2;
    private boolean[][] checkBoard1 = new boolean[SIZE][SIZE];
    private boolean[][] checkBoard2 = new boolean[SIZE][SIZE];

    public static void main(String[] args) {
        new ServerApp().init();
    }

    private void init(){
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("서버 대기중...");

            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                System.out.println("Player 1 들어옴.");
                board1 = generateBingoBoard();
                sendBoard(out, board1);

                board2 = generateBingoBoard();
                int no;
                boolean userIsBingo;
                boolean computeIsBingo;
                while (!in.readUTF().equals("end")) {
                    no = in.readInt();
                    System.out.println(no);

                    checkedNumber(no, board1, checkBoard1);
                    checkedNumber(no, board2, checkBoard2);
                    sendCheckBoard(out, checkBoard1);

                    userIsBingo = isBingo(out, checkBoard1);
                    computeIsBingo = isBingo(out, checkBoard2);
                    result(out, userIsBingo, computeIsBingo);

                    if(in.readUTF().equals("end")){
                        break;
                    }

                    Thread.sleep(1000);
                    Random random = new Random();
                    no = random.nextInt(25) + 1;
                    out.writeInt(no);

                    checkedNumber(no, board1, checkBoard1);
                    checkedNumber(no, board2, checkBoard2);
                    sendCheckBoard(out, checkBoard1);

                    userIsBingo = isBingo(out, checkBoard1);
                    computeIsBingo = isBingo(out, checkBoard2);
                    result(out, userIsBingo, computeIsBingo);
                }
            } catch (InterruptedException e) {
                System.out.println("컴퓨터 차례 대기 중 오류 발생");
            }
        } catch (IOException e) {
            System.out.println("에러 발생");
            e.printStackTrace();
        }
    }

    private void result(DataOutputStream out, boolean userIsBingo, boolean computeIsBingo){
        try {
            if(userIsBingo) {
                if (computeIsBingo) {
                    out.writeUTF("draw");
                }
                out.writeUTF("user win");
            }else if(computeIsBingo){
                out.writeUTF("compute win");
            }
            out.writeUTF("no result");
        }catch (IOException e){
            System.out.println("판정 결과 전송 중 오류 발생");
        }
    }

    private boolean isBingo(DataOutputStream out, boolean[][] checkBoard) {
        // 총 빙고 카운트
        int userBingoCounter = 0;

        // 가로
        for (int col = 0; col < SIZE; col++) {
            boolean isUserRowBingo = true;
            for (int row = 0; row < SIZE; row++) {
                if (!checkBoard[col][row]) {
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
                if (!checkBoard[col][row]) {
                    isUserColBingo = false;
                    break;
                }
            }
            if (isUserColBingo) userBingoCounter++;
        }
        // 대각선
        boolean isUserDiagonal1 = true;
        boolean isUserDiagonal2 = true;
        for (int rc = 0; rc < SIZE; rc++) {
            if (!checkBoard[rc][rc]) isUserDiagonal1 = false;
            if (!checkBoard[rc][SIZE - 1 - rc]) isUserDiagonal2 = false;
        }

        if (isUserDiagonal1) userBingoCounter++;
        if (isUserDiagonal2) userBingoCounter++;

        return (userBingoCounter >= 3);
    }

    private void sendCheckBoard(DataOutputStream out, boolean[][] checkBoard) {
        try {
            for (int col = 0; col < SIZE; col++) {
                for (int row = 0; row < SIZE; row++) {
                    out.writeBoolean(checkBoard[col][row]);
                }
            }
        } catch (IOException e) {
            System.out.println("보드 전송 중 에러");
        }
    }

    private void checkedNumber(int no, int[][] board, boolean[][] checkBoard) {
        for (int col = 0; col < SIZE; col++) {
            for (int row = 0; row < SIZE; row++) {
                if (no == board[col][row]) {
                    checkBoard[col][row] = true;
                }
            }
        }
    }

    private int[][] generateBingoBoard() {
        int[][] board = new int[SIZE][SIZE];
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        int index = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = numbers.get(index);
                index++;
            }
        }
        return board;
    }

    private void sendBoard(DataOutputStream out, int[][] board) {
        try {
            for (int col = 0; col < SIZE; col++) {
                for (int row = 0; row < SIZE; row++) {
                    out.writeInt(board[col][row]);
                }
            }
        } catch (IOException e) {
            System.out.println("보드 전송 중 에러");
        }
    }
}
