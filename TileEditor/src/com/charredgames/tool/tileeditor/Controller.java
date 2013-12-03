package com.charredgames.tool.tileeditor;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import com.charredgames.tool.tileeditor.graphics.Position;
import com.charredgames.tool.tileeditor.graphics.TileImage;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 30, 2013
 */
public class Controller {

	public static ArrayList<Position> gridPositions = new ArrayList<Position>();
	public static ArrayList<Position> spriteSheet = new ArrayList<Position>();
	public static int mouseState = -1;
	
	public static void exportImage(File path, int rows, int cols, int colourOffset){
		BufferedImage img = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
		int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		int pixel = 0;
		ArrayList<TileImage> foundTiles = new ArrayList<TileImage>();
		for(Position pos : Controller.gridPositions){
			if(!foundTiles.contains(pos.getImage())) foundTiles.add(pos.getImage());
			int num = 0xFFFFFFFF + foundTiles.indexOf(pos.getImage()) + 1 + colourOffset;
			pixels[pixel] = num;
			pixel++;
		}
		try {
			ImageIO.write(img, "png", path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void generateData(File path, int colourOffset){
		ArrayList<TileImage> foundTiles = new ArrayList<TileImage>();
		try {
			PrintWriter writer = new PrintWriter(path, "UTF-8");
			for(Position pos : Controller.gridPositions){
				if(!foundTiles.contains(pos.getImage())) {
					foundTiles.add(pos.getImage());
					int num = 0xFFFFFFFF + foundTiles.indexOf(pos.getImage()) + 1 + colourOffset;
					writer.println("public static final Tile tile" + foundTiles.indexOf(pos.getImage()) + " = new Tile(\"0xFF" + String.format("%06d",num) + "\", false);");
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {e.printStackTrace();} catch (UnsupportedEncodingException e) {e.printStackTrace();}
	}
	
	public static void reset(){
		gridPositions.clear();
		spriteSheet.clear();
	}
	
	public static void generateGrid(int rows, int cols){
		gridPositions.clear();
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < cols; c++){
				gridPositions.add(new Position(r, c));
			}
		}
	}
	
	public static void generateSpriteSheet(BufferedImage img, int size){
		int rows = img.getHeight() / size;
		int cols = img.getWidth() / size;
		int c = 0;
		BufferedImage[] images = new BufferedImage[rows * cols];
		for(int x = 0; x < rows; x++){
			for(int y = 0; y < cols; y++){
				images[c] = new BufferedImage(size, size, img.getType());  
				  
                Graphics2D gr = images[c++].createGraphics();  
                gr.drawImage(img, 0, 0, size, size, size * y, size * x, size * y + size, size * x + size, null);  
                gr.dispose();  
                Position pos = new Position(x, y);
                pos.setImage(new TileImage(images[c - 1]));
                spriteSheet.add(pos);
			}
		}
		
		//for(BufferedImage image : images){			
		//	new TileImage(image);
		//}
		
	}
	
	public static Position getGridPos(int row, int col){
		for(Position pos : gridPositions){
			if(pos.getRow() == row && pos.getCol() == col) return pos;
		}
		return gridPositions.get(0);
	}
	
	public static Position getSpritePos(int row, int col){
		for(Position pos : spriteSheet){
			if(pos.getRow() == row && pos.getCol() == col) return pos;
		}
		return spriteSheet.get(0);
	}
	
	public static Component getComponentByName(JComponent compo, String name){
		for(Component comp : compo.getComponents()){
			System.out.println(comp.getName());
			if(comp.getName().equalsIgnoreCase(name)) return comp;
		}
		return null;
	}
	
	public static void drawProximity(int radius, int row, int col){
    	for(int x = row - radius; x < (row + radius); x++){
    		for(int y = col - radius; y < (col + radius); y++){
        		Position pos = Controller.getGridPos(x, y);
        		pos.setImage(TileEditor.selectedPosition.getImage());
        		TileEditor.invalidate();
        		TileEditor.revalidate();
        	}
    	}
    }
	
	public static void fillBucket(TileImage img){
		for(Position pos : gridPositions){
			if(pos.getImage() == img) pos.setImage(TileEditor.selectedPosition.getImage());
		}
		TileEditor.invalidate();
		TileEditor.revalidate();
	}
	
}
