import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Simulator {
	private ElevatorInsidePanel eip;
	private ElevatorOutside eop;
	private int floorCount,elevatorNumber;
	private Person[] persons;
	private Person tmpPerson;
	private FileOperations fileOp = new FileOperations("logs");
	
	public Simulator(ElevatorInsidePanel eip, ElevatorOutside eop,int floorCount, int elevatorNumber){
		this.eip = eip;
		this.eop = eop;
		this.floorCount = floorCount;
		this.elevatorNumber = elevatorNumber;
		createRequests(floorCount);
	}
	
	/*public static void main(String[] args){
		
		Simulator sim = new Simulator(null,null);
		sim.createRequests(10);
	}
	*/
	//**********************************************************************//
	//							Inner Person Class							//
	class Person{
		private int waitFloor;
		private int targetFloor;
		
		public Person(int waitFloor, int targetFloor){
			this.targetFloor = targetFloor;
			this.waitFloor = waitFloor;
		}

		public int getWaitFloor() {
			return waitFloor;
		}
		
		public int getTargetFloor() {
			return targetFloor;
		}
	}
	//**********************************************************************//
	
	public void createRequests(int floorCount){
		floorCount = floorCount+1;
		Random rand = new Random();
		int requestCount = rand.nextInt(floorCount) + 3 ;
		persons = new Person[requestCount];
		Timer[] timers = new Timer[requestCount];
		
		int waitFloor, targetFloor;
		for(int i=0 ; i<requestCount ; i++){
			do{
				waitFloor = rand.nextInt(floorCount);
				targetFloor = rand.nextInt(floorCount);
			}while(waitFloor == targetFloor);
			
			persons[i] = new Person(waitFloor,targetFloor);
			final int tmp = i;
			timers[i]=new Timer();
			TimerTask tsk = new TimerTask() {
				boolean isInElevator = false;
				boolean isFirst = true;
				String curDat;
				String text;
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				@Override
				public void run() {
					Date currentDate = new Date();
					curDat = sdf.format(currentDate);
					if(isFirst){
						eop.simulateCall(persons[tmp].getWaitFloor());
						text = "Time : " + curDat + "\t| Elevator " + elevatorNumber + " -> " + tmp + " called from : " + persons[tmp].getWaitFloor()  ;
						fileOp.writeLogs(text);
						System.out.println(text);
						isFirst= false;
					}else{
						if(!isInElevator && eop.canEnter(persons[tmp].getWaitFloor())){
							text = "Time : " + curDat + "\t| Elevator " + elevatorNumber + " -> " + tmp + " entered" ;
							fileOp.writeLogs(text);
							System.out.println(text);
							try {
								Thread.sleep(500 + tmp*20 );
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							eop.enterElevator(persons[tmp].getWaitFloor());
							eip.clickCallButton(persons[tmp].getTargetFloor());
							isInElevator = true;
						}else if(isInElevator && eop.canEnter(persons[tmp].getTargetFloor())){
							text = "Time : " + curDat + "\t| Elevator " + elevatorNumber + " -> " + tmp + " got out from : " + persons[tmp].getTargetFloor() ;
							fileOp.writeLogs(text);
							System.out.println(text);
							try {
								Thread.sleep(500 + tmp*40);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							eop.getOutFromElevator(persons[tmp].getTargetFloor());
							cancel();
						}
					}
					
					
					
				}
			};
			timers[i].schedule(tsk, rand.nextInt(10000 + 5000*floorCount), 500);
			String txt = "Elevator : " + elevatorNumber + " -> " + i + ". Person curent: " + waitFloor + " - Target: " + targetFloor ;
			fileOp.writeLogs(txt);
			System.out.println(txt);
		}
		
		
		
		
	}
	
}
