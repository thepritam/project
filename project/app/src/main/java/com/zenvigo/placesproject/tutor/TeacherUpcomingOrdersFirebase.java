package com.project.placesproject.tutor;




public class TeacherUpcomingOrdersFirebase {


    private String Category;
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

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public TeacherUpcomingOrdersFirebase() {
    }

    public TeacherUpcomingOrdersFirebase(String Category,String OrderId,long timestamp,long Completed,long Cancelled) {
        this.Category = Category;
        this.OrderId = OrderId;
        this.timestamp = timestamp;
        this.Completed = Completed;
        this.Cancelled = Cancelled;
    }
}