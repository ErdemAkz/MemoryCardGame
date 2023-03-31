package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.Set;
import java.util.Iterator;
import java.net.InetSocketAddress;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel{


    static class ConfirmWindow extends JFrame implements ActionListener {
        public ConfirmWindow(){
            setSize(300,300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(null);
            JLabel label = new JLabel("Oyun Bitti ! ");
            JButton ok = new JButton("Exit");
            label.setBounds(115,30,100,30);
            ok.addActionListener(this);
            ok.setBounds(115,200,70,20);
            String puan = "";
            for (int i=0;i<players.size();i++){
                puan = puan + players.get(i).name+"'s points: "+players.get(i).puan+"\n";
            }
            JTextArea puanLabel = new JTextArea(puan);
            puanLabel.setBounds(80,60,140,100);
            puanLabel.setEditable(false);


            add(puanLabel);
            add(label);
            add(ok);
            this.setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Exit")){
                System.exit(0);
            }
        }
    }
    static class KaydetListener implements ActionListener{
        public void actionPerformed(ActionEvent arg0) {

        }
    }



    static class OyunucuWindow extends JFrame {
        public OyunucuWindow(){
            setSize(500,400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(null);

            ArrayList<JTextField> fields = new ArrayList<>();
            JLabel isim = new JLabel("Oyuncu Isimlerini Girin");
            isim.setBounds(165,50,200,20);
            for (int i=0;i<5;i++) {
                JTextField text = new JTextField();
                fields.add(text);
            }
            JButton onlineButton = new JButton("Online Game");
            onlineButton.addActionListener(new KaydetListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    onlineGame=true;


                    dispose();
                }
            });
            onlineButton.setBounds(10,250,170,35);

            JLabel oyuncu1Label = new JLabel("Player1: ");
            oyuncu1Label.setBounds(10,100,80,20);
            fields.get(0).setBounds(100,100,80,20);

            JLabel oyuncu2Label = new JLabel("Player2: ");
            oyuncu2Label.setBounds(10,130,80,20);
            fields.get(1).setBounds(100,130,80,20);

            JLabel oyuncu3Label = new JLabel("Player3: ");
            oyuncu3Label.setBounds(10,160,80,20);
            fields.get(2).setBounds(100,160,80,20);

            JLabel oyuncu4Label = new JLabel("Player4: ");
            oyuncu4Label.setBounds(10,190,80,20);
            fields.get(3).setBounds(100,190,80,20);

            JLabel oyuncu5Label = new JLabel("Player5: ");
            oyuncu5Label.setBounds(10,220,80,20);
            fields.get(4).setBounds(100,220,80,20);

            JButton ok = new JButton("Tamam");
            ok.setBounds(200,330,100,20);
            add(ok);
            add(fields.get(0));
            add(oyuncu1Label);
            add(fields.get(1));
            add(oyuncu2Label);
            add(fields.get(2));
            add(oyuncu3Label);
            add(fields.get(3));
            add(oyuncu4Label);
            add(fields.get(4));
            add(oyuncu5Label);
            add(isim);
            add(onlineButton);
            this.setLocationRelativeTo(null);
            ok.addActionListener(new KaydetListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    for (int i=0;i<5;i++){
                        if (fields.get(i).getText().length()>=1) {
                            break;
                        }
                        JLabel uyari = new JLabel("*Lutfen en az 1 isim giriniz");
                        uyari.setBounds(300,170,200,20);
                        uyari.setForeground(Color.RED);
                        add(uyari);
                        repaint();
                        return;
                    }

                    for (int i=0;i<5;i++){
                        if (fields.get(i).getText().length()>=1) {
                            player p = new player(fields.get(i).getText());
                            players.add(p);
                        }
                    }
                    oyunBaslasin=1;
                    dispose();
                }
            });


            setResizable(false);
            setVisible(true);
        }
    }

    static boolean ClickOn=false;
    static int openCardCount=0;
    static Card firstcard;
    static Card secondcard;
    static ArrayList <Card> cards = new ArrayList<>(64);
    public static int size=0;
    public static int sira=1;
    public static int oyuncuSayisi;
    public static ArrayList<player> players = new ArrayList<>();
    public static ArrayList<JLabel> puanLabels = new ArrayList<>();
    public static JLabel siradaki = new JLabel();
    public static int oyunBaslasin=0;
    public static boolean onlineGame=false;
    public GameFrame gameFrame;



    public String randomIntAl(){

        Random random = new Random();
        boolean siradakiKart=false;
        int randomSayiX=0;
        int randomSayiY=0;
        while(!siradakiKart) {
            randomSayiX = random.nextInt(8);
            randomSayiY = random.nextInt(8);
            siradakiKart=true;
            for (int i=0;i<size;i++){
                if (cards.get(i).x==randomSayiX && cards.get(i).y==randomSayiY)
                    siradakiKart=false;
            }
        }
        //System.out.println(""+randomSayiX+""+randomSayiY);
        size++;
        return randomSayiX+""+randomSayiY;
    }

    public void orderRenkDegistir(String s){
        gameFrame.renkDegistir(s);
    }

    public static void openCard(Card p,boolean internal) {
        openCardCount++;

        if (openCardCount==1){
            firstcard=p;
            if (internal)
                Client.sendMessage("/t1 "+p.index);
        }
        if (openCardCount==2){
            secondcard=p;
            if (internal)
                Client.sendMessage("/t2 "+p.index);
        }
        if (openCardCount >=2 )  {
            ClickOn=false;
            Thread t = new Thread(new beklemeThread());
            t.start();
        }

    }
    public static void openCard(String s) {
        int sayi = Integer.parseInt(s);
        Card kart = cards.get(sayi);
        kart.openCard(false);
        System.out.println("string: "+s);
        System.out.println("sayi: "+sayi);
    }

    public static void closeCards() {
        if (firstcard.value==secondcard.value) {
            firstcard.setTaken();
            secondcard.setTaken();
            ClickOn = true;
            control();
        }
        else {
            firstcard.closeCard();
            secondcard.closeCard();
            ClickOn = true;
        }
        control();
        openCardCount=0;
    }

    public void newGame(String s){
        String[] values = s.split(" ");
        for (int i=0;i<cards.size();i++){
            try {
                Image img = ImageIO.read(getClass().getResource("/images/"+values[i] +".png"));
                img=img.getScaledInstance(100,100,Image.SCALE_SMOOTH);
                cards.get(i).image = new ImageIcon(img);
                cards.get(i).value = Integer.parseInt(values[i]);
            }
            catch (Exception ex)
            {
                System.out.println("NEW GAME HATA ALINDI! "+ex);
            }

        }
        gameFrame.gameStart();

    }


    public GamePanel(GameFrame gameFrame) {
        this.gameFrame=gameFrame;
        setSize(Screen.width, Screen.heigth);


        for (int i=0;i<8;i++){
            for (int j=0;j<8; j++) {
                Card kart1 = new Card();
                String koordinat = randomIntAl();
                kart1.setPreferredSize(new Dimension(100,100));
                kart1.x = j;
                kart1.y = i;
                kart1.index=i*8+j;
                cards.add(kart1);

                kart1.closeCard();

                add(kart1);
                kart1.setVisible(true);
            }
        }
/*
        new OyunucuWindow();

        while (true){
            if (oyunBaslasin==1)
                break;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/
/*
        this.oyuncuSayisi=players.size();

        for (int i=0;i<players.size();i++){
            JLabel label = new JLabel(players.get(i).name +"'s points--->"+ players.get(i).puan );
            if (i==0) {
                label.setForeground(Color.CYAN);
                label.setBounds(20,30,150,30);
            }
            else if(i==1) {
                label.setForeground(Color.RED);
                label.setBounds(170,30,150,30);
            }
            else if (i==2) {
                label.setForeground(Color.GREEN);
                label.setBounds(320,30,150,30);
            }
            else if(i==3) {
                label.setForeground(Color.PINK);
                label.setBounds(470,30,150,30);
            }
            else if (i==4) {
                label.setForeground(Color.YELLOW);
                label.setBounds(620,30,150,30);
            }
            puanLabels.add(label);
            add(label);
        }
        */
 /*

        siradaki.setText("Next ---> "+ players.get(0).name);
        siradaki.setBounds(350,70,150,20);
        siradaki.setForeground(Color.WHITE);
        add(siradaki);

  */

        setFocusable(true);
        setVisible(true);
        ClickOn=true;

    }

    public static void control(){

        for (int i=0; i<size; i++){
            if (cards.get(i).hasTaken==false)
                return;
        }

    }





    public void paint(Graphics g){
        super.paint(g);
    }
}
