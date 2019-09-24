package UCF;

/*
Name: <Robert Stein>
Course: CNT 4714 Spring 2019
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 17, 2019
Class: <FacilityManager>
*/

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FacilityManager {
	static int stationsCount;
    static List<ConveyorModel> conveyorList =new ArrayList<ConveyorModel>();
    static List<RoutingStation> routingStationList =new ArrayList<RoutingStation>();
	static List<Integer> workLoadCounts =new ArrayList<Integer>();
    static String configFile = "config.txt";
	public static void main(String[] args)
	{
		System.out.println("\n");
		System.out.println("* * * SYSTEM BEGINS * * * \n\n");
		System.out.println("* * * SYSTEM ENDS * * * \n\n");
		
        try
        {
            Scanner stations = new Scanner(new File(configFile));
            stationsCount = stations.nextInt();
            for(int i = 0; i < stationsCount; i++)
                conveyorList.add(new ConveyorModel(i));
            ExecutorService simulator = Executors.newFixedThreadPool(stationsCount);
            for(int i = 0; i < stationsCount; i++)
            {
               simulateProcess(i,simulator);
            }
            simulator.shutdown();
            stations.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
	}

    public static void simulateProcess(int i,ExecutorService simulator)
    {
        workLoadCounts.add(i);
        int currLoad= workLoadCounts.get(i);
        routingStationList.add(new RoutingStation(i,currLoad, stationsCount));
        RoutingStation currRoutingStation = routingStationList.get(i);
        currRoutingStation.setInputConveyor(conveyorList.get(currRoutingStation.getInputConveyorID()));
        currRoutingStation.setOutputConveyor(conveyorList.get(currRoutingStation.getOutputConveyorID()));
        try
        {
            simulator.execute(currRoutingStation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
