package ServiceLayer.ServiceObjects;

import BussinessLayer.Objects.Site;
import BussinessLayer.Managers.SiteManager;

import java.util.HashMap;

public class SiteService {

    private final SiteManager siteManager;

    public SiteService(){
        this.siteManager=SiteManager.getInstance();
    }

    //add site
    public void AddSite(Integer idSite ,String contactPerson, String phoneNumber, String address,String shippingArea){
//siteManager
        siteManager.CreateSite(idSite,contactPerson, phoneNumber, address, shippingArea);
    }

    public HashMap<Integer, SiteS> DisplaySites() {
        HashMap<Integer, SiteS> siteS=new HashMap<>();
        HashMap<Integer, Site> sites=siteManager.getSites();
        for(Site s:sites.values()){
            siteS.put(s.getIdSite(),new SiteS(s));
        }
        return siteS;
    }

    public SiteS getSite(int id){
       return new SiteS(siteManager.getSite(id));
    }

    public void UpdateSiteContactPerson(int idSite,String contactPerson) {
        siteManager.UpdateSiteContactPerson(idSite,contactPerson);
    }

    public void UpdateSitePhoneNumber(int idSite,String phoneNumber) {
        siteManager.UpdateSitePhoneNumber(idSite, phoneNumber);
    }

    public void UpdateSiteAddress(int idSite,String address) {
        siteManager.UpdateSiteAddress(idSite, address);
    }

    public void UpdateSiteShippingArea(int idSite,String shippingArea) {
        siteManager.UpdateSiteShippingArea(idSite, shippingArea);
    }

    public void LoadData(){
        siteManager.LoadData();
    }

    public void DelData(){
        siteManager.DelData();
    }


    public void startingProgram() {
        siteManager.startingProgram();
    }
}
