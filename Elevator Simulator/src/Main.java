/*
 * Author 	: Burak IDI
 * E-mail	: idiburak60@gmail.com
 * IDE		: Eclipse Mars
 * Operation System		: Windows 10 Pro 64 bit
 * System Information >>
 * CPU : Intel(R) Core(TM) i7-4702MQ CPU @ 2.20GHz
 * RAM : 8,00 GB
 * 
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Toolkit;

public class Main extends JFrame {

	private JPanel contentPane;
	private int elevatorCount;
	private int floorCount;
	private int personLimit;
	private boolean isSimulate;
	//Creating main frame
	public Main(int elevatorCount, int floorCount, int personLimit,boolean isSimulate) {
		setTitle("Elevator Simulator");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/doors/4.png")));
		
		this.elevatorCount = elevatorCount;
		this.floorCount = floorCount;
		this.personLimit = personLimit;
		this.isSimulate = isSimulate;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(25 + 235*elevatorCount, 700);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		//Creating elevator panels
		ElevatorInsidePanel[] eip = new ElevatorInsidePanel[elevatorCount];
		ElevatorOutside[] eop = new ElevatorOutside[elevatorCount];
		Simulator[] sim = new Simulator[elevatorCount];
		for(int i=0; i<elevatorCount ; i++){
			eip[i] = new ElevatorInsidePanel(floorCount);
			eip[i].setLocation(10 + 235*i, 5);
			contentPane.add(eip[i]);
			
			eop[i] = new ElevatorOutside(floorCount,personLimit,i);
			eop[i].setLocation(10 + 235*i, 170);
			contentPane.add(eop[i]);
			eop[i].setInsidePanel(eip[i]);
			
			if(isSimulate)
				sim[i] = new Simulator(eip[i],eop[i],floorCount,i);
		}
		
		//Background Picture
		JLabel mainBck = new JLabel(new ImageIcon(ElevatorInsidePanel.class.getResource("/mainBack.png")));
		mainBck.setBounds(0, 0, 965, 700);
		contentPane.add(mainBck);
	}
	
	
	

}
