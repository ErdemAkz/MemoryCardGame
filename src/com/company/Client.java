package com.company;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static com.company.GamePanel.players;

public class Client implements Runnable {
    static boolean connected=false;
    public static Socket socket;
    static public GamePanel gamePanel;
    static public MenuPanel menuPanel;
    static public skorPanel skorpanel;
    static public String[] oyuncular;
    static public String[] oyuncuPuanlari;

    static String orderName ="";


    public Client(GamePanel gamePanel, MenuPanel menuPanel,skorPanel skorpanel){
        this.gamePanel=gamePanel;
        this.menuPanel=menuPanel;
        this.skorpanel=skorpanel;
    }


    public void processMsg(String msj){
        System.out.println("mesaj: "+msj);
        int n = msj.indexOf(" ");
        String komut = msj.substring(0,n);
        if (komut.equals("/newgame")){
            System.out.println("newgame!!!" + msj.substring(9));
            gamePanel.newGame(msj.substring(9));
        }
        else if(komut.equals("/t1")){
            System.out.println("asdas"+msj.substring(4));
            gamePanel.openCard(msj.substring(4));
        }
        else if (komut.equals("/t2")){
            System.out.println("t2: "+msj.substring(4));
            gamePanel.openCard(msj.substring(4));
        }
        else if (komut.equals("/order")){
            //System.out.println(MenuPanel.isim);
            //System.out.println(msj.substring(7));
            orderName=msj.substring(7);
            skorpanel.setOrder(orderName);
            gamePanel.orderRenkDegistir(msj.substring(7));
            if (MenuPanel.isim.equals(msj.substring(7))){
                Card.enabled=true;
            }
            else
                Card.enabled=false;
        }
        else if(komut.equals("/listPlayers")){
            String[] isimler=msj.substring(13).split(" ");
            oyuncular=isimler;
            menuPanel.oyunculariListele();
        }
        else if (komut.equals("/listPoints")){
            String[] puanlar=msj.substring(12).split(" ");
            skorpanel.setOyuncular(puanlar);
            oyuncuPuanlari=puanlar;
        }
        else
            System.out.println("noluyoz?");


    }


    public void run() {
        byte [] buffer=new byte[512];
        int n=-1;
        System.out.println("Run baby!");
        InputStream in=null;
        BufferedReader bufin=null;

        try {
            in = socket.getInputStream();
            bufin = new BufferedReader(new InputStreamReader(in));
        }
        catch (Exception ex) {}

        while (true) {
            try {
                String msg = bufin.readLine();
                //DisplayText.append(msg+"\r\n");
                processMsg(msg);
            }
            catch (Exception ex) {
                connected=false;
                System.out.println("Connection Koptu "+ ex);
                System.exit(1);
                //DisplayText.append("* "+n+" can not read from socket!\r\n");
                //        connect();
            }

        }
    }
    public static void connectTo() {
        try {
            socket = new Socket("192.168.1.150", 8080);
            connected=true;
            //DisplayText.append("* Connected!\r\n");
        }
        catch (IOException ex) {
            System.out.println("Bağlantı Kurulamadı! "+ex);

        }
    }
    public static void sendMessage(String msg) {

        try {
            OutputStream out = socket.getOutputStream();
            BufferedWriter bufOut = new BufferedWriter( new OutputStreamWriter( out ) );
            bufOut.write( msg );
            bufOut.newLine();
            bufOut.flush();
        }
        catch (Exception ex) {
            connected=false;
            System.out.println("* can not send message!\r\n");
            connectTo();
        }
    }


}
