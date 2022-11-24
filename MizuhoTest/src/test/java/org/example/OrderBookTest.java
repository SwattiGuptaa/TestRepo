package org.example;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

class OrderBookTest {

    private OrderBook orderBook;
    @BeforeEach
    void setUp() {
        List<Order> bidOrders = new LinkedList<> (Arrays.asList(
                new Order(1,100.0, 'B', 10),
                new Order(2,200.0, 'B', 3),
                new Order(9,200.0, 'B', 3),
                new Order(12,2000.0, 'B', 16)
            )
        );

        List<Order> offerOrders = new LinkedList<> (Arrays.asList(
                new Order(3,400.0, 'O', 10),
                new Order(4,200.0, 'O', 3),
                new Order(5,200.0, 'O', 6)
             )
        );
        orderBook = new OrderBook(bidOrders,offerOrders);
    }


    @Test
    void testAddOrder() {
        orderBook.addOrder( new Order(6,500.0, 'B', 6) );
        Assertions.assertEquals(orderBook.getBids().size(), 5);
    }

    @Test
    void testRemoveOrder() {
        orderBook.removeOrder(5);
        MatcherAssert.assertThat(orderBook.getOffers(), not(hasItem(
                orderBook.getOffers().stream().filter(order -> order.getId() == 5).findFirst().orElse(null))));
    }

    @Test
    void testUpdateOrder() throws NoSuchElementException {
        orderBook.updateOrder(5, 9);
        Optional<Order> updatedOrder =  orderBook.getOffers().stream().filter(order -> order.getId() == 5).findFirst();
        if(updatedOrder.isPresent()) {
            Assertions.assertEquals(updatedOrder.get().getSize(), 9);
        }
    }

    @Test
    void testUpdateOrderToInvalidOrder() {
        NoSuchElementException thrown = Assertions.assertThrows(NoSuchElementException.class,
                () -> orderBook.updateOrder(16, 9), "Invalid OrderId passed");
        Assertions.assertEquals("Invalid OrderId passed", thrown.getMessage());
    }

    @Test
    void testGetPriceForSideAndLevel() throws InvalidLevelException {
        Assertions.assertEquals(
                orderBook.getPriceForSideAndLevel('B', 3), 100.0);

        Assertions.assertEquals(
                orderBook.getPriceForSideAndLevel('O', 2), 400.0);
    }

    @Test
    void testGetPriceForInvalidLevel() {
        InvalidLevelException thrown = Assertions.assertThrows(InvalidLevelException.class,
                () -> orderBook.getPriceForSideAndLevel('B', 15), "Level doesn't exist");
        Assertions.assertEquals("Level doesn't exist", thrown.getMessage());
    }

    @Test
    void testGetTotalSizeForSideAndLevel() throws InvalidLevelException {
        Assertions.assertEquals(
                orderBook.getTotalSizeForSideAndLevel('O',1), 9);
    }

    @Test
    void testGetOrdersOfSideProvided() {
        List<Order> expectedOfferOrders = new LinkedList<> (Arrays.asList(
                new Order(4,200.0, 'O', 3),
                new Order(5,200.0, 'O', 6),
                new Order(3,400.0, 'O', 10)
            ));
            Assertions.assertEquals(orderBook.getOrdersOfSideProvided('O'), expectedOfferOrders);

    }
}