package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App {
    static ArrayList <ServerThread> personeinchat=new ArrayList();
    public static void main( String[] args ) {
        try {
            ServerSocket servsock= new ServerSocket(3000);
             
            while(true){
                Socket s=servsock.accept();

                ServerThread thread=new ServerThread(s);
                thread.start();
                personeinchat.add(thread);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}