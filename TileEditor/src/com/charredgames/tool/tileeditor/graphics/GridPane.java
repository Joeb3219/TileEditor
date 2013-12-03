package com.charredgames.tool.tileeditor.graphics;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class GridPane extends JPanel {

	int rows, cols = 5;
	public CellPane cellPane;
	public ArrayList<CellPane> cellPanes = new ArrayList<CellPane>();
	
    public GridPane(boolean map, int rows, int cols) {
    	this.rows = rows;
    	this.cols = cols;
    	
	    setLayout(new GridBagLayout());
	
	    GridBagConstraints c = new GridBagConstraints();
	    for(int row = 0; row < rows; row++){
	    	for(int col = 0; col < cols; col++){
	        	c.gridx = col;
	            c.gridy = row;
	
	            cellPane = new CellPane(map, row, col);
	            cellPanes.add(cellPane);
	            Border border = null;
	            if(row < (rows - 1)){
	            	if(col < (cols - 1)){
	                	border = new MatteBorder(1, 1, 0, 0, Color.BLACK);
	                } else {
	                	border = new MatteBorder(1, 1, 0, 1, Color.BLACK);
	                }
	            }else{
	            	if(col < (cols - 1)) border = new MatteBorder(1, 1, 1, 0, Color.BLACK);
	            	else border = new MatteBorder(1, 1, 1, 1, Color.BLACK);
	            }
	            cellPane.setBorder(border);
	            add(cellPane, c);
	    	}
    	}
	}
}