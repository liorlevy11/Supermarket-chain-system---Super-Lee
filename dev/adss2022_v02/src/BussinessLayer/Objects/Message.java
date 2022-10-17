package BussinessLayer.Objects;

public class Message {
    private String job;
    private String message;
    private boolean read;
    private static int counter = 1;
    private int idMessage;

    public Message(String job, String message, boolean read) {
        idMessage = counter;
        counter ++;
        this.job = job;
        this.message = message;
        this.read = read;
    }
    public Message(int id,String job, String message, boolean read) {
        idMessage = id;
        this.job = job;
        this.message = message;
        this.read = read;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getIdMessage() {
        return idMessage;
    }
}
