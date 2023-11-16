package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread{
    protected Socket socket;
    protected String username;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public String getUsername(){
        return this.username;
    }

    public Socket getSocket(){
        return this.socket;
    }

    @Override
    public void run(){

        try {
            
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String username;
            String message;
            boolean exit = false;
            boolean kek;
            do{
                kek = true;
                username = in.readLine();
                for(int i=0; i<App.personeinchat.size(); i++){
                    if(App.personeinchat.get(i).getUsername().equals(username)){
                        out.writeBytes("!\n");
                        kek = false;
                    }
                }
                if(kek){
                    out.writeBytes(".\n");
                }
            }while(!kek);
            for(int i=0; i<App.personeinchat.size(); i++){
                if(!this.username.equals(App.personeinchat.get(i).getUsername())){
                    DataOutputStream outsingle=new DataOutputStream(App.personeinchat.get(i).getSocket().getOutputStream());
                    outsingle.writeBytes("+\n");
                    outsingle.writeBytes(this.username + "\n");
                }
            }
            while(!exit){
                username = in.readLine();
                switch(username){
                    case "@everyone":
                        out.writeBytes(".\n");
                        message = in.readLine();
                        for(int i=0; i<App.personeinchat.size(); i++){
                            if(!this.username.equals(App.personeinchat.get(i).getUsername())){
                                DataOutputStream outsingle=new DataOutputStream(App.personeinchat.get(i).getSocket().getOutputStream());
                                outsingle.writeBytes("@\n");
                                outsingle.writeBytes(this.username + "\n");
                                outsingle.writeBytes(message + "\n");
                            }
                        }
                        break;
                    case "@exit":
                        out.writeBytes("&\n");
                        out.writeBytes("x\n");
                        exit = true;
                        for(int i=0; i<App.personeinchat.size(); i++){
                            if(!this.username.equals(App.personeinchat.get(i).getUsername())){
                                DataOutputStream outsingle=new DataOutputStream(App.personeinchat.get(i).getSocket().getOutputStream());
                                outsingle.writeBytes("-\n");
                                outsingle.writeBytes(this.username + "\n");
                            }
                        }
                        break;
                    default:
                        DataOutputStream outsingle = null;
                        for(int i=0; i<App.personeinchat.size(); i++){
                            if(App.personeinchat.get(i).getUsername().equals(username)){
                                outsingle = new DataOutputStream(App.personeinchat.get(i).getSocket().getOutputStream());
                                kek=true;
                            }
                        }
                        if(kek){
                            out.writeBytes(".\n");
                            message = in.readLine();
                            outsingle.writeBytes("*\n");
                            outsingle.writeBytes(this.username + "\n");
                            outsingle.writeBytes(message + "\n");
                        }else{
                            out.writeBytes("#\n");
                        }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}