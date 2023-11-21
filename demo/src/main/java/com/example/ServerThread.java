package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread{
    protected Socket socket;
    protected String username;
    protected BufferedReader in;
    protected DataOutputStream out;

    public ServerThread(Socket socket) {
        this.socket = socket;
        this.username = "";
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String getUsername(){
        return this.username;
    }

    public Socket getSocket(){
        return this.socket;
    }

    public boolean setUsername(String string) throws IOException{
        boolean kek = true;
        for(int i=0; i<App.personeinchat.size(); i++){
            if(App.personeinchat.get(i).getUsername().equals(username)){
                out.writeBytes("!\n");
                kek = false;
            }
        }
        if(kek){
            this.username = string;
            out.writeBytes(".\n");
        }
        return kek;
    }

    public void sendBroadcast(String string) throws IOException{
        out.writeBytes(".\n");
        for(int i=0; i<App.personeinchat.size(); i++){
            if(!this.username.equals(App.personeinchat.get(i).getUsername())){
                DataOutputStream outsingle = new DataOutputStream(App.personeinchat.get(i).getSocket().getOutputStream());
                outsingle.writeBytes(string + "\n");
            }
        }
    }

    public void sendMessage(String string) throws IOException{
        boolean kek = false;
        DataOutputStream outsingle = null;
        for(int i=0; i<App.personeinchat.size(); i++){
            if(App.personeinchat.get(i).getUsername().equals(username)){
                outsingle = new DataOutputStream(App.personeinchat.get(i).getSocket().getOutputStream());
                kek=true;
            }
        }
        if(kek){
            outsingle.writeBytes(string + "\n");
        }else{
            out.writeBytes("Utente non trovato\n");
        }
    }

    @Override
    public void run(){
        try {
            String incoming;
            String message[];
            boolean exit = false;
            setUsername("");
            System.out.println("L'utente " + this.username + " si è connesso");
            sendBroadcast("L'utente " + this.username + " si è connesso alla chat");
            while(!exit){
                incoming = in.readLine();
                message = incoming.split(":");
                switch(message[0]){
                    case "@everyone":
                        System.out.println("L'utente " + this.username + " vuole comunicare in broadcast");
                        sendBroadcast(this.username + " scrive: " + message[1]);
                        break;
                    case "@exit":
                        System.out.println("L'utente " + this.username + " vuole chiudere la connessione");
                        exit = true;
                        sendBroadcast("L'utente " + this.username + " ha abbandonato la chat");
                        break;
                    case "@username":
                        System.out.println("L'utente " + this.username + " vuole cambiare il suo username");
                        setUsername("");
                        break;
                    default:
                        System.out.println("L'utente " + this.username + " vuole comunicare in privato");
                        sendMessage(message[1]);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}