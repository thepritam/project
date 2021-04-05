package com.project.placesproject;


public class UpcomingOrderFirebase {


    private String ShopName;
    private String Price;
    private String RiderOrder;
    private String OrderId;
    private long timestamp;
    private long Completed;
    private long Cancelled;

    public long getCancelled(){
        return Cancelled;
    }
    public void setCancelled(long cancelled){
        this.Cancelled = cancelled;
    }

    public long getCompleted() {
        return Completed;
    }

    public void setCompleted(long completed) {
        Completed = completed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getRiderOrder() {
        return RiderOrder;
    }

    public void setRiderOrder(String riderOrder) {
        RiderOrder = riderOrder;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public UpcomingOrderFirebase() {
    }

    public UpcomingOrderFirebase(String ShopName, String Price, String RiderOrder,String OrderId,long timestamp,long Completed,long Cancelled) {
        this.ShopName = ShopName;
        this.Price = Price;
        this.RiderOrder = RiderOrder;
        this.OrderId = OrderId;
        this.timestamp = timestamp;
        this.Completed = Completed;
        this.Cancelled = Cancelled;
    }
}