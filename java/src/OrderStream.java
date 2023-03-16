import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class OrderStream {
    List<Order> orderStore;
    public List<Order> orders;
    int lastOrder = -1;
    public OrderStream(List<String> s){
        orderStore = new ArrayList<>();
        orders = new ArrayList<>();
        for(String ord : s){
            orderStore.add(new Order(ord));
        }
    }

    List<Order> updateTime(Time t){
        if(lastOrder >= orderStore.size() - 1) return orders;
        while(orderStore.get(lastOrder + 1).orderTime.equals(t)){
            lastOrder++;
            orders.add(orderStore.get(lastOrder));
            Collections.sort(orders);
            return orders;
        }
        return orders;
    }
}
