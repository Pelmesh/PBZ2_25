package sample;

import java.sql.Date;
import java.time.LocalDate;

public class Data {
    private int id_region;
    private int code;
    private int id;
    private double purchase_price;
    private double selling_price;
    private String city;
    private String area;
    private String type;
    private String name;
    private String name_product;
    private String sort;
    private LocalDate date;

    public Data(double purchase_price, double selling_price, LocalDate date, String name_product) {
        this.date=date;
        this.name_product=name_product;
        this.selling_price=selling_price;
        this.purchase_price=purchase_price;
    }

    public Data(double purchase_price, double selling_price,  int id) {
        this.id=id;
        this.selling_price=selling_price;
        this.purchase_price=purchase_price;
    }

    public double getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(double purchase_price) {
        this.purchase_price = purchase_price;
    }

    public double getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Data(int id, String name, String name_product, double purchase_price, double selling_price, LocalDate date,
                String name_employee,String position_employee) {
        this.id=id;
        this.name=name;
        this.name_product=name_product;
        this.purchase_price=purchase_price;
        this.selling_price=selling_price;
        this.date=date;
        this.name_employee=name_employee;
        this.position_employee=position_employee;
    }

    public Data(double purchase_price, double selling_price,String name, String name_product, LocalDate date) {
        this.purchase_price=purchase_price;
        this.selling_price=selling_price;
        this.name=name;
        this.name_product=name_product;
        this.date=date;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private String group;

    public Data(int id, String city, String area) {
        this.id=id;
        this.city=city;
        this.area=area;
    }

    public Data(int id, int code, String name, String sort, String group, String type) {
         this.id=id;
         this.code=code;
         this.name=name;
         this.sort=sort;
         this.group=group;
         this.type=type;
    }                                           

    public int getId_region() {
        return id_region;
    }

    public void setId_region(int id_region) {
        this.id_region = id_region;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName_employee() {
        return name_employee;
    }

    public void setName_employee(String name_employee) {
        this.name_employee = name_employee;
    }

    public String getPosition_employee() {
        return position_employee;
    }

    public void setPosition_employee(String position_employee) {
        this.position_employee = position_employee;
    }

    private String address;
    private String telephone;
    private String name_employee;     
    private String position_employee;

    public Data(int id, int code, String type, String name, String address, String telephone, int id_region) {
        this.id=id;
        this.code=code;
        this.type=type;
        this.name=name;
        this.address=address;
        this.telephone=telephone;
        this.id_region=id_region;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
