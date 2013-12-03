package com.charredgames.tool.tileeditor.graphics;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.charredgames.tool.tileeditor.TileEditor;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 30, 2013
 */
public class TileImage {

	private String path;
	private Image img;
	private JLabel jLabel;
	private int size;
	
	
	public static TileImage blank = new TileImage(TileImage.class.getResource("/test.png"));
	
	public TileImage(Image image){
		this.img = image;
		this.size = TileEditor.getTileSize();
		img = img.getScaledInstance(size, size, Image.SCALE_FAST);
		jLabel = new JLabel(new ImageIcon(img));
	}
	
	public TileImage(URL path){
		this.size = TileEditor.getTileSize();
		try {
			img = ImageIO.read(new File(path.toURI()));
			img = img.getScaledInstance(size, size, Image.SCALE_FAST);
			jLabel = new JLabel(new ImageIcon(img));
		} catch (IOException e) {e.printStackTrace();} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public java.awt.Image getImage(){
		if(size != TileEditor.getTileSize()) {
			size = TileEditor.getTileSize();
			img = img.getScaledInstance(size, size, Image.SCALE_FAST);
		}
		return img;
	}
	
	public JLabel getImageLabel(){
		return jLabel;
	}
}
