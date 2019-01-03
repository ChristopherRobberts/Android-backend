package net;

import controller.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket clientSocket;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private boolean connected = false;
    Controller controller = new Controller();

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        this.connected = true;
        try {
            this.fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.toClient = new PrintWriter(clientSocket.getOutputStream(), true);

            while (connected) {
                String message = fromClient.readLine();
                System.out.println(message);

                if (!controller.gameIsOngoing()) {
                    controller.startGame();
                }

                this.controller.processGuess(message);
                String state = createStateMessage();
                toClient.println(state);
                System.out.println(controller.getWordState());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createStateMessage() {
        System.out.println(controller.getAttemptsState());
        return controller.getWordState() + "#" + controller.getAttemptsState() + "#" + controller.getScoreState();
    }
}