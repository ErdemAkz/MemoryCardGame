package com.company;

import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.*;

public class skorPanel extends JPanel {

    JLabel [] labels;
    public skorPanel(){
        super();
    }

    public  void setOyuncular(String [] oyuncular)
    {
        removeAll();
        GridLayout gy =new GridLayout(1,oyuncular.length);
        labels = new JLabel[oyuncular.length];
        gy.setHgap(40);
        setLayout(gy);
        for (int i=0;i< oyuncular.length;i++)
        {
            labels[i]=new JLabel(oyuncular[i]) {

                public Dimension getPrefferedSize(){
                    return new Dimension(100,100);
                };
            };
            labels[i].setPreferredSize(new Dimension(100,30));
            add(labels[i]);
        }
    }

    public void  setOrder(String order) {
        for (int i=0;i<labels.length;i++) {
            JLabel label=labels[i];
            int n=label.getText().indexOf(":");
            System.out.println("("+label.getText().substring(0,n)+")("+order+")");
           if (label.getText().substring(0,n).equals(order)) {
               label.setBorder(BorderFactory.createEtchedBorder(1,Color.BLUE,Color.CYAN));
            }
            else
               label.setBorder(BorderFactory.createEmptyBorder());
        }
    }
}
