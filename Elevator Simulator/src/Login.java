import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import java.awt.Component;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JButton;
import java.awt.ComponentOrientation;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.Toolkit;

public class Login extends JFrame {

	private JPanel contentPane;
	private int elevatorCount;
	private int floorCount;
	private int personLimit;
	//Icons---------------------------------------------------------//
	private ImageIcon bckIcon;
	private ImageIcon elevatorCountIcon;
	private ImageIcon floorCountIcon;
	private ImageIcon personLimitIcon;
	private ImageIcon simulteButtonIcon;
	private ImageIcon manualButtonIcon;
	//Icons---------------------------------------------------------//
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/javax/swing/plaf/metal/icons/ocean/question.png")));
		setTitle("Choose Wisely");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300, 450);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		readIcons();
		initialize();
	}
	
	//Creating the frame
	@SuppressWarnings("unchecked")
	private void initialize(){
		
		JLabel elevatorCountLabel = new JLabel(elevatorCountIcon);
		elevatorCountLabel.setBounds(10, 11, 264, 70);
		contentPane.add(elevatorCountLabel);
		
		JLabel floorCountLabel = new JLabel(floorCountIcon);
		floorCountLabel.setBounds(10, 129, 264, 70);
		contentPane.add(floorCountLabel);
		
		JLabel elevatorLimitLabel = new JLabel(personLimitIcon);
		elevatorLimitLabel.setBounds(10, 247, 264, 70);
		contentPane.add(elevatorLimitLabel);
		
		JSpinner floorCountSpinner = new JSpinner();
		
		floorCountSpinner.setModel(new SpinnerNumberModel(3, 3, 20, 1));
		floorCountSpinner.setFont(new Font("Tahoma", Font.BOLD, 16));
		floorCountSpinner.setBounds(10, 210, 264, 26);
		contentPane.add(floorCountSpinner);
		
		JComboBox elevatorCountComboBox = new JComboBox();
		elevatorCountComboBox.setToolTipText("Choose Visely");
		elevatorCountComboBox.setFont(new Font("Tahoma", Font.BOLD, 16));
		elevatorCountComboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4"}));
		elevatorCountComboBox.setBounds(10, 92, 264, 26);
		contentPane.add(elevatorCountComboBox);
		
		JComboBox personLimitComboBox = new JComboBox();
		personLimitComboBox.setToolTipText("Choose Person Limit");
		personLimitComboBox.setModel(new DefaultComboBoxModel(new String[] {"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"}));
		personLimitComboBox.setFont(new Font("Tahoma", Font.BOLD, 16));
		personLimitComboBox.setBounds(10, 328, 264, 26);
		contentPane.add(personLimitComboBox);
		
		JLabel simulateButton = new JLabel(simulteButtonIcon);
		simulateButton.setBounds(10, 365, 128, 35);
		contentPane.add(simulateButton);
		
		JLabel manuelButton = new JLabel(manualButtonIcon);
		manuelButton.setBounds(146, 365, 128, 35);
		contentPane.add(manuelButton);
		
		JLabel bckgr = new JLabel(bckIcon);
		bckgr.setBounds(0, 0, 284, 411);
		contentPane.add(bckgr);
		
		//Listeners--------------------------------------------------------------------//
		
		//Simulate button listener
		simulateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				dispose();
				elevatorCount = Integer.parseInt(elevatorCountComboBox.getSelectedItem().toString());
				floorCount = Integer.parseInt(floorCountSpinner.getValue().toString());
				personLimit = Integer.parseInt(personLimitComboBox.getSelectedItem().toString());
				createSimulateMain(elevatorCount,floorCount,personLimit);
			}
		});
		
		//Manuel button listener
		manuelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				dispose();
				elevatorCount = Integer.parseInt(elevatorCountComboBox.getSelectedItem().toString());
				floorCount = Integer.parseInt(floorCountSpinner.getValue().toString());
				personLimit = Integer.parseInt(personLimitComboBox.getSelectedItem().toString());
				createManuelMain(elevatorCount,floorCount,personLimit);
			}
		});
		
		//Listeners--------------------------------------------------------------------//
		
		
	}
	
	private void createSimulateMain(int elevatorCount, int floorCount, int personLimit){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main mn = new Main(elevatorCount, floorCount, personLimit,true);
					mn.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	//Creating the manual frame
	private void createManuelMain(int elevatorCount, int floorCount, int personLimit){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main mn = new Main(elevatorCount, floorCount, personLimit,false);
					mn.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public void readIcons(){
		bckIcon = new ImageIcon(Login.class.getResource("/loginGraphics/bck.png"));
		elevatorCountIcon = new ImageIcon(Login.class.getResource("/loginGraphics/elevatorCount.png"));
		floorCountIcon = new ImageIcon(Login.class.getResource("/loginGraphics/floorCount.png"));
		personLimitIcon = new ImageIcon(Login.class.getResource("/loginGraphics/personLimit.png"));
		simulteButtonIcon = new ImageIcon(Login.class.getResource("/loginGraphics/simulateButton.png"));
		manualButtonIcon = new ImageIcon(Login.class.getResource("/loginGraphics/manuelButton.png"));

	}
	
}
