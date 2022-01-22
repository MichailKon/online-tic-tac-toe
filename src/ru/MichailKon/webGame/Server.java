package ru.MichailKon.webGame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    int numPlayers;
    static ServerSocket serverSocket;
    private ServerSideConnection player1, player2;
    TicTacToe game;

    int player1Row, player1Col;
    int player2Row, player2Col;

    public Server() {
        System.out.println("---TicTacToe Server---");
        game = new TicTacToe();
        numPlayers = 0;
        try {
            serverSocket = new ServerSocket(51734);
        } catch (IOException ex) {
            System.out.println("IOException from Server constructor");
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            while (numPlayers < 2) {
                Socket s = serverSocket.accept();
                numPlayers++;
                System.out.println("Player #" + numPlayers + " has connected");
                ServerSideConnection ssc = new ServerSideConnection(s, numPlayers);
                if (numPlayers == 1) {
                    player1 = ssc;
                } else {
                    player2 = ssc;
                }
                Thread t = new Thread(ssc);
                t.start();
            }
            System.out.println("Found 2 players");
        } catch (IOException ex) {
            System.out.println("IOException from acceptConnections");
        }
    }

    private class ServerSideConnection implements Runnable {
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        private final int playerId;

        public ServerSideConnection(Socket s, int id) {
            playerId = id;
            try {
                dataIn = new DataInputStream(s.getInputStream());
                dataOut = new DataOutputStream(s.getOutputStream());
            } catch (IOException e) {
                System.out.println("IOException in ServerSideConnection constructor");
            }
        }

        public void run() {
            try {
                dataOut.writeInt(playerId);
                dataOut.flush();

                while (true) {
                    if (game.getNowPlayer() != playerId) {
                        continue;
                    }
                    if (playerId == 1) {
                        player1Row = dataIn.readInt();
                        player1Col = dataIn.readInt();
                        System.out.println("Got signal as 1");
                        TicTacToe.MoveResult res = game.makeMove(player1Row, player1Col);
                        assert(res != TicTacToe.MoveResult.ILLEGAL_MOVE);
                        System.out.println("Player 1 set to " + player1Row + " " + player1Col);
                        player2.sendPos(player1Row, player1Col);
                    } else {
                        player2Row = dataIn.readInt();
                        player2Col = dataIn.readInt();
                        System.out.println("Got signal as 2");
                        TicTacToe.MoveResult res = game.makeMove(player2Row, player2Col);
                        assert(res != TicTacToe.MoveResult.ILLEGAL_MOVE);
                        System.out.println("Player 2 set to " + player2Row + " " + player2Col);
                        player1.sendPos(player2Row, player2Col);
                    }
                    game.printField();
                }
            } catch (IOException e) {
                System.out.println("IOException from run in ServerSideConnection");
            }
        }

        public void sendPos(int row, int col) {
            try {
                dataOut.writeInt(row);
                dataOut.writeInt(col);
                dataOut.flush();
            } catch (IOException e) {
                System.out.println("Error in sendPos() in SSC");
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.acceptConnections();
    }
}
