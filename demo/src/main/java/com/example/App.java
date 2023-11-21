package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class App {
    static ArrayList <ServerThread> personeinchat = new ArrayList();
    public static void main( String[] args ) {
        try {
            ServerSocket servsock= new ServerSocket(3000);
            boolean loop = true;
            while(loop){
                System.out.println("Server in attesa");
                Socket s=servsock.accept();
                ServerThread thread=new ServerThread(s);
                System.out.println("Client connesso con il thread " + thread.getName());
                thread.start();
                personeinchat.add(thread);
            }
            servsock.close();
            System.out.println("Sospensione server");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}