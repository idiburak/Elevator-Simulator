import java.io.*;
import java.util.Scanner;


public class FileOperations {
	private String fileName;
	private FileWriter fw;
	
	public FileOperations(String fileName){
		this.fileName = fileName;
		try {
			fw = new FileWriter("logs.txt",true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enterTheElevator(int floorNumber){
		try {
			//If there is no file create new one
			try{
				Scanner check = new Scanner(new FileReader(fileName));
				check.nextLine();
			}catch (Exception e){
				FileWriter fw = new FileWriter(fileName);
				String inside;
				inside = "1.Elevator : 0 \n";
				inside += "2.Elevator : 0 \n";
				inside += "3.Elevator : 0 \n";
				inside += "4.Elevator : 0 \n";
				fw.write(inside);
				fw.flush();
				fw.close();
			}
			
			//Read the file
			FileReader fr = new FileReader(fileName);
			Scanner sc = new Scanner(fr);
			
			//Read lines
			String firstLine = sc.nextLine();
			String secondLine = sc.nextLine();
			String thirdLine = sc.nextLine();
			String fourthLine = sc.nextLine();
			
			//Read the numbers in lines
			int firstElNumber = Integer.parseInt(firstLine.substring(firstLine.lastIndexOf(':')+2, firstLine.length()-1));
			int secondElNumber = Integer.parseInt(secondLine.substring(secondLine.lastIndexOf(':')+2, secondLine.length()-1));
			int thirdElNumber = Integer.parseInt(thirdLine.substring(thirdLine.lastIndexOf(':')+2, thirdLine.length()-1));
			int fourthElNumber = Integer.parseInt(fourthLine.substring(fourthLine.lastIndexOf(':')+2, fourthLine.length()-1));
			
			//Increase the selected floors number
			switch(floorNumber){
				case 0 :{
					firstElNumber ++; break;
				}
				case 1 :{
					secondElNumber ++; break;
				}	
				case 2 :{
					thirdElNumber ++; break;
				}	
				case 3 :{
					fourthElNumber ++; break;
				}	
			}
			
			firstLine = firstLine.substring(0, firstLine.lastIndexOf(':')+2) + firstElNumber ;
			secondLine = secondLine.substring(0, secondLine.lastIndexOf(':')+2) + secondElNumber ;
			thirdLine = thirdLine.substring(0, thirdLine.lastIndexOf(':')+2) + thirdElNumber ;
			fourthLine = fourthLine.substring(0, fourthLine.lastIndexOf(':')+2) + fourthElNumber ;
			
			//Write it to the same file
			FileWriter fil = new FileWriter(fileName);
			fil.write(firstLine + " \n");
			fil.write(secondLine + " \n");
			fil.write(thirdLine + " \n");
			fil.write(fourthLine + " \n");
			fil.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void writeLogs(String log){
		try {
			fw.append(log);
			fw.append("\r\n");
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
