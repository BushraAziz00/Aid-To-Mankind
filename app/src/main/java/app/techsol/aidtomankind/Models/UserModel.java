package app.techsol.aidtomankind.Models;

public class UserModel {
    public UserModel() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public UserModel(String userid, String phoneno, String email, String address, String name, String password, String userlat, String userlong) {
        this.userid = userid;
        this.phoneno = phoneno;
        this.email = email;
        this.address = address;
        this.name = name;
        this.password = password;
        this.userlat = userlat;
        this.userlong = userlong;
    }

    String userid;
    String phoneno;
    String email;
    String address;
    String name;
    String password;
    String userlat;
    String userlong;

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public UserModel(String userid, String phoneno, String email, String address, String name, String password, String userlat, String userlong, String usertype) {
        this.userid = userid;
        this.phoneno = phoneno;
        this.email = email;
        this.address = address;
        this.name = name;
        this.password = password;
        this.userlat = userlat;
        this.userlong = userlong;
        this.usertype = usertype;
    }

    String usertype;

}
