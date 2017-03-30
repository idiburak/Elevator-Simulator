import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;


public class ElevatorOutside extends JPanel {

	private ElevatorInsidePanel eip;
	private Floors[] floors;	
	
	//Labels and Panels-----------------------------------------//
	private JPanel panel;										//
	private JScrollPane scrollPane;								//
	private JLabel bckGr, movableElevator;						//
	//Labels and Panels-----------------------------------------//
	
	//Variables ------------------------------------------------//
	private int elevatorMoveDelayTime = 15; // Bigger is slower	//
	private int[] floorCheck;									//
	private int floorCount;										//
	private int personLimit;									//
	private int elevatorNumber;									//
	//Variables ------------------------------------------------//
	private FileOperations records = new FileOperations("records.txt");
	
	//Constructor
	public ElevatorOutside(int floorCount,int personLimit,int elevatorNumber) {
		this.floorCount = floorCount;
		this.personLimit = personLimit;
		this.elevatorNumber = elevatorNumber;
		setSize(225,485);
		setLayout(null);
		
		floorCheck = new int[floorCount+1];
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(0, 0, 225, 485);
		add(scrollPane);
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(205, 125*(floorCount+1)-40));
		scrollPane.setViewportView(panel);
		panel.setLayout(null);
		
		
		floors = new Floors[floorCount+1];
		//Creating floors
 		for(int i=0 ; i<=floorCount ; i++){
 			floors[i] = new Floors();
 			floors[i].setFloorNumber(i);
 			floors[i].setLocation(0, 125*(floorCount-i));
 			floors[i].makeDisable(true);
 			final int callFloor = i;
 			//----------------------------------------------------------------------//
 			//Elevator Up button listener											//
 			floors[i].getElevatorUpButton().addMouseListener(new MouseAdapter() {	//
 				public void mouseClicked(MouseEvent arg0) {							//	
 					if(floors[callFloor].getElevatorUpButton().isEnabled()){		//
 						floors[callFloor].setUpGreen();								//	
 						floors[callFloor].setDownGreen();							//	
	 					addQueue(callFloor);										//
 					}																//	
 				}																	//	
			});																		//
 			//Elevator Down button listener											//	
 			floors[i].getElevatorDownButton().addMouseListener(new MouseAdapter() {	//	
 				public void mouseClicked(MouseEvent arg0) {							//	
 					if(floors[callFloor].getElevatorDownButton().isEnabled()){		//
 						floors[callFloor].setUpGreen();								//	
 						floors[callFloor].setDownGreen();							//	
	 					addQueue(callFloor);										//
 					}																//	
 				}																	//	
			});																		//
 			//----------------------------------------------------------------------//
 			//Person Add Button Listener											//
 			floors[i].getAddPersonButton().addMouseListener(new MouseAdapter() {	//
				public void mouseClicked(MouseEvent arg0) {							//
					enterElevator(callFloor);										//						//
				}																	//
 			});																		//
 			//Person Remove Button Listener											//
 			floors[i].getRemovePersonButton().addMouseListener(new MouseAdapter() {	//
				public void mouseClicked(MouseEvent arg0) {							//
					getOutFromElevator(callFloor);									//							//
				}																	//
 			});																		//
 			//----------------------------------------------------------------------//
 			//Warning for person count 												//
 			floors[i].getPersonCount().addPropertyChangeListener("text", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					int pCount = Integer.parseInt(floors[callFloor].getPersonCount().getText());
					if(pCount==0){
						eip.setCallButtonsEnabled(false);
					}else{
						eip.setCallButtonsEnabled(true);
					}
					if(pCount>personLimit){
						floors[callFloor].heavyWarningRed(true);
						if(pCount>personLimit  ){
							floors[callFloor].getAddPersonButton().setEnabled(false);
						}else if(floors[callFloor].getRemovePersonButton().isEnabled()){
							floors[callFloor].getAddPersonButton().setEnabled(true);
						}
					}else{
						floors[callFloor].heavyWarningRed(false);
					}
				}
			});
 			//----------------------------------------------------------------------//
 			
 			//Adding created floor to the panel
 			panel.add(floors[i]);
		}
 		
 		//The elevator label which can move between floors
 		ImageIcon ME = new ImageIcon(ElevatorOutside.class.getResource("/elevatorOutsideGraphics/elevatorInside.png"));
 		movableElevator = new JLabel(ME);
		movableElevator.setBounds(39, floorCount*125, 48, 75);
		panel.add(movableElevator);
		
		//Setting the background
		bckGr = new JLabel(new ImageIcon(ElevatorOutside.class.getResource("/elevatorOutsideGraphics/bckGr.png")));
		bckGr.setBounds(0, 0, 205, 2585);
		panel.add(bckGr);
		
	}
	
	public Point getElevatorLocation(){
		return movableElevator.getLocation();
	}
	
	
	public int getElevatorFloor(){
		return (floorCount - (movableElevator.getLocation().y)/125);
	}
	
	//**********************************************************************************//
	//																					//
	//								ELEVATOR MOVE FUNCTIONS								//
	//																					//
	//**********************************************************************************//
	private Thread th; // The thread which move the elevator
	//Set the destination floor in floorCheck lookup table '1'
	public void addQueue(int callFloor){
		floorCheck[callFloor] = 1 ;
		if(th != null ){
			th.stop();
		}
		callElevator();
	}
	
	private boolean movingUp = true; //Elevators direction
	//Elevator move function
	public void callElevator(){
		th= new Thread(){
			@Override
			public void run(){
				synchronized (movableElevator){
					try{
						int elevatorCurrentY = getElevatorLocation().y;
						int elevatorCurrentFloor = getElevatorFloor();
						//if elevator is moving up
						if(movingUp){
							//move the elevator up from current floor to destination
							for(int i=elevatorCurrentFloor ; i<=floorCount ; i++){
								movingUp = true;
								if(floorCheck[i]==1 ){
									while(elevatorCurrentY > 125*(floorCount-i)){
										elevatorCurrentY--;
										movableElevator.setLocation(39,elevatorCurrentY);
										showElevatorFloorNumber();
										sleep(elevatorMoveDelayTime);
									}
									
									reachFloor(i);
									floorCheck[i]=0;
								}
							}
							//when finish floors above current floor
							//move the elevator down from current floor to destination
							for(int i=elevatorCurrentFloor -1; i>=0 ; i--){
								movingUp = false;
								if(floorCheck[i]==1){
									while(elevatorCurrentY < 125*(floorCount-i)){
										elevatorCurrentY++;
										movableElevator.setLocation(39,elevatorCurrentY);
										showElevatorFloorNumber();
										sleep(elevatorMoveDelayTime);
									}
									reachFloor(i);
									floorCheck[i]=0;
								}
							}
						}else{//if elevator is moving down
							//move the elevator down from current floor to destination
							for(int i=elevatorCurrentFloor; i>=0 ; i--){
								movingUp = false;
								if(floorCheck[i]==1 ){
									while(elevatorCurrentY < 125*(floorCount-i)){
										elevatorCurrentY++;
										movableElevator.setLocation(39,elevatorCurrentY);
										showElevatorFloorNumber();
										sleep(elevatorMoveDelayTime);
									}
									if(!isMoving()){
										reachFloor(i);
										floorCheck[i]=0;
									}
									
								}
							}
							//when finish floors below current floor
							//move the elevator up from current floor to destination
							for(int i=elevatorCurrentFloor ; i<=floorCount ; i++){
								movingUp = true;
								if(floorCheck[i]==1){
									while(elevatorCurrentY > 125*(floorCount-i)){
										elevatorCurrentY--;
										movableElevator.setLocation(39,elevatorCurrentY);
										showElevatorFloorNumber();
										sleep(elevatorMoveDelayTime);
									}
									reachFloor(i);
									floorCheck[i]=0;
								}
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		};
		th.start();
	}
	//**********************************************************************************//
	
	
	//------------------------------------------//
	//The door animation and return default		//
	//------------------------------------------//
	public void reachFloor(int rf){
		try {
			floors[rf].adjustNumber(rf);
			floors[rf].openDoors();
			Thread.sleep(100);
			floors[rf].makeDisable(false);
			eip.setNumberIconDefault(rf);
			floors[rf].callButtonDefaultIcon();
			Thread.sleep(3000);
			floors[rf].makeDisable(true);
			Thread.sleep(100);
			floors[rf].closeDoors();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//Shows the elevators current floor
	public void showElevatorFloorNumber(){
		for(Floors pan:floors){
			pan.adjustNumber(floorCount - (getElevatorLocation().y + 66)/125);
		}
		eip.adjustNumber(floorCount - (getElevatorLocation().y + 66)/125);
	}
	
	//When emergency button is clicked find the nearest floor and move elevator to there
	public void moveToNearestFloor(){
		if(isMoving()){
			th.stop();
			int nearestFloor;
			if(getElevatorLocation().y % 125 > 66){
				nearestFloor = (floorCount - getElevatorLocation().y / 125 -1);
			}else{
				nearestFloor = (floorCount - getElevatorLocation().y / 125  );
			}
			for(int i=0 ; i<floorCount +1 ; i++){
				floorCheck[i] = 0;
				floors[i].callButtonDefaultIcon();
				eip.setNumberIconDefault(i);
			}
			makeCallsDisable();
			addQueue(nearestFloor);
			//callElevatorNew();
			
		}else{
			
		}
	}
	
	//When emergency button is clicked make all buttons disable
	public void makeCallsDisable(){
		Thread t = new Thread(){
			public void run(){
				for(int i=0 ; i<floorCount+1 ; i++){
					floors[i].setCallEnable(false);
				}
				eip.setCallButtonsEnabled(false);
				try {
					sleep(4000);
				} catch (Exception e) {
					
				}
				for(int i=0 ; i<floorCount+1 ; i++){
					floors[i].setCallEnable(true);
				}
				eip.setCallButtonsEnabled(true);
			}
		};
		t.start();
	}
	
	public boolean isMoving(){
		if(movableElevator.getLocation().y % 125 == 0)
			return false;
		return true;
	}
	
	public void setInsidePanel(ElevatorInsidePanel eip){
		this.eip = eip;
		eip.setOutside(this);
		eip.setCallButtonsEnabled(false);
	}
	public boolean canEnter(int floorNumber){
		if(getElevatorFloor() == floorNumber && getElevatorLocation().y % 125 == 0
				&& floors[floorNumber].getDoorCurrentStatus() == 0){
			return true;
		}
		return false;
	}
	
	//Add person to elevator
	public void enterElevator(int floorNumber){
		if(floors[floorNumber].getAddPersonButton().isEnabled()){
			floors[floorNumber].addPerson();
			String number =floors[floorNumber].getPersonCount().getText();
			records.enterTheElevator(elevatorNumber);
			floors[floorNumber].openDoors();
			try {
				th.stop();
				callElevator();	
			} catch (Exception e) {	e.printStackTrace(); }
			for(int i=0 ; i<floorCount+1 ; i++){
				floors[i].setPersonCountNumber(number);
			}
		}
	}
	
	//Remove person from elevator
	public void getOutFromElevator(int floorNumber){
		if(floors[floorNumber].getRemovePersonButton().isEnabled() 
				&& Integer.parseInt(floors[floorNumber].getPersonCount().getText())>0){
			floors[floorNumber].removePerson();
			String number=floors[floorNumber].getPersonCount().getText();
			floors[floorNumber].openDoors();
			try {				
				th.stop();											
				callElevator();	
			} catch (Exception e) {	e.printStackTrace(); }
			for(int i=0 ; i<floorCount+1 ; i++){
				floors[i].setPersonCountNumber(number);	
			}
		}
	}
	
	//Elevator call function that used by simulation mode
	public void simulateCall(int callFloor){
		if(floors[callFloor].getElevatorUpButton().isEnabled()){
			floors[callFloor].setUpGreen();	
			floors[callFloor].setDownGreen();
			addQueue(callFloor);
		}
	}
	
	public ElevatorInsidePanel getInsidePanel(){
		return eip;
	}

	public Floors[] getFl() {
		return floors;
	}

	public Thread getTh() {
		return th;
	}
	
	
	
	
	
	
	
}
