package application;

import orders.Order;
import requests.CreateNewOrderRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Singleton class that handles the CRUD operations for orders
 *
 * Class holds a map to keep hold of the order
 */
public class OrderDataManager {
    static OrderDataManager instance;
    final Map<String, Order> orders = new HashMap<>();
    int idCounter = -1;

    private OrderDataManager() {
    }

    /**
     * Singleton instance of the Order Data Manager
     * @return
     */
    public static OrderDataManager getInstance() {
        if (instance == null) {
            instance = new OrderDataManager();
        }
        return instance;
    }

    /**
     * Method to create a order
     * @param createNewOrderRequest new order request
     * @return ref number
     */
    public String createOrder(final CreateNewOrderRequest createNewOrderRequest) {
        //Get a new order ref number
        final String orderRefNumber = getNewOrderRefNumber();

        //Create a new order object
        final Order order = new Order(orderRefNumber, createNewOrderRequest);

        //Add the new order to the map of orders
        orders.put(orderRefNumber, order);
        return orderRefNumber;
    }

    /**
     * Find and return the order for a given order ref number
     * If no order can be found return null
     * @param orderRefNumber unique order ref number
     * @return order or null
     */
    public Order getOrder(final String orderRefNumber){
        return orders.getOrDefault(orderRefNumber, null);
    }

    /**
     * Get the next order ref number
     * @return new order ref number
     */
    private String getNewOrderRefNumber() {
        //Increment the Id Counter
        idCounter++;
        return "" + idCounter;
    }

    /**
     * Return a copy list of orders
     * @return list of orders
     */
    public List<Order> getOrders() {
        return orders.values().stream().map(Order::new).collect(Collectors.toList());
    }
}
