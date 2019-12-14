package sample;

public class Data {
    private int id_region;
    private int code;
    private int id;
    private String city;
    private String area;
    private String type;
    private String name;
    private String sort;

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

    public Data(int id, int code, String type, String name, String address, String telephone, String name_employee, String position_employee, int id_region) {
        this.id=id;
        this.code=code;
        this.type=type;
        this.name=name;
        this.address=address;
        this.telephone=telephone;
        this.name_employee=name_employee;
        this.position_employee=position_employee;
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
