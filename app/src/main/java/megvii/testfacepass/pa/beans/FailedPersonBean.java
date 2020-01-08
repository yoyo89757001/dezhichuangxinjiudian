package megvii.testfacepass.pa.beans;

public class FailedPersonBean {

    public FailedPersonBean(String person_id, String reason) {
        this.person_id = person_id;
        this.reason = reason;
    }

    private String person_id;
    private String reason;

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
