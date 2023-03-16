import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    final static int NUMBER_OF_SCENARIOS = 2;
    final static Time OPEN_TIME = new Time("9:00 AM");
    final static Time CLOSE_TIME = new Time("5:00 PM");

    public static void main(String[] args) {
        int scenario = -1;
        Scanner s = new Scanner(System.in);
        while(!(0 <= scenario && scenario < NUMBER_OF_SCENARIOS)) {
            System.out.println("Scenario # (" + NUMBER_OF_SCENARIOS + " available): ");
            scenario = s.nextInt() - 1;
        }
        File ordersFile = new File("data/scenario" + scenario + "/orders.txt");
        File mapFile = new File("data/scenario" + scenario + "/map.txt");

        List<String> orderLines = new ArrayList<>();
        List<String> mapLines = new ArrayList<>();
        try {
            Scanner orderScanner = new Scanner(ordersFile);
            while (orderScanner.hasNext()) orderLines.add(orderScanner.nextLine());
            Scanner mapScanner = new Scanner(mapFile);
            while (mapScanner.hasNext()) mapLines.add(mapScanner.nextLine());
        }catch(FileNotFoundException e){
            System.out.println("File not found");
            System.exit(1);
        }

        OrderStream orderStream = new OrderStream(orderLines);
        System.out.println("Order Stream created");

        Map map = new Map(mapLines);
        System.out.println("Map created");

        Scheduler scheduler = new Scheduler(orderStream, map);
        List<Time> vehicleReturnTimes = new ArrayList<>();

        int distance = 0;

        for(Time currentTime = new Time(OPEN_TIME);
            !currentTime.equals(CLOSE_TIME);  currentTime.add(1)){
            //System.out.println(currentTime + ", Distance: " + distance);
            orderStream.updateTime(currentTime);
            List<Action> myActions = scheduler.getNextActions(vehicleReturnTimes, currentTime);
            //System.out.println(myActions);
            for(Action a : myActions){
                if(a.leaveTime.equals(currentTime)){
                    while(vehicleReturnTimes.size() <= a.vehicleId){
                        vehicleReturnTimes.add(new Time());
                    }
                    vehicleReturnTimes.set(a.vehicleId, a.returnTime);
                    orderStream.orders.removeAll(a.destinations);
                    System.out.println(a);
                    distance += a.distance;
                }
            }
        }
        System.out.println();
        System.out.println("Total distance: " + distance);
        System.out.println("Vehicles needed: " + vehicleReturnTimes.size());
    }
}