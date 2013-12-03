package com.charredgames.tool.tileeditor.graphics;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.charredgames.tool.tileeditor.Controller;
import com.charredgames.tool.tileeditor.TileEditor;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 29, 2013
 */
public class NewProjectPanel extends JFrame implements ActionListener{

	int width = 400, height = 300;
	private JFileChooser fc = new JFileChooser();
	private TileImage gameImage = null;
	private JPanel gridPanel, tilePanel, uploadPanel;
	private JSpinner rows, cols, size, scale, offset;
	
	public NewProjectPanel(){
		setPreferredSize(new Dimension(width, height));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		gridPanel = getGridPanel();
		tilePanel = getTilePanel();
		uploadPanel = getUploads();
		
		c.insets = new Insets(10, 0, 0, 0);
		c.gridx = 1;
		c.weightx = 1.0;
		c.gridy = 1;
		add(gridPanel, c);
		c.gridx = 1;
		c.weightx = 0.0;
		c.gridy = 2;
		c.insets = new Insets(20, 0, 0, 0);
		add(tilePanel, c);
		c.gridx = 1;
		c.weightx = 0.0;
		c.gridy = 3;
		c.insets = new Insets(20, 0, 0, 0);
		add(uploadPanel, c);
		c.gridy = 4;
		add(getSubmitButton(), c);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("CharredGames TileEditor: New Project");
		
		fc.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("BROWSE")) {
			
			int returnVal = 0;
			returnVal = fc.showOpenDialog(NewProjectPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
			}
		}
		else if(e.getActionCommand().equalsIgnoreCase("CREATE")){
			if(fc.getSelectedFile() == null) return;
			BufferedImage img;
			TileEditor.invalidate();
			TileEditor.map = TileEditor.newMap((Integer)rows.getValue(), (Integer) cols.getValue(), (Integer) size.getValue(), (Integer) scale.getValue(), (Integer) offset.getValue());
			try {
				img = ImageIO.read(fc.getSelectedFile().getAbsoluteFile());
				Controller.generateSpriteSheet(img, (Integer) size.getValue());
				TileEditor.setSpritePanel(Controller.spriteSheet);
			} catch (IOException e1) {e1.printStackTrace();}
			TileEditor.revalidate();
			setVisible(false);
		}
	}
	
	public void toggle(boolean state){
		setVisible(state);
	}
	
	public JPanel getSubmitButton(){
		JPanel panel = new JPanel();
		
		JButton browse = new JButton("Create");
		browse.setActionCommand("CREATE");
		browse.addActionListener(this);
		panel.add(browse);
		
		return panel;
	}
	
	public JPanel getGridPanel(){
		JPanel panel = new JPanel();
		JLabel label;
		TitledBorder border = new TitledBorder("Grid Settings");
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(0, 20, 5, 5);
		c.gridx = 2;
		panel.add(new JLabel("rows:"), c);
		c.insets = new Insets(0, 5, 5, 15);
		c.gridx = 3;
		rows = new JSpinner(new SpinnerNumberModel(32, 1, 256, 1));
		panel.add(rows, c);
		c.insets = new Insets(0, 30, 5, 5);
		c.gridx = 4;
		panel.add(new JLabel("cols:"), c);
		c.insets = new Insets(0, 5, 5, 15);
		c.gridx = 5;
		cols = new JSpinner(new SpinnerNumberModel(32, 1, 1000, 1));
		panel.add(cols, c);
		
		panel.setBorder(border);
		
		return panel;
	}
	
	public JPanel getTilePanel(){
		JPanel panel = new JPanel();
		JSpinner spinner;
		TitledBorder border = new TitledBorder("Tile Settings");
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(0, 20, 5, 5);
		c.gridx = 2;
		panel.add(new JLabel("size:"), c);
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 3;
		size = new JSpinner(new SpinnerNumberModel(16, 1, 256, 1));
		panel.add(size, c);
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 4;
		panel.add(new JLabel("scale:"), c);
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 5;
		scale = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
		panel.add(scale, c);
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 6;
		panel.add(new JLabel("offset:"), c);
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 7;
		offset = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
		panel.add(offset, c);
		
		panel.setBorder(border);
		
		return panel;
	}
	
	public JPanel getUploads(){
		JPanel panel = new JPanel();
		TitledBorder border = new TitledBorder("SpriteSheet");
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(0, 20, 5, 5);
		c.gridy = 1;
		panel.add(new JTextField(18), c);
		c.gridx = 4;
		//c.insets = new Insets(0, 20, 5, 5);
		JButton browse = new JButton("Browse");
		browse.setActionCommand("BROWSE");
		browse.addActionListener(this);
		panel.add(browse, c);
		
		panel.setBorder(border);
		
		return panel;
	}
}
