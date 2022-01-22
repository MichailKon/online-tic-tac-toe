package ru.MichailKon.webGame;

import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    private int playerId;
    private int otherPlayer;
    private ClientSideConnection csc;
    TicTacToe game;

    public Client() {
        game = new TicTacToe();
    }

    public void connectToServer() {
        csc = new ClientSideConnection();
    }

    public void updateGame() {
        Pair<Integer, Integer> pos = csc.receiveQuery();
        int row = pos.getFirst(), col = pos.getSecond();

        TicTacToe.MoveResult move = game.makeMove(row, col);
        System.out.println("Player " + otherPlayer + " sent " + row + " " + col);
        if (move == TicTacToe.MoveResult.ILLEGAL_MOVE) {
            throw new IllegalStateException("Got illegal move for " + row + " " + col);
        }
        if (move == TicTacToe.MoveResult.SUCCESS) {
            System.out.println("Now it's your turn. Here's field:");
            game.printField();
        } else {
            System.out.println("Another player won. You can try again! (just relaunch server)");
        }
    }

    private class ClientSideConnection {
        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;

        public ClientSideConnection() {
            System.out.println("---Client---");
            try {
                socket = new Socket("0.0.0.0", 51734);
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
                playerId = dataIn.readInt();
                System.out.println("Connected to server as Player #" + playerId);
            } catch (IOException e) {
                System.out.println("IOException from CSC constructor");
                System.exit(-1);
            }
        }

        public void sendPos(int row, int col) {
            try {
                System.out.println("Sent move " + row + " " + col);
                System.out.println("Current field:");
                game.printField();
                dataOut.writeInt(row);
                dataOut.writeInt(col);
                dataOut.flush();
            } catch (IOException e) {
                System.out.println("IOException from sendPos() in CSC");
                System.exit(-1);
            }
        }

        public Pair<Integer, Integer> receiveQuery() {
            System.out.println("Got signal");
            int row = -1, col = -1;
            try {
                row = dataIn.readInt();
                col = dataIn.readInt();
            } catch (IOException e) {
                System.out.println("Error in receiveQuery() in CSC");
                System.exit(-1);
            }
            return new Pair<>(row, col);
        }
    }

    public static void main(String[] args) {
        Client c = new Client();
        c.connectToServer();
//        c.startReceivingSignals();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        c.game.printField();
        if (c.playerId == 1) {
            System.out.print("Make your move: ");
        } else {
            System.out.println("Wait for the first player.");
        }

        Thread t = new Thread(() -> {
            while (true) {
                c.updateGame();
            }
        });
        t.start();

        try {
            while ((line = in.readLine()) != null) {
                Scanner scanner = new Scanner(line);
                int row, col;
                try {
                    row = scanner.nextInt();
                    col = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Incorrect input!");
                    continue;
                }
                if (c.game.getNowPlayer() != c.playerId) {
                    System.out.println("Another player's move!");
                    continue;
                }
                TicTacToe.MoveResult move = c.game.makeMove(row, col);
                if (move == TicTacToe.MoveResult.ILLEGAL_MOVE) {
                    System.out.println("Illegal move. Try again");
                    continue;
                }
                c.csc.sendPos(row, col);
                if (move == TicTacToe.MoveResult.FIRST_WIN) {
                    System.out.println("Nice you win!");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException in main() in Client");
            System.exit(-1);
        }
    }
}
