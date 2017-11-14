
package enyakinkomsu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

public class EnYakinKomsu {

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setSize(600, 600);
        window.setTitle("K Nearest Neighborhood");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        
        drawingComponent DC = new drawingComponent();
        window.add(DC);
        
        window.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                DC.paintClick(e.getX(),e.getY());
            }
        });
        
        
    }
    
}
