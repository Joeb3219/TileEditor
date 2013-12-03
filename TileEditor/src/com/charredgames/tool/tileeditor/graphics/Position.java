package com.charredgames.tool.tileeditor.graphics;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 30, 2013
 */
public class Position {

	private int row, col;
	private TileImage img = TileImage.blank;
	
	public Position(int row, int col){
		this.row = row;
		this.col = col;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	public void setImage(TileImage image){
		this.img = image;
	}
	
	public TileImage getImage(){
		return img;
	}
	
}
