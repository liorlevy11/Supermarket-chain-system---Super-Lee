package ServiceLayer.ServiceObjects;

import javafx.util.Pair;

import java.util.Date;
import java.util.Map;

public class ServiceOrder {
    private int orderId;
    private Date orderDate;
    private Map<Pair<Integer, Integer>, Integer> itemCount;

    private double totalPriceBeforeDiscount;
    private double totalPriceAfterDiscount;


    public ServiceOrder(int orderId, Date orderDate, Map<Pair<Integer, Integer>, Integer> itemCount, double totalPriceBeforeDiscount, double totalPriceAfterDiscount) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.itemCount = itemCount;

        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }


    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Map<Pair<Integer, Integer>, Integer> getItemCount() {
        return itemCount;
    }

    public void setItemCount(Map<Pair<Integer, Integer>, Integer> itemCount) {
        this.itemCount = itemCount;
    }

    public double getTotalPriceAfterDiscount() {
        return totalPriceAfterDiscount;
    }

    public void setTotalPriceAfterDiscount(double totalPriceAfterDiscount) {
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }

    public double getTotalPriceBeforeDiscount() {
        return totalPriceBeforeDiscount;
    }

    public void setTotalPriceBeforeDiscount(double totalPriceBeforeDiscount) {
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
    }

    public int getOrderId() {
        return orderId;
    }
}
