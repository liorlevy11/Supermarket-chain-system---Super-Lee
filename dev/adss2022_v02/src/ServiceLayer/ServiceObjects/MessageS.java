package ServiceLayer.ServiceObjects;

public class MessageS {
    private int idMessage;
    private String job;
    private String message;
    private boolean read;

    public MessageS(int idMessage , String job, String message, boolean read) {
        this.idMessage = idMessage;
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
