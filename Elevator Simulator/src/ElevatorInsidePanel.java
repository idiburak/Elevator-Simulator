import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.*;
import java.awt.Dimension;
import javax.swing.border.LineBorder;

public class ElevatorInsidePanel extends JPanel {

	private ElevatorOutside eop;
	private JPanel buttonsPanel;
	
	//Labels--------------------------------------------------------//
	private JLabel firstDigit,secondDigit ;							//
	private JLabel phoneButton, alarmButton;						//
	private JLabel emergencyButton, stopButton;						//
	private JLabel insideBackground;								//
	private JLabel[] callButtons;									//
	//Labels--------------------------------------------------------//
	
	//Icons---------------------------------------------------------//
	private ImageIcon circleButton;									//
	private ImageIcon redCircleButton, greenCircleButton;			//
	private ImageIcon phoneCircleButton, alarmCircleButton;			//
	private ImageIcon emergencyCircleButton, emergencyActiveButton;	//
	private ImageIcon[] numberIcons = new ImageIcon[10];			//
	//Icons---------------------------------------------------------//
	
	private int floorCount;
	
	public ElevatorInsidePanel(int floorCount) {
		this.floorCount = floorCount;
		readIcons();
		
		setOpaque(false);
		setPreferredSize(new Dimension(225, 160));
		setSize(new Dimension(225, 160));
		setLayout(null);
		
		stopButton = new JLabel("STOP");
		stopButton.setForeground(Color.RED);
		stopButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		stopButton.setIcon(circleButton);
		stopButton.setHorizontalTextPosition(SwingConstants.CENTER);
		stopButton.setHorizontalAlignment(SwingConstants.CENTER);
		stopButton.setBounds(35, 75, 30, 30);
		add(stopButton);
		
		//First digit of the which shows the current floor
		firstDigit = new JLabel("");
		firstDigit.setIcon(new ImageIcon(ElevatorInsidePanel.class.getResource("/numbers/0.png")));
		firstDigit.setBounds(5, 11, 30, 50);
		add(firstDigit);
		//Second digit of the which shows the current floor		
		secondDigit = new JLabel("");
		secondDigit.setIcon(new ImageIcon(ElevatorInsidePanel.class.getResource("/numbers/0.png")));
		secondDigit.setBounds(35, 11, 30, 50);
		add(secondDigit);
		
		phoneButton = new JLabel(phoneCircleButton);
		phoneButton.setToolTipText("Call");
		phoneButton.setBounds(3, 110, 30, 30);
		add(phoneButton);
		
		emergencyButton = new JLabel(emergencyCircleButton);
		emergencyButton.setToolTipText("Call");
		emergencyButton.setBounds(3, 75, 30, 30);
		add(emergencyButton);
		
		alarmButton = new JLabel(alarmCircleButton);
		alarmButton.setBounds(35, 110, 30, 30);
		add(alarmButton);
		
		buttonsPanel = new JPanel();
		buttonsPanel.setOpaque(false);
		buttonsPanel.setBounds(70, 5, 150, 150);
		add(buttonsPanel);
		buttonsPanel.setLayout(null);
		
		callButtons = new JLabel[floorCount + 1];
		JLabel lbl; //Temporary label for creating callButton labels
		//Creating call buttons
		for(int i=0 ; i<floorCount+1 ; i++){
			lbl = new JLabel(Integer.toString(i));
			lbl.setIcon(circleButton);
			lbl.setFont(new Font("Tahoma", Font.BOLD, 11));
			lbl.setHorizontalTextPosition(SwingConstants.CENTER);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			callButtonIconChange(lbl);
			callButtons[i]=lbl;			
		}
		
		//Adding buttons to button panel with absolute location
		callButtons[0].setBounds(60, 120, 30, 30);
		buttonsPanel.add(callButtons[0]);
		int j=0;
		for(int i=1 ; i<floorCount+1 ; i++){
			if((i-1)%5 == 0){
				j++;
			}
			callButtons[i].setBounds(30*((i-1)%5), 120-(30*j), 30, 30);
			buttonsPanel.add(callButtons[i]);
		}
		
		//Background of the panel
		insideBackground = new JLabel(new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorInsideGraphics/bck.png")));
		insideBackground.setBounds(0, 0, 225, 160);
		add(insideBackground);
		
		//*************************************************************//
		//******************* 		Listeners		*******************//
		//*************************************************************//
		
		alarmButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Toolkit.getDefaultToolkit().beep();	
				playTheAlarm();
			}
		});
		
		emergencyButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				eop.moveToNearestFloor();
			}
		});
		
		stopButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(stopButton.getIcon() == circleButton){
					stopButton.setIcon(redCircleButton);
					eop.getTh().stop();
				}else{
					stopButton.setIcon(circleButton);
					eop.callElevator();
				}
			}
		});
		
		//*************************************************************//
		
		adjustNumber(0); //Adjust the shown number 0
		
	}
	
	//Call Buttons Mouse Click Listener
	public void callButtonIconChange(JLabel lbl){
		lbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if(lbl.getIcon() == circleButton && lbl.isEnabled()){
					lbl.setIcon(greenCircleButton);
					eop.getFl()[Integer.parseInt(lbl.getText())].setUpGreen();
					eop.getFl()[Integer.parseInt(lbl.getText())].setDownGreen();
					eop.addQueue(Integer.parseInt(lbl.getText()));
				}
			}
		});
	}
	public void clickCallButton(int buttonNumber){
		if(callButtons[buttonNumber].getIcon() == circleButton && callButtons[buttonNumber].isEnabled()){
			callButtons[buttonNumber].setIcon(greenCircleButton);
			eop.getFl()[Integer.parseInt(callButtons[buttonNumber].getText())].setUpGreen();
			eop.getFl()[Integer.parseInt(callButtons[buttonNumber].getText())].setDownGreen();
			eop.addQueue(Integer.parseInt(callButtons[buttonNumber].getText()));
		}
	}
	
	//Reading necessary icons
	public void readIcons(){
		//Reading number icons
		String txt = "";
		for(int i=0 ; i<10 ; i++ ){
			txt = "/numbers/";
			txt += i;
			txt += ".png";
			numberIcons[i] = new ImageIcon(ElevatorInsidePanel.class.getResource(txt));
		}
		circleButton = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorInsideGraphics/circleButton.png"));
		redCircleButton = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorInsideGraphics/redCircleButton.png"));
		greenCircleButton = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorInsideGraphics/greenCircleButton.png"));
		phoneCircleButton = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorInsideGraphics/phoneButton.png"));
		alarmCircleButton = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorInsideGraphics/alarmButton.png"));
		emergencyCircleButton = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorInsideGraphics/emergencyButton.png"));
		emergencyActiveButton = new ImageIcon(ElevatorInsidePanel.class.getResource("/elevatorInsideGraphics/emergencyButtonActive.png"));	
	}
	
	//Adjusting shown number on the panel
	public void adjustNumber(int number){
		if(number>0){
			firstDigit.setIcon(numberIcons[number/10]);
			secondDigit.setIcon(numberIcons[number%10]);
		}else{
			firstDigit.setIcon(numberIcons[0]);
			secondDigit.setIcon(numberIcons[0]);
		}
	}
	
	//Setting all call buttons enabled or disabled
	public void setCallButtonsEnabled(boolean enable){
		for(int i=0 ; i<floorCount + 1 ; i++){
			callButtons[i].setEnabled(enable);
		}
	}
	
	//The audio of the alarm
	public void playTheAlarm(){
		 try {
	         URL url = this.getClass().getClassLoader().getResource("alarm.wav");
	         AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	         Clip clip = AudioSystem.getClip();
	         clip.open(audioIn);
	         clip.start();
	      } catch (Exception e) {
	         e.printStackTrace();
	      } 
	}
	
	public void setNumberIconDefault(int buttonNumber){
		callButtons[buttonNumber].setIcon(circleButton);
	}
	
	public void setOutside(ElevatorOutside eop){
		this.eop = eop;
	}
	
	public ElevatorOutside getOutsidePanel(){
		return eop;
	}
}
