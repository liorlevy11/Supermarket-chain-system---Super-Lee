package BussinessLayer.Objects;



import java.util.regex.*;

public class Site {

    enum ShippingArea {
        North,
        Center,
        South
    }

    private static int counter = 1;
    private int idSite;
    private String contactPerson;
    private String phoneNumber;
    private String address;
    private ShippingArea shippingArea;

    public Site(String contactPerson, String phoneNumber, String address, String shippingArea) {
        this.idSite = counter;
        counter++;
        //System.out.println("The site id :" + idSite);
        this.contactPerson = contactPerson;
        setPhoneNumber(phoneNumber);
        setAddress(address);
        setShippingArea(shippingArea);
    }

    //load from db
    public Site(int idSite,String contactPerson, String phoneNumber, String address, String shippingArea) {
        this.idSite = idSite;
        this.contactPerson = contactPerson;
        ///setPhoneNumber(phoneNumber); No input test required
        this.phoneNumber=phoneNumber;
        // setAddress(address); No input test required
        this.address=address;
        setShippingArea(shippingArea);
    }

    public boolean SameShippingArea(Site.ShippingArea shippingArea) {
        return this.shippingArea.equals(shippingArea);
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!isValidMobileNo(phoneNumber))
            throw new IllegalArgumentException("Phone number should be valid");
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        if (address.length() < 2 || address.length() > 50)
            throw new IllegalArgumentException("address should be less then 50 letters and longer then 1 letters");
        this.address = address;
    }

    public void setShippingArea(String shippingArea) {
        if (shippingArea.equals("North"))
            this.shippingArea = ShippingArea.North;
        else if (shippingArea.equals("Center"))
            this.shippingArea = ShippingArea.Center;
        else if (shippingArea.equals("South"))
            this.shippingArea = ShippingArea.South;
        else throw new IllegalArgumentException("Shipping Area should be North|Center|South");

    }

    public static boolean isValidMobileNo(String str) {
        Pattern p = Pattern.compile("^\\d{10}$");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression for which
        // object of Matcher class is created
        Matcher m = p.matcher(str);

        // Returning boolean value
        return (m.matches());
    }

    public int getIdSite() {
        return idSite;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public ShippingArea getShippingArea() {
        return shippingArea;
    }

    public String getStringShippingArea() {
        return shippingArea.toString();
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public static void setCounter(int counter) {
        Site.counter = counter;
    }

    @Override
    public boolean equals(Object other){
        Site s=(Site) other;
        return idSite==s.getIdSite();
    }

    @Override
    public String toString() {
        return "Site{" +
                "idSite=" + idSite +
                ", contactPerson='" + contactPerson + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", shippingArea=" + shippingArea +
                '}';
    }





}
