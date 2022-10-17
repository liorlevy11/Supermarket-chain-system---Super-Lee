package ServiceLayer.ServiceObjects;

import BussinessLayer.Objects.Site;

public class SiteS {

    private final int idSite;
    private final String contactPerson;
    private final String phoneNumber;
    private final String address;
    private final String shippingArea;

    public SiteS(int idSite, String contactPerson, String phoneNumber, String address, String shippingArea) {
        this.idSite = idSite;
        this.contactPerson = contactPerson;
        this.phoneNumber=phoneNumber;
        this.address=address;
        this.shippingArea=shippingArea;
    }

    public SiteS(Site site){
        this.idSite=site.getIdSite();
        this.contactPerson=site.getContactPerson();
        this.phoneNumber=site.getPhoneNumber();
        this.address=site.getAddress();
        this.shippingArea=site.getStringShippingArea();
    }


    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return  idSite+") CONTACT PERSON: " + contactPerson  +
                " | PHONE NUMBER: " + phoneNumber  +
                " | ADDRESS: " + address +
                " | SHIPPING AREA: " + shippingArea ;
    }
}
