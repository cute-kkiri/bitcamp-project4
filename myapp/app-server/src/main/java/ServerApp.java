


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerApp {
    private static final int PORT = 8888;
    private static final int SIZE = 4;
    private static final int MAX_NUM = 25;
    private int[][] board1 = new int[SIZE][SIZE];
    private int[][] board2 = new int[SIZE][SIZE];
    private boolean[][] marked1 = new boolean[SIZE][SIZE];
    private boolean[][] marked2 = new boolean[SIZE][SIZE];
    private static final Set<Integer> pickedNumbers = new HashSet<>();
    private Map<Integer, Integer> saveNumbers = new HashMap<>();
    private Map<Integer, String> saveNames = new HashMap<>();
    private static int currentPlayer = 1;
    private boolean gameEnd = false;
    private boolean complete = true;
    boolean player1Bingo;
    boolean player2Bingo;

    public static void main(String[] args) {
        new ServerApp().init();
    }

    private void init() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("서버 대기중...");

            board1 = initializeBoard();
            board2 = initializeBoard();

            while (true) {
                Socket clientSocket1 = serverSocket.accept();
                Socket clientSocket2 = serverSocket.accept();

                new Thread(new ClientHandler(clientSocket1, 1)).start();
                new Thread(new ClientHandler(clientSocket2, 2)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[][] initializeBoard() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= MAX_NUM; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        int counter = 0;
        int[][] board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = numbers.get(counter++);
            }
        }
        return board;
    }

    private synchronized boolean markNumber(int number, int player) {
        if (pickedNumbers.contains(number)) {
            return false;
        }

        pickedNumbers.add(number);
        System.out.println(saveNumbers.size() + ": " + number);
        saveNumbers.put(saveNumbers.size(), number);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board1[i][j] == number) {
                    marked1[i][j] = true;
                }

                if (board2[i][j] == number) {
                    marked2[i][j] = true;
                }
            }
        }

        currentPlayer = player == 1 ? 2 : 1;
        return true;
    }

    private synchronized boolean checkBingo(boolean[][] marked) {
        int bingoCounter = 0;

        for (int i = 0; i < SIZE; i++) {
            boolean rowBingo = true;
            for (int j = 0; j < SIZE; j++) {
                if (!marked[i][j]) {
                    rowBingo = false;
                    break;
                }
            }
            if (rowBingo) bingoCounter++;
        }

        for (int j = 0; j < SIZE; j++) {
            boolean columnBingo = true;
            for (int i = 0; i < SIZE; i++) {
                if (!marked[i][j]) {
                    columnBingo = false;
                    break;
                }
            }
            if (columnBingo) bingoCounter++;
        }

        boolean diagonal1 = true, diagonal2 = true;
        for (int i = 0; i < SIZE; i++) {
            if (!marked[i][i]) diagonal1 = false;
            if (!marked[i][SIZE - 1 - i]) diagonal2 = false;
        }

        if (diagonal1) bingoCounter++;
        if (diagonal2) bingoCounter++;

        return bingoCounter >= 3;
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private int player;
        String name;
        private final Object lock = new Object();

        public ClientHandler(Socket socket, int player) {
            this.socket = socket;
            this.player = player;
        }

        @Override
        public void run() {
            int cursor = 0;
            try (DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                name = in.readUTF();

                saveNames.put(player, name);

                out.writeUTF("Welcome, " + name);


                out.flush();
                sendBoard(out, player == 1 ? board1 : board2);
                sendMark(out, player == 1 ? marked1 : marked2);

                while (!gameEnd) {

                    if (cursor != saveNumbers.size()) {
                        out.writeUTF(saveNames.get(player == 1 ? 2 : 1) + "님이 선택한 번호 ");
                        out.writeInt(saveNumbers.get(cursor++));

                        out.flush();
                        sendBoard(out, player == 1 ? board1 : board2);
                        sendMark(out, player == 1 ? marked1 : marked2);
                        gameEnd = isEnd(out);
                    }
                    if (currentPlayer == player) {

                        if (gameEnd) {
                            sendBoard(out, player == 1 ? board2 : board1);
                            sendMark(out, player == 1 ? marked2 : marked1);
                            break;
                        }


                        out.writeUTF("당신 차례입니다.");
                        out.flush();
                        int number = in.readInt();
                        out.flush();
                        if (markNumber(number, player)) {
                            out.writeBoolean(true);
                            out.flush();

                            sendBoard(out, player == 1 ? board1 : board2);
                            sendMark(out, player == 1 ? marked1 : marked2);


                            player1Bingo = checkBingo(marked1);
                            player2Bingo = checkBingo(marked2);
                            gameEnd = isEnd(out);

                            if (gameEnd) {
                                sendBoard(out, player == 1 ? board2 : board1);
                                sendMark(out, player == 1 ? marked2 : marked1);
                                break;
                            }

                            cursor++;

                            out.writeUTF(saveNames.get(player == 1 ? 2 : 1) + "님이 진행중...");
                            out.flush();
                        } else {
                            out.writeBoolean(false);
                            out.writeUTF("이미 입력했던 번호입니다 기다려주세요");
                            out.flush();
                        }
                    } else {
                        Thread.yield();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private boolean isEnd(DataOutputStream out) {
            try {
                if (player1Bingo & player2Bingo) {
                    out.writeUTF("무승부입니다");
                    out.flush();
                    return true;
                } else if (player1Bingo) {
                    out.writeUTF("BINGO! " + saveNames.get(1) + " wins!");
                    out.flush();
                    return true;
                } else if (player2Bingo) {
                    out.writeUTF("BINGO! " + saveNames.get(2) + " wins!");
                    out.flush();
                    return true;
                }
            } catch (IOException e) {
                System.out.println("게임 결과 전송 중 에러 발생");
            }
            return false;
        }

        private void sendBoard(DataOutputStream out, int[][] board) {
            try {
                for (int col = 0; col < SIZE; col++) {
                    for (int row = 0; row < SIZE; row++) {
                        out.writeInt(board[col][row]);
                        out.flush();
                    }
                }
            } catch (IOException e) {
                System.out.println("보드 전송 중 에러");
            }
        }

        private void sendMark(DataOutputStream out, boolean[][] mark) {
            try {
                for (int col = 0; col < SIZE; col++) {
                    for (int row = 0; row < SIZE; row++) {
                        out.writeBoolean(mark[col][row]);
                        out.flush();
                    }
                }
            } catch (IOException e) {
                System.out.println("보드 전송 중 에러");
            }
        }
    }
}
