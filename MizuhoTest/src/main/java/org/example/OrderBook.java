package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class OrderBook {

    private final List<Order> bids ;
    private final List<Order> offers ;

    public OrderBook(List<Order> bids, List<Order> offers) {
        this.bids = bids;
        this.offers = offers;
    }

    public List<Order> getBids() {
        return bids;
    }

    public List<Order> getOffers() {
        return offers;
    }

    /**
     * Method to add Order based on side
     * @param o - Order to be added to Order book
     */
    public void addOrder(Order o){
        if(o.getSide() == 'B'){
            bids.add(o);
        } else {
            offers.add(o);
        }
    }

    /**
     * Method to remove order based on order id provided, if order Id is not present no action will be taken
     * @param orderId - unique id of the order
     */
    public void removeOrder(long orderId){

        Order bidsOrder = bids.stream().filter(order -> order.getId() == orderId).findFirst().orElse(null);
        Order offersOrder;
        if (bidsOrder != null){
            bids.remove(bidsOrder);
        }else {
            offersOrder = offers.stream().filter(order -> order.getId() == orderId).findFirst().orElse(null);
            offers.remove(offersOrder);
        }
    }

    /**
     * Method to update order based on order Id provided
     * @param orderId - unique id of the order
     * @param size - size of the order
     * @throws NoSuchElementException - if order id provided is not present exception is thrown
     */
    public void updateOrder(long orderId, long size) throws NoSuchElementException {

        Order bidsOrder = bids.stream().filter(order -> order.getId() == orderId).findFirst().orElse(null);
        Order offersOrder = offers.stream().filter(order -> order.getId() == orderId).findFirst().orElse(null);

        if (bidsOrder != null){
            bidsOrder.setSize(size);
        }else if(offersOrder != null) {
            offersOrder.setSize(size);
        }else{
           throw new NoSuchElementException("Invalid OrderId passed");
        }
    }

    /**
     * This method maps the orders based on level and time(which is based in order ids)
     * Level-1 is used as index to the Map to get the desired level price for a provided side
     * For example, given side=B and level=2 return the second best bid price
     */
    public double getPriceForSideAndLevel(char side, int level) throws InvalidLevelException {
        Map<Double, List<Order>> mapBasedOnLevel;
        if(side == 'B'){
            mapBasedOnLevel = mapOrdersBasedOnLevel(bids);
        } else{
            mapBasedOnLevel = mapOrdersBasedOnLevel(offers);
        }
        if(level > mapBasedOnLevel.size())
            throw new InvalidLevelException("Level doesn't exist");
        else
            return (double) mapBasedOnLevel.keySet().toArray()[level - 1];

    }

    private Map<Double, List<Order>> mapOrdersBasedOnLevel(List<Order> toBeMappedBasedOnLevel) {
        return toBeMappedBasedOnLevel.stream()
                .sorted()
                .collect(Collectors.groupingBy(Order::getPrice, LinkedHashMap::new, Collectors.toCollection(LinkedList::new)));
    }

    /**
     *  Returns total size for the provided side and level
     *  Given a side and a level return the total size available for that level
     */
    public long getTotalSizeForSideAndLevel(char side, int level) throws InvalidLevelException {
        double priceAtLevel =  getPriceForSideAndLevel(side,level);

        if(side == 'B'){
            return bids.stream().filter(o -> o.getPrice() == priceAtLevel).mapToLong(Order::getSize).sum();
        } else{
            return offers.stream().filter(o -> o.getPrice() == priceAtLevel).mapToLong(Order::getSize).sum();
        }

    }

    //Given a side return all the orders from that side of the book, in level- and time-order
    public List<Order> getOrdersOfSideProvided(char side){
        if(side == 'B'){
            Collections.sort(bids);
            return bids;
        }else {
            Collections.sort(offers);
            return offers;
        }
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "bids=" + bids +
                ", offers=" + offers +
                '}';
    }
}
