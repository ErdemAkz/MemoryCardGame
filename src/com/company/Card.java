package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Card extends JLabel implements MouseListener{
    public int x;
    public int y;
    public boolean open;
    public boolean hasTaken;
    public int index;
    public int value;
    public ImageIcon image;
    public int kartNumarasi;
    public static boolean enabled = false;

     Image pencere ;
            //new ImageIcon("images/pencere.png");

    public Card(int x, int y, int index, ImageIcon img) throws IOException {
        super(img);
        this.x=x;
        this.y=y;
        open=false;
        hasTaken=false;
        this.index=index;
        this.image=img;
    }
    public Card() {
        super();
        open=false;
        hasTaken=false;
        addMouseListener(this);
        try {
            Image img=ImageIO.read(getClass().getResource("/images/pencere.png"));
            pencere = img.getScaledInstance(100,100,Image.SCALE_SMOOTH);
        }
        catch (Exception ex) {

        }
    }


    public void openCard(boolean internal) {
        open=true;
        setIcon(image);
        GamePanel.openCard(this,internal);
    }
    public void  closeCard() {
        open=false;
        if (!hasTaken)
            setIcon(new ImageIcon(pencere));
    }
    public void setTaken() {
        hasTaken=true;
        setIcon(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println(enabled);
        if (enabled) {
            if (hasTaken) return;
            if (GamePanel.ClickOn == false) return;
            if (!open)
                openCard(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

}
