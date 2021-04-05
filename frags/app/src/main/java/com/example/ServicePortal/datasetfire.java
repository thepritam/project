package com.example.ServicePortal;

public class datasetfire {

    private Long Assigned;
    private Long Completed;
    private String Category;
    private String AssignedId;
    private String CId;
    private String Cname;
    private String Description;
    private double Latitude;
    private double Longitude;
    private double DropLatitude;
    private double DropLongitude;
    private double PickupLatitude;
    private double PickupLongitude;
    private Long PaidCommission;
    private Long PaidFull;
    private String mUid;
    private String AudioDescription;
    private int isCancelled;
    private String OrderDescriptionID;
    private String QuotationID;
    private String InvoiceID;

    public String getAudioDescription() { return AudioDescription; }

    public void setAudioDescription(String AudioDescription) {
        this.AudioDescription = AudioDescription;
    }

    public double getDropLatitude() {
        return DropLatitude;
    }

    public void setDropLatitude(double DropLatitude) {
        this.DropLatitude = DropLatitude;
    }

    public double getDropLongitude() {
        return DropLongitude;
    }

    public void setDropLongitude(double DropLongitude) {
        this.DropLongitude = DropLongitude;
    }

    public double getPickupLatitude() {
        return PickupLatitude;
    }

    public void setPickupLatitude(double PickupLatitude) {
        this.PickupLatitude = PickupLatitude;
    }

    public double getPickupLongitude() {
        return PickupLongitude;
    }

    public void setPickupLongitude(double PickupLongitude) { this.PickupLongitude = PickupLongitude;  }

    public String getOrderDescriptionID() { return OrderDescriptionID; }

    public void setOrderDescriptionID(String OrderDescriptionID) { this.OrderDescriptionID = OrderDescriptionID; }

    public String getQuotationID() { return InvoiceID; }

    public void setOrderInvoiceID(String OrderDescriptionID) { this.InvoiceID = InvoiceID; }

    public String getInvoiceID() { return InvoiceID; }

    public void setQuotationID(String QuotationID) { this.QuotationID = QuotationID; }

    public int getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(int isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Long getAssigned() {
        return Assigned;
    }

    public String getUid() { return mUid; }

    public void setUid(String uid) { mUid = uid; }

    public String getCname() { return Cname; }

    public void setCname(String cname) { Cname = cname; }

    public void setAssigned(Long assigned) {
        Assigned = assigned;
    }

    public Long getCompleted() {
        return Completed;
    }

    public void setCompleted(Long completed) {
        Completed = completed;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getAssignedId() {
        return AssignedId;
    }

    public void setAssignedId(String assignedId) {
        AssignedId = assignedId;
    }

    public String getCId() {
        return CId;
    }

    public void setCId(String CId) {
        this.CId = CId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Long longitude) {
        Longitude = longitude;
    }

    public Long getPaidCommission() {
        return PaidCommission;
    }

    public void setPaidCommission(Long paid) {
        PaidCommission = paid;
    }

    public Long getPaidFull() {
        return PaidFull;
    }

    public void setPaidFull(Long PaidFull) {
        this.PaidFull = PaidFull;
    }


    public datasetfire() {
    }

    public datasetfire(Long assigned,String assignedId, String AudioDescription, String CId, String category, Long completed,String description,double latitude,double longitude,Long paid , Long PaidFull, int isCancelled) {
        Assigned = assigned;
        AssignedId = assignedId;
        this.AudioDescription = AudioDescription;
        this.CId = CId;
        Category = category;
        Completed = completed;
        Description = description;
        Latitude = latitude;
        Longitude = longitude;
        PaidCommission = paid;
//        this.mUid = mUid;
        this.PaidFull = PaidFull;
        this.isCancelled = isCancelled;

    }
}