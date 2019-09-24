package UCF;

/*
Name: Rober Stein
Course: CNT 4714 Spring 2019
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 17, 2019
Class: <RoutingStation>
*/

public class RoutingStation implements Runnable
{
    private int workLoad=0, totalStations, inputConveyorID,stationID,outputConveyorID;
    private ConveyorModel inputConveyor, outputConveyor;

	public RoutingStation(int stationID,int workLoad, int totalStations)
	{
		this.totalStations = totalStations;
        this.workLoad = workLoad;
        this.stationID = stationID;
		setConveyors();
	}

    public void setConveyors()
    {
        this.setInputConveyorID();
        this.setOutputConveyorID();
        System.out.println("Station " + stationID + ": Workload set. Station "
                + this.stationID + " has " + this.workLoad + " package groups to move");
    }

    public void doWork()
    {
        --this.workLoad;
        this.inputConveyor.onInputConn(this.stationID);
        this.outputConveyor.onOutputConn(this.stationID);
        System.out.println("Station " + this.stationID + ": has " + this.workLoad + " package groups left to move");
    }

    public void sleepSimulate(int time)
    {
        try
        {
            Thread.sleep(time);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	@Override
	public void run() {

		while(this.workLoad>0)
		{
			if(inputConveyor.getReentrantLock().tryLock())
			{
				System.out.println("Station " + this.stationID + ": granted access to conveyor " + this.inputConveyorID);
				if(outputConveyor.getReentrantLock().tryLock())
				{
					System.out.println("Station " + this.stationID + ": granted access to conveyor " + this.outputConveyorID);
					doWork();
				}
				else
				{
                    inputConveyor.getReentrantLock().unlock();
					System.out.println("Station " + this.stationID + ": released access to conveyor " + this.inputConveyorID);
					sleepSimulate(1200);
				}

				if(inputConveyor.getReentrantLock().isHeldByCurrentThread())
				{
                    inputConveyor.getReentrantLock().unlock();
					System.out.println("Station " + this.stationID + ": released access to conveyor " + this.inputConveyorID);
				}
				
				if(outputConveyor.getReentrantLock().isHeldByCurrentThread())
				{
                    outputConveyor.getReentrantLock().unlock();
					System.out.println("Station " + this.stationID + ": released access to conveyor " + this.outputConveyorID);
				}
				sleepSimulate(1200);
			}
		}
		System.out.println("\n* * Station " + stationID + ": workload successfully completed. * *\n");
	}

    public int getOutputConveyorID()
    {
        return outputConveyorID;
    }

    public void setOutputConveyorID()
    {
        this.outputConveyorID = this.stationID ==0 ?this.totalStations-1:this.stationID;
        System.out.println("Station " + stationID + ": Out-Connection set to conveyor " + this.outputConveyorID);
    }

    public int getInputConveyorID()
    {
       return inputConveyorID;
    }

    public void setInputConveyorID()
    {
        this.inputConveyorID = this.stationID ==0 ? 0:this.stationID - 1;
        System.out.println("Station " + stationID + ": In-Connection set to conveyor " + this.inputConveyorID);
    }

    public void setInputConveyor(ConveyorModel inputConveyor)
	{
		this.inputConveyor = inputConveyor;
	}

	public void setOutputConveyor(ConveyorModel outputConveyor)
	{
		this.outputConveyor = outputConveyor;
	}
}
