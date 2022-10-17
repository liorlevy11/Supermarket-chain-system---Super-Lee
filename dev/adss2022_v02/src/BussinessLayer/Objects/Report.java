package BussinessLayer.Objects;

import java.sql.Date;

public abstract class Report {
    //Report date
    private Date ReportDate;
    private int ReportID;
    public Report(Date someDate, int ID){
        this.ReportDate = someDate;
        this.ReportID = ID;
    }
    public int getReportID(){
        return this.ReportID;
    }
    public Date getReportDate(){
        return this.ReportDate;
    }
}
