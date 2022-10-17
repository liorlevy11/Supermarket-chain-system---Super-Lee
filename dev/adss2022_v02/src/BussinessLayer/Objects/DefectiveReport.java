package BussinessLayer.Objects;


import BussinessLayer.Managers.ProductManager;
import DataAccessLayer.Dal.SuperItemDAO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Pair{
    public int element1;
    public int element2;
    public Pair(int e1,int e2){
        this.element1 = e1;
        this.element2 = e2;
    }
}

public class DefectiveReport extends Report {
    //Map of defective items and the quantity of each of them
    Map<Integer, Integer> Defective;
    //Making the report in the constructor
    public DefectiveReport(Date someDate, int ID){
        super(someDate, ID);
        List<Pair> itemsToRemove = new ArrayList<Pair>();
        Defective = new HashMap<Integer, Integer>();
        for(Item itm: SuperItemDAO.getInstance().getAllSuperItems()){
            if(someDate.after(itm.getExpressionDate())|itm.getDefective()){
                itemsToRemove.add(new Pair(itm.getProductid(),itm.getId()));
                if(Defective.containsKey(itm.getProductid())){
                    Defective.replace(itm.getProductid(),Defective.get(itm.getProductid()),Defective.get(itm.getProductid())+1);
                }else{
                    Defective.putIfAbsent(itm.getProductid(),1);
                }
            }
        }
        for(Pair ProIt:itemsToRemove){
            ProductManager.getInstance().removeSpecificItem(ProIt.element1,ProIt.element2);
        }
    }
    public Map<Integer,Integer> getDefective(){
        return this.Defective;
    }
    public boolean ContainItem(int pro){
        return this.Defective.containsKey(pro);
    }
    public int getItemQuantity(int pro){
        return this.Defective.get(pro);
    }
}