package com.charredgames.tool.tileeditor.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import com.charredgames.tool.tileeditor.Controller;
import com.charredgames.tool.tileeditor.TileEditor;
import com.charredgames.tool.tileeditor.Tool;

public class CellPane extends JPanel {

    private final int row, col;
    private final boolean map;
    public TileImage current = TileImage.blank, previous, temp = TileImage.blank;
    
    public CellPane(boolean m, int r, int c) {
    	this.map = m;
    	this.row = r;
    	this.col = c;
        addMouseListener(new MouseAdapter() {
        	public void mouseReleased(MouseEvent e){
        		Controller.mouseState = -1;
        	}
        	public void mousePressed(MouseEvent e){
        		Controller.mouseState = e.getButton();
        		if(!map){
        			TileEditor.selectedPosition = Controller.getSpritePos(row, col);
        			return;
        		}
        		Position pos = Controller.getGridPos(row, col);
        		if(TileEditor.tool == Tool.BUCKET) {
        			Controller.fillBucket(pos.getImage());
        			return;
        		}
            	current = TileEditor.selectedPosition.getImage();
            	temp = current;
            	pos.setImage(current);
            	if(TileEditor.tool == Tool.DUAL) Controller.drawProximity(1, row, col); 
                repaint();
        	}
            public void mouseEntered(MouseEvent e) {
            	if(!map || TileEditor.tool == Tool.BUCKET) return;
            	Position pos = Controller.getGridPos(row, col);
            	if(Controller.mouseState != -1){
            		current = TileEditor.selectedPosition.getImage();
                	temp = current;
                	if(TileEditor.tool == Tool.DUAL) Controller.drawProximity(1, row, col); 
            	}else{
	            	temp = pos.getImage();
	            	current = TileEditor.selectedPosition.getImage();
            	}
            	pos.setImage(current);
                repaint();
            }

            public void mouseExited(MouseEvent e) {
            	if(!map || TileEditor.tool == Tool.BUCKET) return;
            	Position pos = Controller.getGridPos(row, col);
            	current = temp;
            	temp = pos.getImage();
            	pos.setImage(current);
                repaint();
            }
        });
    }

    public Dimension getPreferredSize() {
        return new Dimension(TileEditor.getTileSize(), TileEditor.getTileSize());
    }

    public void paintComponent(Graphics g){
    	Image img;
    	if(map) img = Controller.getGridPos(row, col).getImage().getImage();
    	else img = Controller.getSpritePos(row, col).getImage().getImage();
    	if((!map) && row == TileEditor.selectedPosition.getRow() && col == TileEditor.selectedPosition.getCol()){
    		setBorder(new MatteBorder(2, 2, 2, 2, Color.RED));
    	}
    	g.drawImage(img, 0, 0, null);
    }
    
    public int getRow(){
    	return row;
    }
    
    public int getCol(){
    	return col;
    }
    
}