package ServiceLayer.ServiceObjects;

import java.util.Date;

public class itemS {
    int id;
    private String place;
    private double buyPrice;

    private Date expressionDate;

    public itemS(String place,double buyPrice,int id, Date expressionDate){
        this.buyPrice = buyPrice;
        this.place = place;
        this.expressionDate = expressionDate;
    }
}
