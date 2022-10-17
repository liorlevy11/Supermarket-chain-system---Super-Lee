package ServiceLayer.ServiceObjects;

import java.sql.Date;
import java.util.Map;

enum ReportType {
    Defective,
    Inventory,
    Oredr
}

public class reportS {
    private Date ReportDate;
    private int ReportID;
    private Map<Integer, Integer> products;
    ReportType type;

    public reportS(Date someDate, int ID, Map<Integer, Integer> ProductList, String kind) {
        this.ReportDate = someDate;
        this.ReportID = ID;
        this.products = ProductList;
        if (kind.equals("Defective")) {
            type = ReportType.Defective;
        } else if (kind.equals("Inventory")) {
            type = ReportType.Inventory;
        } else {
            type = ReportType.Oredr;
        }
    }

    public StringBuilder getProductsString() {
        StringBuilder ret = new StringBuilder();
        for (int i : products.keySet()) {
            ret.append(i).append(" ").append(products.get(i)).append("\n");
        }
        return ret;
    }

    public String toString(){
        StringBuilder ret = new StringBuilder();
        for (int i : products.keySet()) {
            ret.append("product ID: ").append(i).append(" quantity: ").append(products.get(i)).append("\n");
        }
        return ret.toString();
    }
    public int getReportID(){
        return this.ReportID;
    }
    public Date getReportDate(){
        return this.ReportDate;
    }
    public String getProductString(){
        String ret = "";
        for(int i:products.keySet()){
            if(ret.isEmpty()){
                ret = i+" "+products.get(i);
            }
            ret = ret+","+i+" "+products.get(i);
        }
        return ret;
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }
}
