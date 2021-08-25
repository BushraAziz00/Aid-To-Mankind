package app.techsol.aidtomankind.Models;

public class OrdersModel {
    String id;
    String userid;
    String orderdate;
    String medname;
    String quantity;
    String price;
    String userlat;
    String orderstatus;

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }




    public String getUserlat() {
        return userlat;
    }

    public void setUserlat(String userlat) {
        this.userlat = userlat;
    }

    public String getUserlong() {
        return userlong;
    }

    public void setUserlong(String userlong) {
        this.userlong = userlong;
    }

    String userlong;

    public OrdersModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getMedname() {
        return medname;
    }

    public void setMedname(String medname) {
        this.medname = medname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public OrdersModel(String id, String userid, String orderdate, String medname, String quantity, String price, String userlat,  String userlong, String orderstatus) {
        this.id = id;
        this.userid = userid;
        this.orderdate = orderdate;
        this.medname = medname;
        this.quantity = quantity;
        this.price = price;
        this.userlat = userlat;
        this.orderstatus = orderstatus;
        this.userlong = userlong;
    }
}
