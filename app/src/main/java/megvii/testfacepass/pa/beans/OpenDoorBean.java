package megvii.testfacepass.pa.beans;

public class OpenDoorBean {


    /**
     * resultCode : yes001
     * resultMsg : 欢迎尊贵会员李女士光临
     * output : true
     * success : true
     * code : 0
     * message : 处理成功
     */

    private String resultCode;
    private String resultMsg;
    private boolean output;
    private boolean success;
    private int code;
    private String message;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public boolean isOutput() {
        return output;
    }

    public void setOutput(boolean output) {
        this.output = output;
    }

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
