package BussinessLayer.Objects;

import javafx.util.Pair;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Order {
    private int orderId;
    private Date orderDate;
    private Map<Pair<Integer, Integer>, Integer> itemCount; // prodID,suppID - count
    private double totalPriceBeforeDiscount;
    private double totalPriceAfterDiscount;
    private boolean periodicOrder;
    private boolean isPlaced;
    private List<DayOfWeek> daysOfPeriodic;

    public Order(int orderId, Date orderDate, Map<Pair<Integer, Integer>, Integer> itemCount, double totalPriceBeforeDiscount, double totalPriceAfterDiscount, boolean periodicOrder, boolean isPlaced, List<DayOfWeek> daysOfPeriodic) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.itemCount = itemCount;
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
        this.periodicOrder = periodicOrder;
        this.isPlaced = isPlaced;
        this.daysOfPeriodic = daysOfPeriodic;
    }

    public Order(int orderId, Date orderDate, Map<Pair<Integer, Integer>, Integer> itemCount,boolean periodicOrder, boolean isPlaced, List<DayOfWeek> daysOfPeriodic) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.itemCount = itemCount;
        this.totalPriceBeforeDiscount = -1;
        this.totalPriceAfterDiscount = -1;
        this.periodicOrder = periodicOrder;
        this.isPlaced = isPlaced;
        this.daysOfPeriodic = daysOfPeriodic;
    }
    public Order(int orderId, Date orderDate, Map<Pair<Integer, Integer>, Integer> itemCount,boolean isPlaced) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.itemCount = itemCount;
        this.totalPriceAfterDiscount = -1;
        this.totalPriceBeforeDiscount = -1;
        this.periodicOrder = false;
        this.daysOfPeriodic = new LinkedList<>();
        this.isPlaced = isPlaced;
    }


    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isPeriodicOrder() {
        return periodicOrder;
    }

    public List<DayOfWeek> getDaysOfPeriodic() {
        return daysOfPeriodic;
    }

    public void setDaysOfPeriodic(List<DayOfWeek> daysOfPeriodic) {
        this.daysOfPeriodic = daysOfPeriodic;
    }

    public void setPeriodicOrder(boolean flag) {
        periodicOrder = flag;
    }

    public boolean getPeriodicOrder() {
        return periodicOrder;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setPlacedOrder(boolean placed) {
        isPlaced = placed;
    }

    public void setTotalPriceAfterDiscount(double totalPriceAfterDiscount) {
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }

    public void setTotalPriceBeforeDiscount(double totalPriceBeforeDiscount) {
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    public Date getOrderDate() {
        return orderDate;
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


    public double getTotalPriceBeforeDiscount() {
        return totalPriceBeforeDiscount;
    }

}
