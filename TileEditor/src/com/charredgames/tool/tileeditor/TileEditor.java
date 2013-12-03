package com.charredgames.tool.tileeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.charredgames.tool.tileeditor.graphics.CellPane;
import com.charredgames.tool.tileeditor.graphics.GridPane;
import com.charredgames.tool.tileeditor.graphics.NewProjectPanel;
import com.charredgames.tool.tileeditor.graphics.Position;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 24, 2013
 */
public class TileEditor implements ActionListener{

	public static final int _WIDTH = 700;
	public static final int _HEIGHT = 600;
	public static int _SIZE = 16, _SCALE = 3;
	public static int rowCount, colCount;
	public static int colourOffset;
	
	public static JFrame window;
	private static JMenuBar menu;
	public static JScrollPane map, spritePanel = null;
	private static GridBagConstraints c;
	
	public static Position selectedPosition = new Position(0, 0);
	public static Tool tool = Tool.SINGLE;
	final JFileChooser fc = new JFileChooser();
	
	public static JScrollPane newMap(int rows, int cols, int size, int scale, int offset){
		colourOffset = offset;
		rowCount = rows;
		colCount = cols;
		_SIZE = size;
		_SCALE = scale;
		Controller.reset();
		Controller.generateGrid(rows, cols);
		JScrollPane scrollPane = new JScrollPane();
		
		GridPane grid = new GridPane(true, rows, cols);
		scrollPane.setPreferredSize(grid.getMinimumSize());
		scrollPane.setViewportView(grid);
		
		return scrollPane;
	}
	
	public static void invalidate(){
		window.remove(map);
		if(spritePanel != null) window.remove(spritePanel);
		window.invalidate();
		window.validate();
		window.repaint();
	}
	
	public static void revalidate(){
		c.gridx = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 1.0;
		c.weightx = 1.0;
		window.add(map, c);
		c.gridx = 1;
		//c.ipadx = -128;
		c.anchor = GridBagConstraints.PAGE_END;
		c.weightx = 0;
		c.weighty = 1.0;
		if(spritePanel != null) window.add(spritePanel, c);
		window.invalidate();
		window.validate();
		window.repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("MENU_FILE_NEW")) new NewProjectPanel();
		else if(e.getActionCommand().equalsIgnoreCase(Tool.SINGLE.componentName)) tool = Tool.SINGLE;
		else if(e.getActionCommand().equalsIgnoreCase(Tool.DUAL.componentName)) tool = Tool.DUAL;
		else if(e.getActionCommand().equalsIgnoreCase(Tool.BUCKET.componentName)) tool = Tool.BUCKET;
		else if(e.getActionCommand().equalsIgnoreCase(Tool.ERASER.componentName)) tool = Tool.ERASER;
		else if(e.getActionCommand().equalsIgnoreCase("MENU_FILE_EXPORT")) {
			int returnVal = fc.showSaveDialog(window);
			if(returnVal == JFileChooser.APPROVE_OPTION) Controller.exportImage(fc.getSelectedFile().getAbsoluteFile(), rowCount, colCount, colourOffset);
		}
		else if(e.getActionCommand().equalsIgnoreCase("MENU_FILE_GENERATE")){
			int returnVal = fc.showSaveDialog(window);
			if(returnVal == JFileChooser.APPROVE_OPTION) Controller.generateData(fc.getSelectedFile().getAbsoluteFile(), colourOffset); 
		}
	}
	
	
	private JMenuBar getMenuBar(){
		JMenuBar menuBar;
		JMenu menu, subMenu;
		JMenuItem menuItem;
		menuBar = new JMenuBar();
		
		menu = new JMenu("File");
		menuItem = new JMenuItem("New");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("New");
		menuItem.setActionCommand("MENU_FILE_NEW");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("Save");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Save");
		menuItem.setActionCommand("MENU_FILE_SAVE");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem("Open");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Open");
		menuItem.setActionCommand("MENU_FILE_OPEN");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem("Export");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Export");
		menuItem.setActionCommand("MENU_FILE_EXPORT");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem("Generate Data");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Generate Data");
		menuItem.setActionCommand("MENU_FILE_GENERATE");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);

		menu = new JMenu("Tools");
		menuItem = new JMenuItem(Tool.SINGLE.displayName);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Single Placement");
		menuItem.setActionCommand(Tool.SINGLE.componentName);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem(Tool.DUAL.displayName);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Dual Placement");
		menuItem.setActionCommand(Tool.DUAL.componentName);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem(Tool.BUCKET.displayName);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Bucket");
		menuItem.setActionCommand(Tool.BUCKET.componentName);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem(Tool.ERASER.displayName);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Eraser");
		menuItem.setActionCommand(Tool.ERASER.componentName);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);
		
		return menuBar;
	}
	
	
	public TileEditor(){
		
	}
	
	
	public static void setSpritePanel(ArrayList<Position> positions){
		//invalidate();
		spritePanel = getSpritePanel(positions);
		//revalidate();
	}
	
	private static JScrollPane getSpritePanel(ArrayList<Position> positions){
		spritePanel = new JScrollPane();
		
		int highestRow = positions.get(positions.size() - 1).getRow();
		int highestCol = positions.get(positions.size() - 1).getCol();
		
		GridPane grid = new GridPane(false, highestRow, highestCol);
		spritePanel.setPreferredSize(grid.getMinimumSize());
		
		int i = 0;
		for(CellPane pane : grid.cellPanes){
			i++;
			Controller.getSpritePos(pane.getRow(), pane.getCol()).setImage(positions.get(i).getImage());
		}
		
		selectedPosition = Controller.getSpritePos(0, 0);
		spritePanel.setViewportView(grid);
		
		return spritePanel;
	}
	
	public static void main(String[] args){
		
		TileEditor editor = new TileEditor();
		window = new JFrame();
		window.setPreferredSize(new Dimension(_WIDTH, _HEIGHT));
		Controller.reset();
		window.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		editor.menu = editor.getMenuBar();
		window.setJMenuBar(editor.menu);
		map = newMap(0, 0, 16, 3, 0);
		window.add(map);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.setResizable(false);
		window.setTitle("TileEditor");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			File file = new File(TileEditor.class.getResource("/icon.png").toURI());
			window.setIconImages(null);
			List<Image> imageList = new ArrayList<Image>();
			imageList.add(ImageIO.read(file));
			window.setIconImages(imageList);
		} catch (IOException e) {e.printStackTrace();} catch (URISyntaxException e) {e.printStackTrace();}
	}

	public static int getTileSize(){
		return _SCALE * _SIZE;
	}
	
}
