package ServiceLayer.ServiceObjects;


import java.util.ArrayList;
import java.util.List;

public class categoryS {
    private String Name;

    private String categoryType;
    private List<productS> productList;

    public categoryS(String name, String categoryType){
        this.Name = name;
        this.productList = new ArrayList<>();
        this.categoryType = categoryType;
    }
}
