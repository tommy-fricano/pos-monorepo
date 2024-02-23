package com.virtualJournal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {

    private Socket socket = null;

    private int port;
    private PrintWriter out;
    private BufferedReader in;
    public ClientSocket(int port) throws IOException {
        this.port = port;
    }

    public void startConnection() throws IOException {
        boolean scanning=true;
        while(scanning) {
            try {
                socket = new Socket("127.0.0.1", port);
                scanning=false;
            } catch(Exception e) {
                System.out.println("Connect failed, waiting and trying again");
                try {
                    Thread.sleep(3000);//2 seconds
                } catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        }

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        sendMessage("Client connected.");
        System.out.println("Connected to server.");

        while (true) {
            try {
                char c = (char)in.read();
                if(c == '\uFFFF'){
                    break;
                }
                System.out.print(c);
            }
            catch (Exception e){
                System.out.println(e);
                break;
            }
        }
    }

    public void sendMessage(String msg) throws IOException {
        try {
            out.println(msg);
            out.flush();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void stopConnection() throws IOException {
        try {
            in.close();
            out.close();
            socket.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
