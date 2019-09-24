package UCF;

/*
Name: <Robert Stein>
Course: CNT 4714 Spring 2019
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 17, 2019
Class: <ConveyorModel>
*/
import java.util.concurrent.locks.ReentrantLock;

public class ConveyorModel
{
	private int id;
    private ReentrantLock reentrantLock = new ReentrantLock();

    public ReentrantLock getReentrantLock() {
        return reentrantLock;
    }

    public void setReentrantLock(ReentrantLock reentrantLock) {
        this.reentrantLock = reentrantLock;
    }

    public void onInputConn(int stationID)
    {
        System.out.println("Station " + stationID + ": successfully moves packages on conveyor " + this.id);
    }

    public void onOutputConn(int stationID)
    {
        System.out.println("Station " + stationID + ": successfully moves packages on conveyor " + this.id);
    }

    public ConveyorModel(int conveyorID)
	{
		this.id = conveyorID;
	}
	

}
