package BussinessLayer.Objects;

public class Category {
    private String Name;
    private int Sale_ID;
    protected CategoryType categoryType;
    private int ID;
    public Category(int IDs, String name, CategoryType categoryType, int SaleID){
        this.ID = IDs;
        this.Name = name;
        this.categoryType = categoryType;
        this.Sale_ID = SaleID;
    }
    public int getID(){
        return this.ID;
    }
    public int getSale_ID(){
        return Sale_ID;
    }
    public void setSale_ID(int newS){
        this.Sale_ID = newS;
    }
    public String getCategoryTypeString(){
        if(categoryType== categoryType.Main){
            return "Main";
        }else if(categoryType== categoryType.Sub){
            return "Sub";
        }else if(categoryType== categoryType.SubSub){
            return "SubSub";
        }else{
            return "";
        }
    }

    public String getName(){
        return this.Name;
    }
    public void setName(String name){
        this.Name = name;
    }
}
