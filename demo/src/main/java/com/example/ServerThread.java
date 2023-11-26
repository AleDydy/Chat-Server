package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread{
    private Socket socket;
    public String username;
    private BufferedReader in;
    private Sender sender;
    public Semaforo semaforo;

    public ServerThread(Socket socket) {
        this.socket = socket;
        this.username = "";
        this.sender = new Sender(this.socket);
        this.semaforo = new Semaforo();
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

    public void sendMessage(String message){
        semaforo.p();
        sender.addMessage(message);
        semaforo.v();
    }

    public boolean setUsername(String string) throws IOException{
        boolean kek = true;
        for(int i=0; i<App.peopleInChat.size(); i++){
            if(App.peopleInChat.get(i).getUsername().equals(string)){
                kek = false;
            }
        }
        if(kek){
            this.username = string;
        }
        return kek;
    }

    public boolean sendBroadcast(String message) throws IOException{
        boolean kek = false;
        for(int i=0; i<App.peopleInChat.size(); i++){
            if(!this.username.equals(App.peopleInChat.get(i).getUsername()) || !App.peopleInChat.get(i).getUsername().equals("")){
                App.peopleInChat.get(i).sendMessage(message);;
                kek = true;
            }
        }
        return kek;
    }

    public void sendPrivate(String username, String message) throws IOException{
        boolean kek = false;
        for(int i=0; i<App.peopleInChat.size(); i++){
            if(App.peopleInChat.get(i).getUsername().equals(username) && !App.peopleInChat.get(i).getUsername().equals("")){
                App.peopleInChat.get(i).sendMessage(this.username + " sent you this message: " + message + "\n");
                kek = true;
                return;
            }
        }
        if(kek && !username.equals("")){
            sendMessage(".\n");
        }else{
            sendMessage("!\n");
        }
    }

    @Override
    public void run(){
        try {
            sender.start();
            String incoming;
            String message[];
            boolean exit = false;
            boolean kek;
            do{
                incoming = in.readLine();
                System.out.println(incoming);
                kek = setUsername(incoming);
                if(kek){
                    sendMessage(".\n");        
                }else{
                    sendMessage("!\n");
                }
            }while(!kek);
            System.out.println(this.username + " entered the chat");
            sendBroadcast(this.username + " entered the chat");
            while(!exit){
                incoming = in.readLine();
                message = incoming.split("ඞ");
                switch(message[0]){
                    case "@everyone":
                        System.out.println(this.username + " want to comunicare with everyone");
                        kek = sendBroadcast(this.username + " sent everyone this message: " + message[1]);
                        if(kek){
                            sendMessage(".\n");
                        }else{
                            sendMessage("!\n");
                        }
                        break;
                    case "@exit":
                        System.out.println(this.username + " want to close the connection");
                        exit = true;
                        sendBroadcast("L'utente " + this.username + " left the chat");
                        sendMessage("#\n");
                        break;
                    case "@username":
                        System.out.println(this.username + " want to change his/her username");
                        kek = setUsername(message[1]);
                        if(kek){
                            sendMessage(".\n");
                        }else{
                            sendMessage("!\n");
                        }
                        break;
                    default:
                        System.out.println(this.username + " want to chat in private mode");
                        sendPrivate(message[0], message[1]);
                }
            }
            sender.close();
        } catch (Exception e) {
            App.peopleInChat.remove(this);
            System.out.println(e.getMessage());
        }
    }
}