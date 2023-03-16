import java.util.Comparator;
import java.util.concurrent.Callable;

public class Order implements Comparable {
    public String id;
    public String deliveryAddress;
    public Time orderTime;
    public Time preparationDoneTime;
    public Time latestDeliveryTime;

    String getData(String s, String marker){
        int markerIndex = s.indexOf(marker);
        int markerComma = s.indexOf(",", markerIndex);
        return s.substring(markerIndex + marker.length(), markerComma);
    }
    String getData(String s, String marker, String endMarker){
        int markerIndex = s.indexOf(marker);
        int markerComma = s.indexOf(endMarker, markerIndex);
        return s.substring(markerIndex + marker.length(), markerComma);
    }

    public Order(String ord){
        this.id = getData(ord, "Order ID: ");
        this.deliveryAddress = getData(ord, "Delivery Address: ");
        orderTime = new Time(getData(ord, "Time Placed: "));
        int minutesToOrder = Integer.valueOf(getData(ord, "Estimated Time to Prepare: ", " minutes"));
        preparationDoneTime = new Time(orderTime);
        preparationDoneTime.add(minutesToOrder);
        latestDeliveryTime = new Time(preparationDoneTime);
        latestDeliveryTime.add(30);
    }


    @Override
    public int compareTo(Object o) {
        if(o instanceof Order ord){
            if(preparationDoneTime.equals(ord.preparationDoneTime)) return 0;
            return (preparationDoneTime.before(ord.preparationDoneTime)) ? -1 : 1;
        }
        return 1;
    }

    @Override
    public String toString(){
        return "Order: " + id + ", Order Time: " + orderTime + ", Preparation Done: " + preparationDoneTime + ", Delivery Address: " + deliveryAddress + "\n";
    }
}
