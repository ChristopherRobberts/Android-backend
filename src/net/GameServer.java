package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class GameServer {
    private static final int TIME_UNTIL_TIMEOUT = 600000;
    private static final int LINGER_TIME = 5000;
    private int port = 8080;

    public static void main(String[] args) {
        new GameServer().serve();
    }

    private void serve() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected!");
                serveClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serveClient(Socket socket) throws SocketException {
        socket.setSoTimeout(TIME_UNTIL_TIMEOUT);
        socket.setSoLinger(true, LINGER_TIME);
        ClientHandler clientHandler = new ClientHandler(socket);
        Thread dedicatedThread = new Thread(clientHandler);
        dedicatedThread.start();
    }
}
