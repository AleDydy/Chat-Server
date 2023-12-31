package com.example;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Sender extends Thread{
    private Socket socket;
    private ArrayList<String> buffer;
    private boolean loop;

    public Sender(Socket socket){
        this.socket = socket;
        this.buffer = new ArrayList<String>();
        this.loop = true;
    }

    public void addMessage(String message){
        buffer.add(message);
    }

    public void close(){
        this.loop = false;
    }

    @Override
    public void run(){
        try{
            DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
            do{
                String message = null;
                if(buffer.size() > 0){
                    message = buffer.get(0);
                }
                if(message != null){
                    out.writeBytes(message);
                }
            }while(loop);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
