package com.virtualJournal;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class VirtualJournalSocketApplication {

    public static void main(String[] args) throws IOException {

        ClientSocket clientSocket = new ClientSocket(4040);
        clientSocket.startConnection();
        clientSocket.stopConnection();
    }
}
