package enyakinkomsu;

import java.awt.Color;

public class Group {
    private String groupName;
    private Color groupColor = Color.RED;

    public String getGroupName() {
        return groupName;
    }

    public Color getGroupColor() {
        return groupColor;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public void setGroupColor(Color groupColor){
        this.groupColor = groupColor;
    }
}
