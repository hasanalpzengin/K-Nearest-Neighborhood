
package enyakinkomsu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
/**
 *
 * @author hasalp
 */
public class drawingComponent extends JComponent {
    
    private final int GROUP_AMOUNT = 2;
    private List<String> groupNames = new ArrayList<>();
    private final int CIRCLE_WIDTH = 10;
    private final int CIRCLE_HEIGHT = 10;
    private final int GRID = 20;
    private final int NEIGHBOR = 3;
    private final int BIG_VALUE = 9999999;
    private Graphics2D g2;
    
    public void paintComponent(Graphics g){
        g2 = (Graphics2D) g;
        
        List<Komsu> komsular = getValues();
        for(Komsu komsu : komsular){
            g2.setColor(komsu.getGroup().getGroupColor());
            System.out.println(komsu.getCoordinate_x()+" "+komsu.getCoordinate_y());
            g2.drawOval(komsu.getCoordinate_x(), komsu.getCoordinate_y(), CIRCLE_WIDTH, CIRCLE_HEIGHT);
        }
        
    }
    
    public void paintClick(int x, int y){
        //setup start
        g2 = (Graphics2D) this.getGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.setColor(Color.black);
        this.paintComponent(g2);
        groupNames.clear();
        List<Group> groups = getGroups();
        List<Komsu> komsu = getValues();
        List<Komsu> nearest = new ArrayList<>();
        nearest.clear();
        int[] smallerInfo = new int[2];
        double tempx, tempy;
        //setup end
        
        for(int j=0;j<NEIGHBOR;j++){
            smallerInfo[1]=BIG_VALUE;
            //trying to find smallest neighbor
            for(int i=0;i<komsu.size();i++){
                //oklid calculation
                tempx = Math.pow((x-komsu.get(i).getCoordinate_x())-5,2);
                tempy = Math.pow((y-komsu.get(i).getCoordinate_y())-40,2);
                //smallest oklid
                if(smallerInfo[1]>=Math.sqrt(tempx+tempy)){
                    smallerInfo[0]=i;
                    smallerInfo[1]=(int) Math.sqrt(tempx+tempy);
                    System.out.println("nearest coordinates "+i+" = "+komsu.get(i).getCoordinate_x()+" "+komsu.get(i).getCoordinate_y());
                }
            }
            nearest.add(komsu.get(smallerInfo[0]));
            System.out.println(smallerInfo[0]+" deleted");
            komsu.remove(smallerInfo[0]);
        }
        //find group classification
        int group1Amount=0;
        int group2Amount=0;
        for(Komsu k: nearest){
            if(k.getGroup().getGroupName().equalsIgnoreCase(groupNames.get(0))){
                group1Amount++;
            }else{
                group2Amount++;
            }
            System.out.println(group1Amount+" "+group2Amount);
        }
        //draw
        if(group1Amount<group2Amount){
            g2.setColor(groups.get(1).getGroupColor());
            g2.drawOval(x-5, y-40, CIRCLE_WIDTH, CIRCLE_HEIGHT);
            g2.drawString("Selected Item Belong To "+groupNames.get(0)+" Group", this.getWidth()-250, 40);
            System.out.print("Belong to: "+groupNames.get(0));
        }else if(group1Amount>group2Amount){
            g2.setColor(groups.get(0).getGroupColor());
            g2.drawOval(x-5, y-40, CIRCLE_WIDTH, CIRCLE_HEIGHT);
            g2.drawString("Selected Item Belong To "+groupNames.get(1)+" Group", this.getWidth()-250, 40);
            
            System.out.print("Belong to: "+groupNames.get(1));
        }else{
            g2.setColor(Color.BLACK);
            g2.drawOval(x-5, y-40, CIRCLE_WIDTH, CIRCLE_HEIGHT);
        }
        
    }
    
    public List<Group> getGroups(){
        FileReader fileReader = null;
        String line;
        int count=0;
        String[] tempString;
        List<Group> groupList = new ArrayList<>();
        try {
            File dataFile = new File("src/enyakinkomsu/data.dat");
            fileReader = new FileReader(dataFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while(count<GROUP_AMOUNT){
               //read row GROUP_AMOUNT times
               line = bufferedReader.readLine();
               tempString = line.split(" ");
               Group group = new Group();
               group.setGroupName(tempString[0]);
               groupNames.add(tempString[0]);
               Field field = Color.class.getField(tempString[1]);
               group.setGroupColor((Color)field.get(null));
               groupList.add(group);
               //one row readed
               count++;
            }
        } catch (Exception e) {
            Logger.getLogger(drawingComponent.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return groupList;
    }
    
    public List<Komsu> getValues(){
        FileReader fileReader = null;
        String line;
        int count=0;
        String[] tempString;
        List<Komsu> valueList = new ArrayList<>();
        try {
            File dataFile = new File("src/enyakinkomsu/data.dat");
            fileReader = new FileReader(dataFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<Group> groups = getGroups();
            float avarage = getAvg();
            while((line = bufferedReader.readLine())!=null){
               //read row GROUP_AMOUNT times
               if(count>=GROUP_AMOUNT){
                tempString = line.split(" ");
                Komsu komsu = new Komsu();
                komsu.setCoordinate_x(Integer.parseInt(tempString[0])*GRID);
                komsu.setCoordinate_y(Integer.parseInt(tempString[1])*GRID);
                if(avarage < (komsu.getCoordinate_x()+komsu.getCoordinate_y())){
                    komsu.setGroup(groups.get(1));
                }else{
                    komsu.setGroup(groups.get(0));
                }
                
                valueList.add(komsu);
               }
               
               //one row readed
               count++;
            }
            bufferedReader.close();
            fileReader.close();
        } catch (Exception e) {
            Logger.getLogger(drawingComponent.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return valueList;
    }
    
    public float getAvg(){
        FileReader fileReader = null;
        String line;
        int count=0;
        int x;
        int y;
        float total=0;
        String[] tempString;
        try {
            File dataFile = new File("src/enyakinkomsu/data.dat");
            fileReader = new FileReader(dataFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<Group> groups = getGroups();
            while((line = bufferedReader.readLine())!=null){
               //read row GROUP_AMOUNT times
               if(count>=GROUP_AMOUNT){
                tempString = line.split(" ");
                x = (Integer.parseInt(tempString[0])*GRID);
                y = (Integer.parseInt(tempString[1])*GRID);
                
                total += (x+y);
               }
               
               //one row readed
               count++;
            }
            
            bufferedReader.close();
            fileReader.close();
        } catch (Exception e) {
            Logger.getLogger(drawingComponent.class.getName()).log(Level.SEVERE, null, e);
        }
        if(total!=0){
            return (float)total/(count-GROUP_AMOUNT);
        }else{
            return total;
        }
        
    }
    
}
