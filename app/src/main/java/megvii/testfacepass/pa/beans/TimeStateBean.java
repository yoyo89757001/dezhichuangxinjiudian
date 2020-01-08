package megvii.testfacepass.pa.beans;

public class TimeStateBean {


    /**
     * success : true
     * code : 0
     * message : 已更新最新设备运行状态
     */

    private boolean success;
    private int code;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
