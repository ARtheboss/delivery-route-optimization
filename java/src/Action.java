import java.util.ArrayList;
import java.util.List;

public class Action {
    final public int vehicleId;
    final public Time leaveTime;
    final public Time returnTime;
    final public List<Order> destinations;
    final public int distance;

    public Action(){
        vehicleId = 0;
        leaveTime = new Time();
        returnTime = new Time();
        destinations = new ArrayList<>();
        distance = 0;
    }

    public Action(int vi, Time lt, Time rt, List<Order> ds, int d){
        vehicleId = vi;
        leaveTime = lt;
        returnTime = rt;
        destinations = ds;
        distance = d;
    }

    @Override
    public String toString(){
        String s = "Action taken\n";
        s += "Vehicle: " + vehicleId + ", Leave Time: " + leaveTime.toString() + ", Return Time: " + returnTime.toString() + ", Distance: " + distance + "\n";
        s += destinations.toString();
        return s;
    }

}
