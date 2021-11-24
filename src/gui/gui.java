package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FilenameUtils;

import core.AES;
import utils.Formating;

public class gui {

	private JFrame frame;
	private JTextField txtKey;
	private JSeparator separator;
	private JButton btnGenerate;
	private JButton btnEncrypt;
	private JButton btnDecrypt;
	private JLabel lblNewLabel_1;
	private JButton btnUpload;
	private JLabel lblNewLabel_2;
	private JTextField txtFilePath;
	private JSeparator separator_1;
	private JLabel lblEncCompleted;
	private JButton btnSaveEncrypted;

	private byte[] fileBytes;
	private byte[] encrypted;
	private byte[] decrypted;
	private File selectedFile;
	private JButton btnSaveToFile;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JButton btnLoadFromFile;

	private JRadioButton rdbtnCTR;
	private JRadioButton rdbtnCCM;
	private JRadioButton rdbtnEBC;
	private JRadioButton rdbtnCBC;

	private List<JRadioButton> radioButtons = new ArrayList<JRadioButton>();

	private byte[] keyBytes;
	private Random r = new Random();

	private AES aes = new AES();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui window = new gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 559, 296);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Key:");
		lblNewLabel.setBounds(10, 94, 64, 13);
		frame.getContentPane().add(lblNewLabel);

		txtKey = new JTextField();
		txtKey.setToolTipText("64 bit hex or 32 bit string");
		txtKey.setBounds(65, 91, 467, 19);
		frame.getContentPane().add(txtKey);
		txtKey.setColumns(10);

		separator = new JSeparator();
		separator.setBounds(10, 83, 522, 2);
		frame.getContentPane().add(separator);

		btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(162, 130, 113, 21);
		frame.getContentPane().add(btnGenerate);

		btnEncrypt = new JButton("Encrypt and save");
		btnEncrypt.setBounds(162, 206, 142, 21);
		frame.getContentPane().add(btnEncrypt);

		btnDecrypt = new JButton("Decrypt and save");
		btnDecrypt.setBounds(339, 206, 142, 21);
		frame.getContentPane().add(btnDecrypt);

		lblNewLabel_1 = new JLabel("Choose file:");
		lblNewLabel_1.setBounds(10, 14, 123, 13);
		frame.getContentPane().add(lblNewLabel_1);

		btnUpload = new JButton("Browse");
		btnUpload.setBounds(162, 10, 370, 21);
		frame.getContentPane().add(btnUpload);

		lblNewLabel_2 = new JLabel("File path: ");
		lblNewLabel_2.setBounds(10, 53, 99, 13);
		frame.getContentPane().add(lblNewLabel_2);

		txtFilePath = new JTextField();
		txtFilePath.setEditable(false);
		txtFilePath.setBounds(162, 50, 370, 19);
		frame.getContentPane().add(txtFilePath);
		txtFilePath.setColumns(10);

		separator_1 = new JSeparator();
		separator_1.setBounds(10, 161, 522, 2);
		frame.getContentPane().add(separator_1);

		btnSaveToFile = new JButton("Save to file");
		btnSaveToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSaveToFile.setBounds(287, 130, 113, 21);
		frame.getContentPane().add(btnSaveToFile);

		lblNewLabel_3 = new JLabel("Key operations: ");
		lblNewLabel_3.setBounds(10, 134, 142, 13);
		frame.getContentPane().add(lblNewLabel_3);

		lblNewLabel_4 = new JLabel("Encrypt and decrypt:");
		lblNewLabel_4.setBounds(10, 210, 123, 13);
		frame.getContentPane().add(lblNewLabel_4);

		btnLoadFromFile = new JButton("Load from file");
		btnLoadFromFile.setBounds(412, 130, 113, 21);
		frame.getContentPane().add(btnLoadFromFile);

		JLabel lblNewLabel_4_1 = new JLabel("Choose the mode:");
		lblNewLabel_4_1.setBounds(10, 173, 123, 13);
		frame.getContentPane().add(lblNewLabel_4_1);

		rdbtnEBC = new JRadioButton("ECB");
		rdbtnEBC.setBounds(162, 169, 64, 21);
		frame.getContentPane().add(rdbtnEBC);

		rdbtnCBC = new JRadioButton("CBC");
		rdbtnCBC.setBounds(257, 169, 64, 21);
		frame.getContentPane().add(rdbtnCBC);

		rdbtnCTR = new JRadioButton("CTR");
		rdbtnCTR.setBounds(357, 169, 64, 21);
		frame.getContentPane().add(rdbtnCTR);

		rdbtnCCM = new JRadioButton("CCM");
		rdbtnCCM.setBounds(450, 169, 64, 21);
		frame.getContentPane().add(rdbtnCCM);

		ButtonGroup g = new ButtonGroup();

		radioButtons.add(rdbtnEBC);
		radioButtons.add(rdbtnCBC);
		radioButtons.add(rdbtnCTR);
		radioButtons.add(rdbtnCCM);

		g.add(radioButtons.get(0));
		g.add(radioButtons.get(1));
		g.add(radioButtons.get(2));
		g.add(radioButtons.get(3));

		radioButtons.get(0).setActionCommand("ECB");
		radioButtons.get(1).setActionCommand("CBC");
		radioButtons.get(2).setActionCommand("CTR");
		radioButtons.get(3).setActionCommand("CCM");

		openFilePicker(btnUpload, txtFilePath);
		generateKey(btnGenerate, txtKey);
		saveKeyToFile(btnSaveToFile);
		loadKeyFromFile(btnLoadFromFile, txtKey);

		setMode(g);

		encrypt(btnEncrypt);
		decrypt(btnDecrypt);
	}

	public void openFilePicker(JButton btn, JTextField txt) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				int r = jfc.showOpenDialog(null);

				if (r == JFileChooser.APPROVE_OPTION) {
					selectedFile = jfc.getSelectedFile();
					txt.setText(selectedFile.getAbsolutePath());
					try {
						fileBytes = Files.readAllBytes(selectedFile.toPath());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}

		});
	}

	public void encrypt(JButton btn) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileBytes == null) {
					JOptionPane.showMessageDialog(frame, "Upload file to encrypt", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!(validateKey()))
					return;

				String key = txtKey.getText();

				if (key.length() == 16) {
					key = Formating.stringToHex(key);
				}

				byte[] keyBytes = Formating.hexStr2Byte(key);

				long pre = System.nanoTime();
				encrypted = aes.cipher(fileBytes, keyBytes);
				long post = System.nanoTime();

				double timePassedInS = (double) (post - pre) / 1000000000.0;
				double fileSizeInMB = ((double) selectedFile.length() / 1024) / 1024;
				double mbps = (fileSizeInMB / timePassedInS);

				System.out.println("=================IZ ENCRYPTA=================");
				System.out.println("KLJUC: " + key);
				System.out.println("VRIJEME U SEKUNDAMA: " + timePassedInS);
				System.out.println("FAJL U MB: " + fileSizeInMB);
				System.out.println("BRZINA U MB/s: " + mbps);
				System.out.println("=============================================");

				JOptionPane.showMessageDialog(
						frame, "File encrypted  successfully.\nEncryption speed: "
								+ (double) Math.round(mbps * 100) / 100 + " MB/s",
						"Success", JOptionPane.INFORMATION_MESSAGE);

				saveEncrypted();
			}

		});
	}

	public void decrypt(JButton btn) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileBytes == null) {
					JOptionPane.showMessageDialog(frame, "Upload file to encrypt", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!validateKey())
					return;

				String key = txtKey.getText();

				if (key.length() == 16) {
					key = Formating.stringToHex(key);
				}

				byte[] keyBytes = Formating.hexStr2Byte(key);

				long pre = System.nanoTime();
				decrypted = aes.decipher(fileBytes, keyBytes);
				long post = System.nanoTime();

				double timePassedInS = (double) (post - pre) / 1000000000.0;
				double fileSizeInMB = ((double) selectedFile.length() / 1024) / 1024;
				double mbps = (fileSizeInMB / timePassedInS);

				System.out.println("=================IZ DECRYPTA=================");
				System.out.println("KLJUC: " + key);
				System.out.println("VRIJEME U SEKUNDAMA: " + timePassedInS);
				System.out.println("FAJL U MB: " + fileSizeInMB);
				System.out.println("BRZINA U MB/s: " + mbps);
				System.out.println("=============================================");

				JOptionPane.showMessageDialog(
						frame, "File decrypted successfully.\nDecryption speed: "
								+ (double) Math.round(mbps * 100) / 100 + " MB/s",
						"Success", JOptionPane.INFORMATION_MESSAGE);

				saveDecrypted();
			}

		});
	}

	public void generateKey(JButton btn, JTextField key) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				keyBytes = new byte[16];
				r.nextBytes(keyBytes);
				key.setText(Formating.byte2HexStr(keyBytes));

			}

		});
	}

	public void saveEncrypted() {

		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		int f = jfc.showSaveDialog(null);

		if (f == JFileChooser.CANCEL_OPTION) {
			return;
		}

		String fileExtension = FilenameUtils.getExtension(txtFilePath.getText());
		System.out.println("======================IZ SAVE ENCRYPTEDA=================");
		System.out.println("Extension: " + fileExtension);
		System.out.println("=========================================================");

		System.out.println(selectedFile.getAbsolutePath());
		File outputFile = new File(jfc.getSelectedFile().toString() + "." + fileExtension);
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
			outputStream.write(encrypted);
			JOptionPane.showMessageDialog(frame, "File saved to: " + jfc.getSelectedFile().getAbsolutePath(), "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void saveDecrypted() {

		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		int f = jfc.showSaveDialog(null);

		if (f == JFileChooser.CANCEL_OPTION) {
			return;
		}

		String fileExtension = FilenameUtils.getExtension(txtFilePath.getText());
		System.out.println("======================IZ SAVE ENCRYPTEDA=================");
		System.out.println("Extension: " + fileExtension);
		System.out.println("=========================================================");

		File outputFile = new File(jfc.getSelectedFile().toString() + "." + fileExtension);
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
			outputStream.write(decrypted);

			JOptionPane.showMessageDialog(frame, "File saved to: " + jfc.getSelectedFile().getAbsolutePath(), "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void loadKeyFromFile(JButton btn, JTextField key) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.addChoosableFileFilter(filter);

				int f = jfc.showOpenDialog(null);

				if (f == JFileChooser.APPROVE_OPTION) {
					try {
						List<String> lines = Files.readAllLines(jfc.getSelectedFile().toPath());

						if (lines.size() != 1) {
							JOptionPane.showMessageDialog(frame, "Wrong file", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}

						key.setText(lines.get(0));

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}

		});
	}

	public boolean validateKey() {
		if (txtKey.getText().length() < 1) {
			JOptionPane.showMessageDialog(frame, "Key is empty	", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		System.out.println(txtKey.getText().length());
		System.out.println(txtKey.getText().length() != 64 && txtKey.getText().length() != 32);
		if (txtKey.getText().length() != 32 && txtKey.getText().length() != 32) {
			JOptionPane.showMessageDialog(frame, "Wrong key size (should be 16 byte string or 32 byte hex", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (txtKey.getText().length() == 32) {
			if (!txtKey.getText().matches("-?[0-9a-fA-F]+")) {
				JOptionPane.showMessageDialog(frame, "Key of this length should be hexadecimal", "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}

		}
		return true;
	}

	public void saveKeyToFile(JButton btn) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String key = txtKey.getText();

				if (!validateKey())
					return;

				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				jfc.showSaveDialog(null);

				try {
					PrintWriter writer = new PrintWriter(jfc.getSelectedFile().toString() + ".txt");
					writer.println(key);
					writer.close();
				} catch (FileNotFoundException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

			}

		});
	}

	public void setMode(ButtonGroup g) {
		for (JRadioButton b : radioButtons) {

			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String mode = g.getSelection().getActionCommand();

					aes.setMode(mode);

				}

			});
		}
	}

}
