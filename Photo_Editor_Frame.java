import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////


public class Photo_Editor_Frame extends JFrame 
                                implements ActionListener
	{
	Toolkit tk = Toolkit.getDefaultToolkit();
	
	JMenuBar mb;
	JMenu file_menu;
	JMenu edit_menu;
	JMenu enc_sub_menu;
	JMenu dec_sub_menu;
	JLabel label;
	
	private ArrayList<JMenuItem> f_menu_items;
	private ArrayList<JMenuItem> e_menu_items;
	private ArrayList<JMenuItem> enc_sub_menu_items;
	private ArrayList<JMenuItem> dec_sub_menu_items;
	
	private HashMap<String, Integer> file_hash;
	private HashMap<String, Integer> edit_hash;
	private HashMap<String, Integer> enc_hash;
	private HashMap<String, Integer> dec_hash;
	
	private ArrayList<Integer> row_encryption_key;
	private ArrayList<Integer> col_encryption_key;
	
	private BufferedImage original_picture;
	private BufferedImage disp_picture;
	

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	
	public Photo_Editor_Frame()
		{
		mb = new JMenuBar();
		file_menu = new JMenu("File");
		edit_menu = new JMenu("Edit Photo");
		enc_sub_menu = new JMenu("Encryption");
		dec_sub_menu = new JMenu("Decryption");
		f_menu_items = new ArrayList<JMenuItem>();
		e_menu_items = new ArrayList<JMenuItem>();
		enc_sub_menu_items = new ArrayList<JMenuItem>();
		dec_sub_menu_items = new ArrayList<JMenuItem>();
		
		
		label = new JLabel();
		
		file_hash = new HashMap<String, Integer>();
		edit_hash = new HashMap<String, Integer>();
		enc_hash = new HashMap<String, Integer>();
		dec_hash = new HashMap<String, Integer>();
		
		row_encryption_key = new ArrayList<Integer>();
		col_encryption_key = new ArrayList<Integer>();
		
		
		setupMenus();
		setupPhotoFrame();
		}
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	
	private void setupPhotoFrame()
		{
		setLayout(new FlowLayout());
		add(label);
		setTitle("Todd's Photo Editor");
		setJMenuBar(mb);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation((int) tk.getScreenSize().getWidth() / 8, (int) tk.getScreenSize().getHeight() / 8);
		setSize((int) (tk.getScreenSize().getWidth() / 1.5), (int) (tk.getScreenSize().getHeight() / 1.5));
		setVisible(true);	
		}
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	
	private void setupMenus()
		{
		int k;
		
		f_menu_items.add(new JMenuItem("Upload new photo"));
		f_menu_items.add(new JMenuItem("Reload original photo"));
		f_menu_items.add(new JMenuItem("Save photo"));
		f_menu_items.add(new JMenuItem("Exit"));
		
		file_hash.put("UPLOAD", 0);
		file_hash.put("RELOAD", 1);
		file_hash.put("SAVE", 2);
		file_hash.put("EXIT", 3);
		
				
		e_menu_items.add(new JMenuItem("Black and White Overlay"));
		e_menu_items.add(new JMenuItem("Red Overlay"));
		e_menu_items.add(new JMenuItem("Green Overlay"));
		e_menu_items.add(new JMenuItem("Blue Overlay"));
		e_menu_items.add(new JMenuItem("Magenta Overlay"));
		e_menu_items.add(new JMenuItem("Yellow Overlay"));
		e_menu_items.add(new JMenuItem("Cyan Overlay"));
		e_menu_items.add(new JMenuItem("Vertical Reflection"));
		e_menu_items.add(new JMenuItem("Horizontal Reflection"));
		
		
		edit_hash.put("BW", 0);
		edit_hash.put("R", 1);
		edit_hash.put("G", 2);
		edit_hash.put("B", 3);
		edit_hash.put("M", 4);
		edit_hash.put("Y", 5);
		edit_hash.put("C", 6);
		edit_hash.put("V" ,7);
		edit_hash.put("H", 8);
		
		
		enc_sub_menu_items.add(new JMenuItem("Encrypt Photo"));
		enc_sub_menu_items.add(new JMenuItem("Save Decryption Key File"));
		
		enc_hash.put("EP", 0);
		enc_hash.put("SDK", 1);
		
		
		dec_sub_menu_items.add(new JMenuItem("Choose Decryption Key File"));
		dec_sub_menu_items.add(new JMenuItem("Decrypt Photo"));
		
		dec_hash.put("CDK", 0);
		dec_hash.put("DP", 1);
		
		
		
		f_menu_items.get(file_hash.get("RELOAD")).setEnabled(false);
		f_menu_items.get(file_hash.get("SAVE")).setEnabled(false);
		
		for(k=0; k < f_menu_items.size(); k++)
			{
			f_menu_items.get(k).addActionListener(this);
			file_menu.add(f_menu_items.get(k));
			}
		
		for (k=0; k < e_menu_items.size(); k++)
			{
			e_menu_items.get(k).addActionListener(this);
			edit_menu.add(e_menu_items.get(k));
			}
		
		for (k=0; k < enc_sub_menu_items.size(); k++)
			{
			enc_sub_menu_items.get(k).addActionListener(this);
			enc_sub_menu.add(enc_sub_menu_items.get(k));
			}
		
		for (k=0; k < dec_sub_menu_items.size(); k++)
			{
			dec_sub_menu_items.get(k).addActionListener(this);
			dec_sub_menu.add(dec_sub_menu_items.get(k));
			}
		
		enc_sub_menu_items.get(enc_hash.get("SDK")).setEnabled(false);
		
		dec_sub_menu_items.get(dec_hash.get("DP")).setEnabled(false);
		
		
		edit_menu.add(enc_sub_menu);
		edit_menu.add(dec_sub_menu);
		
		edit_menu.setEnabled(false);
		mb.add(file_menu);
		mb.add(edit_menu);
		}
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	
	public void actionPerformed (ActionEvent e)
		{
		if (e.getSource() == f_menu_items.get(file_hash.get("UPLOAD")))
			{
			boolean fileChosen;
			
			fileChosen = chooseImageFile("I");   
			
			if (fileChosen)
				{
				f_menu_items.get(file_hash.get("RELOAD")).setEnabled(true);
				f_menu_items.get(file_hash.get("SAVE")).setEnabled(true);
				edit_menu.setEnabled(true);
				}
			
			row_encryption_key.clear();
			col_encryption_key.clear();
			}
		
		else if (e.getSource() == f_menu_items.get(file_hash.get("RELOAD")))
			{
			label.setIcon(new ImageIcon(original_picture));
			copy(disp_picture, original_picture);
			}
		
		else if (e.getSource() == f_menu_items.get(file_hash.get("EXIT")))
			{
			System.exit(0);
			}
		
		else if (e.getSource() == f_menu_items.get(file_hash.get("SAVE")))
			{
			saveImageFile("I");
			}
		
		else if (e.getSource() == e_menu_items.get(edit_hash.get("BW")))
			{
			//Black and white		
			try
				{
				generateNewImage(disp_picture, "BW");
				label.setIcon(new ImageIcon(disp_picture));
				}
			
			catch (Exception e1)
				{
				JOptionPane.showMessageDialog(null, "Unexpected Error", "Error", JOptionPane.ERROR_MESSAGE);		
				}
			
			
			}
		
		else if (e.getSource() == e_menu_items.get(edit_hash.get("R")))
			{
			//Red
			try
				{
				generateNewImage(disp_picture, "R");
				label.setIcon(new ImageIcon(disp_picture));
				}
		
			catch (Exception e1)
				{
				JOptionPane.showMessageDialog(null, "Unexpected Error", "Error", JOptionPane.ERROR_MESSAGE);		
				}
			}
		
		else if (e.getSource() == e_menu_items.get(edit_hash.get("G")))
			{
			//Green
			try
				{
				generateNewImage(disp_picture, "G");
				label.setIcon(new ImageIcon(disp_picture));
				}
			
			catch (Exception e1)
				{
				JOptionPane.showMessageDialog(null, "Unexpected Error", "Error", JOptionPane.ERROR_MESSAGE);		
				}
		
		
			}

		else if (e.getSource() == e_menu_items.get(edit_hash.get("B")))
			{
			//Blue
			try
				{
				generateNewImage(disp_picture, "B");
				label.setIcon(new ImageIcon(disp_picture));
				}
			
			catch (Exception e1)
				{
				JOptionPane.showMessageDialog(null, "Unexpected Error", "Error", JOptionPane.ERROR_MESSAGE);		
				}
		
		
			}

		else if (e.getSource() == e_menu_items.get(edit_hash.get("M")))
			{
			//Magenta
			try
				{
				generateNewImage(disp_picture, "M");
				label.setIcon(new ImageIcon(disp_picture));
				}
			
			catch (Exception e1)
				{
				JOptionPane.showMessageDialog(null, "Unexpected Error", "Error", JOptionPane.ERROR_MESSAGE);		
				}
		
		
			}

		else if (e.getSource() == e_menu_items.get(edit_hash.get("Y")))
			{
			//Yellow
			try
				{
				generateNewImage(disp_picture, "Y");
				label.setIcon(new ImageIcon(disp_picture));
				}
			
			catch (Exception e1)
				{
				JOptionPane.showMessageDialog(null, "Unexpected Error", "Error", JOptionPane.ERROR_MESSAGE);		
				}
		
		
			}

		else if (e.getSource() == e_menu_items.get(edit_hash.get("C")))
			{
			//Cyan
			try
				{
				generateNewImage(disp_picture, "C");
				label.setIcon(new ImageIcon(disp_picture));
				}
			
			catch (Exception e1)
				{
				JOptionPane.showMessageDialog(null, "Unexpected Error", "Error", JOptionPane.ERROR_MESSAGE);		
				}
		
		
			}
		
		else if (e.getSource() == e_menu_items.get(edit_hash.get("V")))
			{
			//Vertical Reflection
			
			try
				{
				reflectImageVertically(disp_picture);
				label.setIcon(new ImageIcon(disp_picture));
				}
			
			catch (Exception e1)
				{
				JOptionPane.showMessageDialog(null,  "Vertical reflection failed.", "Editing Error", JOptionPane.ERROR_MESSAGE);	
				}
			}
		
		else if (e.getSource() == e_menu_items.get(edit_hash.get("H")))
			{
			//Horizontal Reflection
			
			try
				{
				reflectImageHorizontally(disp_picture);
				label.setIcon(new ImageIcon(disp_picture));	
				}
			
			catch (Exception e1)
				{
				JOptionPane.showMessageDialog(null,  "Horizontal reflection failed.", "Editing Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		
		else if (e.getSource() == enc_sub_menu_items.get(enc_hash.get("EP")))
			{
			//Encryption of photo
			
			try
				{
				encryptPhoto(disp_picture);
				label.setIcon(new ImageIcon(disp_picture));
				enc_sub_menu_items.get(enc_hash.get("EP")).setEnabled(false);
				}
			
			catch(Exception e1)
				{
				JOptionPane.showMessageDialog(null,  "Encryption failed.", "Encryption Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		
		else if (e.getSource() == enc_sub_menu_items.get(enc_hash.get("SDK")))
			{
			//Save Decryption Key
			saveImageFile("D");
			}
		
		else if (e.getSource() == dec_sub_menu_items.get(dec_hash.get("DP")))
			{
			//Decryption of photo	
			
			try
				{
				decryptPhoto(disp_picture);
				label.setIcon(new ImageIcon(disp_picture));
				enc_sub_menu_items.get(enc_hash.get("EP")).setEnabled(true);
				row_encryption_key.clear();
				col_encryption_key.clear();
				}
			
			catch(Exception e1)
				{
				JOptionPane.showMessageDialog(null, "Decryption failed.", "Decryption Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		     
	
		else if (e.getSource() == dec_sub_menu_items.get(dec_hash.get("CDK")))
			{
			//Choose decryption key
			chooseImageFile("D");
			}
		
		if (e.getSource() == enc_sub_menu_items.get(enc_hash.get("EP")) || e.getSource() == enc_sub_menu_items.get(enc_hash.get("SDK")) )
			enc_sub_menu_items.get(enc_hash.get("SDK")).setEnabled(true);
		
		else 
			enc_sub_menu_items.get(enc_hash.get("SDK")).setEnabled(false);				
		
		if (!row_encryption_key.isEmpty() && !col_encryption_key.isEmpty())
			dec_sub_menu_items.get(dec_hash.get("DP")).setEnabled(true);
		
		
		else
			dec_sub_menu_items.get(dec_hash.get("DP")).setEnabled(false);
		
		
}
			
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	
	private boolean chooseImageFile(String fileType)
		{
		int result;
		int row_enc_size;
		int col_enc_size;
		int k;
		JFileChooser fc;
		boolean r;//return variable
		String ext;
		
		r = false;                     

		
		fc = new JFileChooser();
		
		//fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		result = fc.showOpenDialog(null); //get file name from user
		
		
		if (result == JFileChooser.APPROVE_OPTION) //if user has chosen a file name
			{
			ext = new String(fc.getSelectedFile().getName().substring(fc.getSelectedFile().getName().lastIndexOf(".")));
			//get the extension of the file
			
			try
				{
				if (fileType.equals("I"))
					{
					if (!(ext.equalsIgnoreCase(".jpg") || ext.equalsIgnoreCase(".png") || ext.equalsIgnoreCase(".bmp") || ext.equalsIgnoreCase("gif")))
						throw new Exception();
					//throws an error if not the right file type
					
					original_picture = ImageIO.read(fc.getSelectedFile());
					disp_picture = ImageIO.read(fc.getSelectedFile()); //update the original and display pictures	
					label.setIcon(new ImageIcon(original_picture));
					r = true;
					}
				
				else if (fileType.equals("D"))
					{
					if (!ext.equalsIgnoreCase(".dec"))
						throw new Exception();
					
					BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
					
					String input;
					
					input = br.readLine();
					
					row_enc_size = Integer.parseInt(input);
					
					for (k=0; k < row_enc_size; k++)
						{
						input = br.readLine();
						row_encryption_key.add(Integer.parseInt(input));
						}
					
					input = br.readLine();
					
					col_enc_size = Integer.parseInt(input);
					
					for (k=0; k < col_enc_size; k++)
						{
						input = br.readLine();
						col_encryption_key.add(Integer.parseInt(input));
						}
					
					
					if (!(row_enc_size == disp_picture.getWidth() && col_enc_size == disp_picture.getHeight()))
						{
						row_encryption_key.clear();
						col_encryption_key.clear();
						
						throw new Exception("Wrong key");
						}
					}
				}
			
			catch (Exception e)
				{
				if (fileType.equals("I"))
					JOptionPane.showMessageDialog(null, "File selected must have extension .jpg, .png, .bmp, or .gif.", "Error", JOptionPane.ERROR_MESSAGE);
				
				else if (fileType.equals("D") && e.getMessage() == null)
					JOptionPane.showMessageDialog(null, "File selected must have extension .dec.", "Error", JOptionPane.ERROR_MESSAGE);
				
				else if (fileType.equals("D") && e.getMessage().equals("Wrong key"))
					{
					JOptionPane.showMessageDialog(null,  "Wrong decryption key chosen.", "Decryption Key Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}	
		
		return r;
		}
	
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////

	private void saveImageFile(String fileType)
		{
		//I = Image file type
		//D = Decryption key file type    
		int result;
		String saveFilePath;
		File saveFile;
		String ext;
		ext = null;
		saveFile = null;
		
		JFileChooser fc = new JFileChooser();
		
		int k;
		
		result = fc.showSaveDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) //if the user has entered the name of a file
			{
			saveFilePath = fc.getSelectedFile().getAbsolutePath().trim(); //store file name (with no white space) in saveFilePath
			
			if (saveFilePath.contains("."))//get the extension of the file if it exists
				ext = new String(fc.getSelectedFile().getName().substring(fc.getSelectedFile().getName().lastIndexOf(".")));
			
			try
				{
				if (saveFilePath.isEmpty()) //skip code if the user only entered white space
						throw new Exception();
				
				
				
				
				else if (fileType.equals("I") && (!(ext.equals(".jpg") || ext.equals("png") || ext.equals(".bmp") || ext.equals(".gif"))))
						{
						saveFilePath = new String(saveFilePath + ".jpg"); //appends .jpg extension if none is found
						ext = new String(".jpg");
						}
				
				
				else if (fileType.equals("D") && !ext.equals(".dec"))
					{
					saveFilePath = new String(saveFilePath + ".dec"); //appends .dec extension if none is found
					ext= new String(".dec");
					}
				
				saveFile = new File(saveFilePath); //creates file from the user given file name
				
				
				
				if (saveFile.exists()) //makes sure user wants to overwrite file if it exists
					{
					k = JOptionPane.showConfirmDialog(null, "Are you sure you want to overwrite this file?", null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (k == JOptionPane.CANCEL_OPTION || k == JOptionPane.NO_OPTION)
						throw new Exception();
					
					}
				
				
				if (fileType.equals("I"))
					{
					ext = new String(ext.substring(1)); // pop off the '.' to fit ImageIO.write syntax
					ImageIO.write(disp_picture, ext, new File(saveFilePath)); //write file to 
					
					original_picture = ImageIO.read(new File (saveFilePath)); 
					}
				
				
				else if (fileType.equals("D"))
					{
					FileWriter fw = new FileWriter(saveFile);
					
					
					fw.write(String.valueOf(row_encryption_key.size()) + "\n");
					
					while (!row_encryption_key.isEmpty())
						{
						fw.write(new String(row_encryption_key.get(0).toString() + "\n"));
						row_encryption_key.remove(0);
						}
					
					fw.write(String.valueOf(col_encryption_key.size()) + "\n");
					
					
					
					while (!col_encryption_key.isEmpty())
						{
						fw.write(new String(col_encryption_key.get(0).toString() + "\n"));
						col_encryption_key.remove(0);
						}
					
					fw.close();
					}
				}
			
			catch(Exception e)
				{
				JOptionPane.showMessageDialog(null,  "File was not saved", null, JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////

	public void generateNewImage(BufferedImage disp_picture, String arg)
		{
		Color c;
		int R;
		int G;
		int B;
		int A;
		int nc; //new color value 
		
		int j;
		int k;

		for (j=disp_picture.getMinX(); j < disp_picture.getWidth(); j++)
			{
			for(k=disp_picture.getMinY(); k < disp_picture.getHeight(); k++)
				{
				c = new Color(disp_picture.getRGB(j, k));
				R = c.getRed();
				G = c.getGreen();
				B = c.getBlue();
				A = c.getAlpha();
				
				if (arg.equals("BW"))
					R = G = B = (R + G + B) / 3;

				
				else if (arg.equals("R"))
					G = B = 0;
				
					
				else if(arg.equals("G"))
					R = B = 0;
				
					
				else if (arg.equals("B"))
					R = G = 0;

				
				else if (arg.equals("M"))
					G = 0;
					
				else if (arg.equals("Y"))
					B = 0;
					
				else if (arg.equals ("C"))
					R = 0;
				
				nc = (A - 256)*(256*256*256) + R*(256*256) + G*256 + B;
				
				disp_picture.setRGB(j, k, nc);
				}
			}
			
		}
	
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	
	public void reflectImageVertically(BufferedImage disp_picture)
		{
		int j;
		int k;
		
		int m;
		int n;
		
		int w;
		int h;
		
		w = disp_picture.getWidth();
		h = disp_picture.getHeight();
		
		for (j = disp_picture.getMinX(); j < w; j++)
			{
			for (k = disp_picture.getMinY(); k < h / 2; k++)
				{
				m = disp_picture.getRGB(j,k);
				n = disp_picture.getRGB(j, h - 1 - k);
				
				disp_picture.setRGB(j, k, n);
				disp_picture.setRGB(j, h - 1 - k, m);
				}
			}
		}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////

	public void reflectImageHorizontally(BufferedImage disp_picture)
		{
		int j;
		int k;
		
		int m;
		int n;
		
		int w;
		int h;
		
		w = disp_picture.getWidth();
		h = disp_picture.getHeight();
		
		for (k = disp_picture.getMinY(); k < h; k++)
			{
			for (j = disp_picture.getMinX(); j < w / 2; j++)
				{
				m = disp_picture.getRGB(j,k);
				n = disp_picture.getRGB(w - 1 - j, k);
				
				disp_picture.setRGB(j, k, n);
				disp_picture.setRGB(w - 1 - j, k, m);
				}
			}
		}
	
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////

public void encryptPhoto(BufferedImage disp_picture)
	{
	int j;
	int k;
	
	int w;
	int h;
	
	int x;
	int y;
	
	int s;//temp variables
	int t;
	
	int c;

	Random r;
	
	r = new Random();
	w = disp_picture.getWidth();
	h = disp_picture.getHeight();  
	
	x = disp_picture.getMinX();
	y = disp_picture.getMinY();
	
	row_encryption_key.clear();
	col_encryption_key.clear();

	//generate encryption keys for row/cols
	
	for (j=x; j < w; j++)
		row_encryption_key.add(r.nextInt(w));
	
	for (j=y; j < h; j++)
		col_encryption_key.add(r.nextInt(h));
	
	//scramble each row

		for (k = y; k < h; k++)
			{
			for (j = x; j < w; j++)
				{
				s = disp_picture.getRGB(j, k);
				t = disp_picture.getRGB(row_encryption_key.get(j), k);
				
				disp_picture.setRGB(j, k, t);
				disp_picture.setRGB(row_encryption_key.get(j), k, s);
				}
			}
				
		//scramble each column
		for (j = x; j < w; j++)
			{
			for (k=y; k < h; k++)
				{
				s = disp_picture.getRGB(j, k);
				t = disp_picture.getRGB(j, col_encryption_key.get(k));
				
				disp_picture.setRGB(j,  k,  t);
				disp_picture.setRGB(j,  col_encryption_key.get(k),  s);	
				}
			}	
	}
	
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////

public void decryptPhoto(BufferedImage disp_picture)
	{
	int j;
	int k;
	
	int w;
	int h;
	
	int x;
	int y;
	
	int s;//temp variables
	int t;
	
	w = disp_picture.getWidth();
	h = disp_picture.getHeight();  
	
	x = disp_picture.getMinX();
	y = disp_picture.getMinY();
	
	//create decryption key in encryption key (from encryption key)
	
	/*
	for (j=x; j < w / 2; j++)
		{
		s = row_encryption_key.get(j);
		t = row_encryption_key.get(w-1-j);
		
		row_encryption_key.set(j, t);
		row_encryption_key.set(w-1-j, s);
		}
	
	for (j = y; j < h / 2; j++)
		{
		s = col_encryption_key.get(j);
		t = col_encryption_key.get(h-1-j);
		
		col_encryption_key.set(j, t);
		col_encryption_key.set(h-1-j, s);
		}
	*/

	//scramble each column
	
	for (j = w-1; j >= x; j--)
		{
		for (k=h-1; k >= y; k--)
			{
			s = disp_picture.getRGB(j, k);
			t = disp_picture.getRGB(j, col_encryption_key.get(k));
			
			disp_picture.setRGB(j,  k,  t);
			disp_picture.setRGB(j,  col_encryption_key.get(k),  s);	
			}
		}	
	
	
	//scramble each row
	
	for (k = h-1; k >= y; k--)
		{
		for (j = w-1; j >= x; j--)
			{
			s = disp_picture.getRGB(j, k);
			t = disp_picture.getRGB(row_encryption_key.get(j), k);
			
			disp_picture.setRGB(j, k, t);
			disp_picture.setRGB(row_encryption_key.get(j), k, s);
			}
		}
	}	

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////


public void copy(BufferedImage disp_picture, BufferedImage original_picture)
	{
	int j;
	int k;
	
	int w;
	int h;
	
	w = disp_picture.getWidth();
	h = disp_picture.getHeight();
	
	for (j = disp_picture.getMinX(); j < w; j++)
		{
		for (k = disp_picture.getMinY(); k < h; k++)
			{
			disp_picture.setRGB(j, k, original_picture.getRGB(j, k));
			}
		}
	}
}
