package org.example;

import java.util.Objects;

public class Order implements Comparable<Order> {

    // Assuming Order Id is unique and incrementally generated
    private final long id;
    private final double price;
    private final char side; // B or O
    private long size;

    public Order(long id, double price, char side, long size) {
        this.id = id;
        this.price = price;
        this.side = side;
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public char getSide() {
        return side;
    }

    public long getSize() {
        return size;
    }

    public long getId() {
        return id;
    }

    public void setSize(long size) {
        this.size = size;
    }


    @Override
    public int compareTo(Order o) {
        if ( o.getSide() == 'B'){
            return Double.compare(o.price, price);
        } else{
            return -(Double.compare(o.price, price));
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Double.compare(order.price, price) == 0 && side == order.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, side);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", side=" + side +
                ", size=" + size +
                '}';
    }
}
