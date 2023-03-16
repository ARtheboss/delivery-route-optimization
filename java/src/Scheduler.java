import java.util.*;

public class Scheduler {

    final static double VEHICLE_SPEED = 15.0; // miles per hour
    final static int MAX_CONCURRENT_ORDERS = 3;
    OrderStream orderStream;
    Map map;

    public Scheduler(OrderStream os, Map m){
        orderStream = os;
        map = m;
    }

    // always optimal to try to complete as
    // many orders in one trip as possible
    // earliest orders always need to be completed first
    public List<Action> getNextActions(List<Time> vehicleReturnTimes, Time currentTime){
        vehicleReturnTimes = new ArrayList<>(vehicleReturnTimes);
        if(orderStream.orders.isEmpty()) return new ArrayList<>();
        int currentDriver = 0;
        List<Order> remainingOrders = new ArrayList<>(orderStream.orders);
        List<Action> toReturn = new ArrayList<>();
        while(!remainingOrders.isEmpty()){
            if(vehicleReturnTimes.size() <= currentDriver){
                vehicleReturnTimes.add(new Time());
            }
            List<Order> myOrders = new ArrayList<>();
            Action bestAction = new Action();
            for(int i = 0; i < remainingOrders.size(); i++){
                if(remainingOrders.get(i).preparationDoneTime.before(vehicleReturnTimes.get(currentDriver))) continue;
                myOrders.add(remainingOrders.get(i));
                Action action = checkPossible(myOrders, currentDriver);
                if(action == null) myOrders.remove(myOrders.size() - 1);
                else{
                    bestAction = action;
                    myOrders = action.destinations;
                }
                if(myOrders.size() >= MAX_CONCURRENT_ORDERS) break;
            }
            if(myOrders.size() == 0){
                if(vehicleReturnTimes.get(currentDriver).before(currentTime)){
                    System.out.println("Vehicles too slow to deliver orders on time!");
                    System.exit(2);
                }
                currentDriver += 1;
            }else{
                vehicleReturnTimes.set(currentDriver, bestAction.returnTime);
                remainingOrders.removeAll(bestAction.destinations);
                toReturn.add(bestAction);
            }
        }
        return toReturn;
    }

    private Action checkPossible(List<Order> orders, int currentDriver){
        Time leaveTime = new Time();
        for(Order o : orders) if(leaveTime.before(o.preparationDoneTime)) leaveTime = o.preparationDoneTime;
        List<List<Order>> allPermutations = generatePermutations(new ArrayList<>(), orders, -1);
        int bestDistance = Integer.MAX_VALUE;
        Action bestAction = new Action();
        for(List<Order> order : allPermutations){
            int result = possibleOrderCombination(order, leaveTime);
            if(result < bestDistance){
                bestDistance = result;
                Time returnTime = new Time(leaveTime);
                returnTime.add(distanceToTime(bestDistance));
                bestAction = new Action(
                        currentDriver,
                        leaveTime,
                        returnTime,
                        order,
                        bestDistance
                        );
            }
        }
        return (bestDistance == Integer.MAX_VALUE) ? null : bestAction;
    }

    private List<List<Order>> generatePermutations(List<Order> currentList, List<Order> left, int i){
        List<Order> cl = new ArrayList<>(currentList);
        List<Order> l = new ArrayList<>(left);
        if(i != -1) {
            cl.add(left.get(i));
            l.remove(i);
        }
        List<List<Order>> toReturn = new ArrayList<>();
        if(l.size() == 0){
            toReturn.add(cl);
        }else {
            for (int x = 0; x < l.size(); x++) {
                toReturn.addAll(generatePermutations(cl, l, x));
            }
        }
        return toReturn;
    }

    private int possibleOrderCombination(List<Order> orders, Time leaveTime){
        int distance = map.shortestDistance("Storefront", orders.get(0).deliveryAddress);
        Time currentTime = new Time(leaveTime);
        currentTime.add(distanceToTime(distance));
        for(int i = 1; i < orders.size(); i++){
            if(orders.get(i - 1).latestDeliveryTime.before(currentTime)) return Integer.MAX_VALUE;
            int nextDistance = map.shortestDistance(orders.get(i - 1).deliveryAddress,
                    orders.get(i).deliveryAddress);
            currentTime.add(distanceToTime(nextDistance));
            distance += nextDistance;
        }
        if(orders.get(orders.size() - 1).latestDeliveryTime.before(currentTime)) return Integer.MAX_VALUE;
        return distance + map.shortestDistance(orders.get(orders.size() - 1).deliveryAddress, "Storefront");
    }

    public int distanceToTime(int miles){
        double fixedTime = (double) miles / VEHICLE_SPEED * 60;
        return (int) Math.ceil(fixedTime);
    }

}
