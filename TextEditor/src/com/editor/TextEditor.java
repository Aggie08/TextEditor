package com.editor;

/** 
 * Unnamed Text Editor
 * Author: Fred Douglas Webb III
 * Company: Webb Studios
 * Date: 4/10/2015
 * 
 * Future additions
 * 	- Editor Name
 * 	- Multiple File views(Files represented as tabs)
 * 	- Update UI
 * 	- Themes
 * 	- Update tool bar pictures
 * 	- Finish New file function
 * 
 */


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.text.DefaultEditorKit;

public class TextEditor extends JFrame{

	private static final long serialVersionUID = 1L;	//Program Version
	private JTextArea area = new JTextArea(30,125);	//Area where the text will go
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));	//Navigation box
	private String currentFile = "Untitled";	//File name
	private boolean changed = false;	//Boolean to determine if user has created text
	
	public TextEditor() {
		
		/**
		 * Setting up the JFrame
		 * and JFrame objects
		 */
		area.setFont(new Font("Monospaced",Font.PLAIN,12));
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu help = new JMenu("Help");
		JToolBar toolBar = new JToolBar();
		
		/**
		 * Creating Menu options
		 */
		add(scroll,BorderLayout.CENTER);
		//File Menu Options
		file.add(New);
		file.add(Open);
		file.add(Save);
		file.add(SaveAs);
		file.add(Quit);
		file.setMnemonic(KeyEvent.VK_F);	//Add keyboard shortcut
		//file.addSeparator();
		menuBar.add(file); 
		
		for(int i=0; i<4; i++){
			file.getItem(i).setIcon(null);
		}
		//Edit Menu Options
		edit.add(Cut);
		edit.add(Copy);
		edit.add(Paste);
		edit.getItem(0).setText("Cut");
		edit.getItem(1).setText("Copy");
		edit.getItem(2).setText("Paste");
		edit.setMnemonic(KeyEvent.VK_E);	//Add keyboard shortcut
		menuBar.add(edit);
		//Help Menu Options
		help.add(About);
		help.getItem(0).setText("About");
		menuBar.add(help);
		
		/**
		 * Creating Toolbar
		 */
		toolBar.add(New);
		toolBar.add(Open);
		toolBar.add(Save);
		toolBar.addSeparator();
		
		//Add Buttons to toolbar
		JButton cut = toolBar.add(Cut);
		JButton copy = toolBar.add(Copy);
		JButton paste = toolBar.add(Paste);
		//Setting up Buttons
		cut.setText(null); 
		cut.setIcon(new ImageIcon("res/cut.png"));
		copy.setText(null); 
		copy.setIcon(new ImageIcon("res/copy.png"));
		paste.setText(null); 
		paste.setIcon(new ImageIcon("res/paste.png"));
		
		/**
		 * Disable the saving functionality
		 * as no change has happened yet
		 */
		Save.setEnabled(false);
		SaveAs.setEnabled(false);
		
		/**
		 * Final JFrame Setup
		 */
		pack();
		setJMenuBar(menuBar);	//Add the MenuBar
		add(toolBar,BorderLayout.NORTH);	//Add the tool bar
		setDefaultCloseOperation(EXIT_ON_CLOSE);	//Terminate when closed
		setLocationRelativeTo(null);	//Center the screen
		area.addKeyListener(keyListener);	//Add a key listener to the textArea
		setTitle(currentFile);	//Title of the Editor
		setVisible(true);	//Display the JFrame
	}
	
	/**
	 * If the user inputs a key
	 * enable Save and Save As function
	 */
	private KeyListener keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			changed = true;
			setTitle("*" + currentFile);
			Save.setEnabled(true);
			SaveAs.setEnabled(true);
		}
	};
	
	/**
	 * Setting up Actions
	 */
	ActionMap m = area.getActionMap();
	Action Cut = m.get(DefaultEditorKit.cutAction);
	Action Copy = m.get(DefaultEditorKit.copyAction);
	Action Paste = m.get(DefaultEditorKit.pasteAction);
	//Create a new file
	Action New = new AbstractAction("New", new ImageIcon("res/new.png")) {
		public void actionPerformed(ActionEvent e) {
			//TODO: Add new file functionality
			JOptionPane.showMessageDialog(null, "Under Construction");
		}
	};
	//Open a file
	Action Open = new AbstractAction("Open", new ImageIcon("res/open.png")) {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
				readInFile(dialog.getSelectedFile().getAbsolutePath());
			}
			SaveAs.setEnabled(true);
		}
	};
	//Save the current file
	Action Save = new AbstractAction("Save", new ImageIcon("res/save.png")) {
		public void actionPerformed(ActionEvent e) {
			if(!currentFile.equals("Untitled"))
				saveFile(currentFile);
			else
				saveFileAs();
		}
	};
	//Save the curent file for the first time
	Action SaveAs = new AbstractAction("Save as...") {
		public void actionPerformed(ActionEvent e) {
			saveFileAs();
		}
	};
	//Quit the Editor
	Action Quit = new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			System.exit(0);
		}
	};
	//Displays Editor Information
	Action About = new AbstractAction("About") {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Name: Unnamed\nCreator: Fred Webb III\nCompany: Webb Studios\nVersion: "+ serialVersionUID,"About",JOptionPane.INFORMATION_MESSAGE);
		}
	};
	
	
	/**
	 * Methods
	 */
	private void saveFile(String fileName) {
		try {
			FileWriter w = new FileWriter(fileName);
			area.write(w);
			w.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
			Save.setEnabled(false);
		}
		catch(IOException e) {
		}
	}
	//Save file
	private void saveFileAs() {
		if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
			saveFile(dialog.getSelectedFile().getAbsolutePath());
	}
	//Check if the user wants to save file, then save
	private void saveOld() {
		if(changed) {
			if(JOptionPane.showConfirmDialog(this, "Would you like to save "+ currentFile +"?","Save",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION)
				saveFile(currentFile);
		}
	}
	//Open the contents of the selected file into the textarea
	private void readInFile(String fileName) {
		try {
			FileReader r = new FileReader(fileName);
			area.read(r,null);
			r.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
		}
		catch(IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this,"Editor can't find the file called " + fileName);
		}
	}
	//Start program
	public  static void main(String[] arg) {
		new TextEditor();
	}

}
