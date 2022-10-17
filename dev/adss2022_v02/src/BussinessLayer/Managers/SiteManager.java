package BussinessLayer.Managers;

import BussinessLayer.Objects.Contact;
import BussinessLayer.Objects.Site;
import BussinessLayer.Objects.Supplier;
import DataAccessLayer.SiteDAO;

import java.util.HashMap;
import java.util.List;

public class SiteManager {

    private static SiteManager siteManager = null;

    private SiteDAO siteDAO;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private SiteManager() {
        siteDAO = new SiteDAO();
    }

    // Static method
    // Static method to create instance of Singleton class
    public static SiteManager getInstance() {
        if (siteManager == null)
            siteManager = new SiteManager();

        return siteManager;
    }

    public void CreateSite(int idSite , String contactPerson, String phoneNumber, String address, String shippingArea) {
        Site site = new Site(idSite,contactPerson, phoneNumber, address, shippingArea);
        siteDAO.Insert(site);
    }

    public boolean deleteSite(int idSite){
        return siteDAO.Delete(getSite(idSite));
    }
    public Site getSite(int idSite) {
        return siteDAO.getSite(idSite);
    }

    public HashMap<Integer, Site> getSites() {
        List<Site> listOfSites = siteDAO.SelectAllSites();
        HashMap<Integer, Site> sites = new HashMap<>();
        for (Site site : listOfSites)
            sites.put(site.getIdSite(), site);
        return sites;
    }

    // update functions
    public void UpdateSiteContactPerson(int idSite, String contactPerson) {
        getSite(idSite).setContactPerson(contactPerson);
        siteDAO.setContactPerson(contactPerson, idSite);
    }

    public void UpdateSitePhoneNumber(int idSite, String phoneNumber) {
        getSite(idSite).setPhoneNumber(phoneNumber);
        siteDAO.setPhoneNumber(phoneNumber, idSite);
    }

    public void UpdateSiteAddress(int idSite, String address) {
        getSite(idSite).setAddress(address);
        siteDAO.setAddress(address, idSite);
    }

    public void UpdateSiteShippingArea(int idSite, String shippingArea) {
        getSite(idSite).setShippingArea(shippingArea);
        siteDAO.setShippingArea(shippingArea, idSite);
    }

    public void LoadData() {
        //creating only one site
        CreateSite(0,"Super-Lee manager", "0543012364", "Tel Aviv Hagana 12", "Center");
//        CreateSite(1,"Shay Kresner", "0543012364", "Tel Aviv Shaul st 11", "Center");
//        CreateSite(2,"Dina Agapov", "0508380280", "Beer Sheva Yizhak Rager 12", "South");
//        CreateSite(3,"Gal Hadar", "0521113399", "Herzliya Yarden 30", "Center");
//        CreateSite(4,"Tamuz Gindes", "0537070999", "Ashdod Haim Moshe Shapira 6 Street", "South");
//        CreateSite(5,"Lior Levy", "0541212121", "Jerusalem Yitzhak Navon 22 ", "Center");
//        CreateSite(6,"Noga Schwartz", "0506784321", "Haifa HaHagana Ave 58", "North");
//        CreateSite(7,"Assi Azar", "0547942134", "Be'er Sheva Yitzhak rager 89", "South");
    }

    public void DelData() {
        siteDAO.resetCache();
        siteDAO.Delete();
    }

    public void resetCounter() {
        Site.setCounter(siteDAO.getMaxid() + 1);
    }

    public void startingProgram() {
        resetCounter();
    }

    public void addSiteBySupplier(Supplier supplier, Contact contact,String shippingArea) {
        int id = supplier.getSupplierCard().getId();
        String contactName = contact.getName();
        String phone = contact.getPhoneNumber();
        String address = supplier.getSupplierCard().getAddress();
        Site site = new Site(id,contactName,phone,address,shippingArea);
        siteDAO.Insert(site);
    }

}
