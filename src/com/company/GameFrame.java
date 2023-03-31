package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class GameFrame extends JFrame {
    skorPanel skorpanel = new skorPanel();
    GamePanel p1 = new GamePanel(this);
    MenuPanel m1 = new MenuPanel(p1,skorpanel);
    JPanel panelContainer = new JPanel();

    ArrayList<JLabel> labels = new ArrayList<>();

    class CustomWindowAdaptor extends WindowAdapter {
        GameFrame window;
        public CustomWindowAdaptor(GameFrame window){
            this.window=window;
        }

        public void windowClosing(WindowEvent e){
            try {
                System.out.println("aloooo");
                Client.sendMessage("/exit");
                Client.socket.close();
                System.exit(0);
            }catch (Exception exc){}
        }

    }

    public void gameStart(){
        m1.setVisible(false);
        remove(m1);


        /*
        for (int i=0;i<m1.c1.oyuncular.length;i++){
            JLabel label = new JLabel(m1.c1.oyuncular[i]+" "+m1.c1.oyuncuPuanlari[i]){
                public Dimension getPrefferedSize(){
                    return new Dimension(100,100);
                }
            };
            labels.add(label);
            skorpanel.add(label);
        }

         */
        add(panelContainer);
        panelContainer.setVisible(true);

    }
    public void menuStart(){
        panelContainer.setVisible(false);
        remove(panelContainer);
        add(m1);
        m1.setVisible(true);
    }

    public void renkDegistir(String s){
        try {
            Thread.sleep(500);
        }catch (Exception e){}
        for (int i=0;i<labels.size();i++){
            labels.get(i).setBorder(BorderFactory.createEmptyBorder());
        }
        for (int i=0;i<labels.size();i++){
            int n=labels.get(i).getText().indexOf(" ");
            if (labels.get(i).getText().substring(0,n).equals(s))
                labels.get(i).setBorder(BorderFactory.createEtchedBorder(1,Color.BLUE,Color.MAGENTA));
        }
    }


    public GameFrame() {
        setSize(Screen.width, Screen.heigth);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        addWindowListener(new CustomWindowAdaptor(this));
        GridLayout gl=new GridLayout(8,8);
        gl.setHgap(2);
        gl.setVgap(2);
        p1.setLayout(gl);

        panelContainer.setLayout(new BorderLayout());



        add(panelContainer);
        panelContainer.add(skorpanel,BorderLayout.PAGE_START);
        panelContainer.add(p1,BorderLayout.CENTER);
        //panelContainer.setPreferredSize(new Dimension(800,100));

        menuStart();

        m1.setLayout(null);
        m1.setBackground(Color.WHITE);

        //p1.setLayout(null);
        p1.setBackground(Color.BLACK);

        this.setLocationRelativeTo(null);
        setVisible(true);
        //setResizable(false);
        getContentPane().setPreferredSize(new Dimension(Screen.width, Screen.heigth));
        pack();
        setTitle("Memory Card Game");


        while (true){
            p1.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

}
