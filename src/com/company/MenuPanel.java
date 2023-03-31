package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    static class KaydetListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {

        }
    }
    public Client c1;
    public GamePanel gamePanel;
    skorPanel skorpanel;
    public static String isim;
    public JTextArea ism = new JTextArea();

    public void oyunculariListele(){

        String i="";
        String[] s = c1.oyuncular;
        ism.setText("");
        for (int j=0;j<s.length;j++){
            i=i+s[j]+"\n";
        }
        ism.setText(i);

        repaint();

    }


    public MenuPanel(GamePanel gamePanel,skorPanel skorpanel){
        setSize(Screen.width,Screen.heigth);
        this.gamePanel=gamePanel;
        this.skorpanel=skorpanel;
        ism.setEditable(false);
        ism.setBounds(320,400,150,300);
        add(ism);

        JLabel isminiz = new JLabel("Isminizi Giriniz:");
        isminiz.setBounds(320,120,150,30);

        JTextField nickname = new JTextField();
        nickname.setBounds(320,150,150,30);

        JLabel ipgir = new JLabel("IP Adresi Giriniz:");
        ipgir.setBounds(320,180,150,30);

        JTextField serverip = new JTextField();
        serverip.setBounds(320,210,150,30);
        serverip.setText("192.168.1.150");


        JButton startGame = new JButton("Start Game!");
        startGame.addActionListener(new GamePanel.KaydetListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Client.sendMessage("/newgame");
            }
        });
        startGame.setBounds(320,300,150,30);
        startGame.setEnabled(false);

        c1= new Client(gamePanel,this,skorpanel);

        JButton onlineButton = new JButton("Connect!");
        onlineButton.addActionListener(new GamePanel.KaydetListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                c1.connectTo();
                Thread t1 = new Thread(c1);
                t1.start();

                if (Client.connected) {
                    c1.sendMessage("/name "+nickname.getText());
                    startGame.setEnabled(true);
                    onlineButton.setEnabled(false);
                }
                isim=nickname.getText();
            }
        });
        onlineButton.setBounds(320,240,150,30);



        add(nickname);
        add(isminiz);
        add(ipgir);
        add(serverip);
        add(onlineButton);
        add(startGame);

        setVisible(true);
    }


}
