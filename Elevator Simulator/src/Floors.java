import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Floors extends JPanel {

	//Labels------------------------------------------------//
	private JLabel floorNumber, door;						//
	private JLabel firstDigit, secondDigit;					//
	private JLabel addPersonButton, removePersonButton;		//
	private JLabel elevatorUpButton, elevatorDownButton;	//
	private JLabel heavyWarning ;							//
	private JLabel person, personCount;						//
	//Labels------------------------------------------------//
	
	//Icons-------------------------------------------------//
	private ImageIcon[] doorIcons = new ImageIcon[7];		//
	private ImageIcon[] numbers = new ImageIcon[10];		//
	private ImageIcon iconAddPerson, iconRemovePerson;		//
	private ImageIcon iconEUp,iconEDown;					//
	private ImageIcon iconEUpGreen,iconEDownGreen;			//
	private ImageIcon iconHeavyWarning, iconPerson;			//
	//Icons-------------------------------------------------//

	
	//Constructor with necessary setup values
	public Floors() {
		setOpaque(false);
		setSize(205,75);
		setLayout(null);
		readIcons();
		initialize();	
	}
	
	//----------------------------------------------------------------------//
	//Creating labels and setting their icons								//
	private void initialize(){
		floorNumber = new JLabel("");
		floorNumber.setFont(new Font("Tahoma", Font.PLAIN, 20));
		floorNumber.setBounds(10, 1, 25, 74);
		add(floorNumber);
		
		door = new JLabel(doorIcons[6]);
		door.setBounds(39, 1, 46, 74);
		add(door);
		
		firstDigit = new JLabel(numbers[0]);
		firstDigit.setAlignmentX(Component.CENTER_ALIGNMENT);
		firstDigit.setBounds(95, 1, 10, 17);
		add(firstDigit);
		
		secondDigit = new JLabel(numbers[0]);
		secondDigit.setAlignmentX(Component.CENTER_ALIGNMENT);
		secondDigit.setBounds(110, 1, 10, 17);
		add(secondDigit);
		
		addPersonButton = new JLabel(iconAddPerson);
		addPersonButton.setBounds(130, 23, 25, 25);
		add(addPersonButton);
		
		removePersonButton = new JLabel(iconRemovePerson);
		removePersonButton.setBounds(130, 50, 25, 25);
		add(removePersonButton);
		
		elevatorUpButton = new JLabel(iconEUp);
		elevatorUpButton.setBounds(95, 23, 25, 25);
		add(elevatorUpButton);
		
		elevatorDownButton = new JLabel(iconEDown);
		elevatorDownButton.setBounds(95, 50, 25, 25);
		add(elevatorDownButton);
		
		heavyWarning = new JLabel(iconHeavyWarning);
		heavyWarning.setEnabled(false);
		heavyWarning.setBounds(130, 1, 25, 20);
		add(heavyWarning);
		
		person = new JLabel(iconPerson);
		person.setBounds(165, 1, 36, 36);
		add(person);
		
		personCount = new JLabel("0");
		personCount.setHorizontalAlignment(SwingConstants.CENTER);
		personCount.setFont(new Font("Tahoma", Font.PLAIN, 20));
		personCount.setBounds(165, 38, 36, 36);
		add(personCount);
	}
	//----------------------------------------------------------------------//
	
	//Method for reading icons.
	public void readIcons(){
		String src = "";
		ImageIcon icn;
		//Reading door icons.
		for(int i=0 ; i<7 ; i++ ){
			src = "/doors/";
			src += i;
			src += ".png";
			icn = new ImageIcon(ElevatorInsidePanel.class.getResource(src));
			doorIcons[i]= icn;
		}
		
		//Reading number icons.
		for(int i=0 ; i<10 ; i++ ){
			src = "/smallNumbers/";
			src += i;
			src += ".png";
			icn = new ImageIcon(ElevatorInsidePanel.class.getResource(src));
			numbers[i]= icn;
		}
		
		//Other icons.
		iconAddPerson = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorOutsideGraphics/addPerson.png"));
		iconRemovePerson = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorOutsideGraphics/removePerson.png"));
		iconEUp = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorOutsideGraphics/upButton.png"));
		iconEDown = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorOutsideGraphics/downButton.png"));
		iconEUpGreen = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorOutsideGraphics/upButtonGreen.png"));
		iconEDownGreen = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorOutsideGraphics/downButtonGreen.png"));
		iconHeavyWarning = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorOutsideGraphics/heavyWarning.png"));
		iconPerson = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorOutsideGraphics/person.png"));
	
	}
	
	//Make the floor's necessary parts disable 
	public void makeDisable(boolean disable){
		//floorNumber.setEnabled(!disable);
		//firstDigit.setEnabled(!disable);
		//secondDigit.setEnabled(!disable);
		//elevatorUpButton.setEnabled(!disable);
		//elevatorDownButton.setEnabled(!disable);
		//heavyWarning.setEnabled(!disable);
		//door.setEnabled(!disable);
		if(!heavyWarning.isEnabled()){
			addPersonButton.setEnabled(!disable);
		}
		if(disable){
			addPersonButton.setEnabled(!disable);
		}
		removePersonButton.setEnabled(!disable);
		person.setEnabled(!disable);
		personCount.setEnabled(!disable);
	}
	
	//Animation of opening doors
	public void openDoors(){
		Thread th = new Thread(){
			public void run(){
				try{
					for(int i=getDoorCurrentStatus() ; i>=0 ; i--){
						door.setIcon(doorIcons[i]);
						sleep(100);
					}
				}catch(Exception e){
					
				}			
			}
		};
		th.start();
		try {
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//Animation of closing doors
	public void closeDoors(){
		Thread th = new Thread(){
			public void run(){
				try{
					for(int i=getDoorCurrentStatus() ; i<7 ; i++){
						door.setIcon(doorIcons[i]);
						sleep(100);
					}
				}catch(Exception e){
					
				}			
			}
		};
		th.start();
		try {
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isDoorClosed(){
		if(door.getIcon() == doorIcons[6])
			return true;
		return false;
	}
	
	//Adjust the number of the floor
	public void adjustNumber(int number){
		if(number>0){
			firstDigit.setIcon(numbers[number/10]);
			secondDigit.setIcon(numbers[number%10]);
		}else{
			firstDigit.setIcon(numbers[0]);
			secondDigit.setIcon(numbers[0]);
		}
	}
	
	public void setCallEnable(boolean enable){
		elevatorUpButton.setEnabled(enable);
		elevatorDownButton.setEnabled(enable);
	}
	
	public void addPerson(){
		int pCount = Integer.parseInt(personCount.getText()) + 1;
		personCount.setText(Integer.toString(pCount));
	}
	public void removePerson(){
		int pCount = Integer.parseInt(personCount.getText()) - 1;
		personCount.setText(Integer.toString(pCount));
	}
	public void setPersonCountNumber(String pCount){
		personCount.setText(pCount);
	}
	
	public void heavyWarningRed(boolean isHeavy){
		heavyWarning.setEnabled(isHeavy);
	}
	
	public int getDoorCurrentStatus(){
		if(door.getIcon() == doorIcons[0]){
			return 0;
		}else if(door.getIcon() == doorIcons[1]){
			return 1;
		}else if(door.getIcon() == doorIcons[2]){
			return 2;
		}else if(door.getIcon() == doorIcons[3]){
			return 3;
		}else if(door.getIcon() == doorIcons[4]){
			return 4;
		}else if(door.getIcon() == doorIcons[5]){
			return 5;
		}else if(door.getIcon() == doorIcons[6]){
			return 6;
		}else{
			return 0;
		}
		
	}
	
	public void callButtonDefaultIcon(){
		elevatorUpButton.setIcon(iconEUp);
		elevatorDownButton.setIcon(iconEDown);
	}
	
	//Getters and setters -----------------------------------------------------//
	
	public void setUpGreen(){
		elevatorUpButton.setIcon(iconEUpGreen);
	}
	
	public void setDownGreen(){
		elevatorDownButton.setIcon(iconEDownGreen);
	}
	
	public void setFloorNumber(int num){
		this.floorNumber.setText(Integer.toString(num));
	}

	public int getFloor() {
		return Integer.parseInt(floorNumber.getText());
	}
	
	public JLabel getAddPersonButton() {
		return addPersonButton;
	}

	public JLabel getRemovePersonButton() {
		return removePersonButton;
	}

	public JLabel getElevatorUpButton() {
		return elevatorUpButton;
	}

	public JLabel getElevatorDownButton() {
		return elevatorDownButton;
	}

	public JLabel getHeavyWarning() {
		return heavyWarning;
	}

	public JLabel getPersonCount() {
		return personCount;
	}

	public JLabel getDoor() {
		return door;
	}
	
	
	//Getters and setters -----------------------------------------------------//
	
	

}
