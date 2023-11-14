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
                
                boolean exit = true;
                String username=in.readLine();
                this.username=username;

                while (exit == true) {
        
                    String message=in.readLine();

                    if(username.equals("@everyone")){
                        for(int i=0; i<App.personeinchat.size(); i++){
                                DataOutputStream outsingle=new DataOutputStream(App.personeinchat.get(i).getSocket().getOutputStream());
                                outsingle.writeBytes(message);
                            }
                        }

                    else{
                        boolean kek=false;

                        for(int i=0; i<App.personeinchat.size(); i++){
                            if(App.personeinchat.get(i).getUsername().equals(username)){
                                DataOutputStream outsingle=new DataOutputStream(App.personeinchat.get(i).getSocket().getOutputStream());
                                outsingle.writeBytes(message);
                                kek=true;
                            }
                        }

                        if(kek){
                            out.writeBytes("." + "\n");
                        }
                        else{
                            out.writeBytes("#" + "\n");
                        }
                    }

                    boolean flag=false;

                    for(int i=0; i<App.personeinchat.size(); i++){
                        if(App.personeinchat.get(i).getName().equals(username)){
                            flag=true;
                        }
                    }

                    if(flag==true){
                        out.writeBytes("!" + "\n");
                    }
                    else{
                        out.writeBytes("." + "\n");
                    }


                    if(username.equals("@exit")){
                        out.writeBytes("-" + "\n");
                        exit=false;
                    }
                }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}