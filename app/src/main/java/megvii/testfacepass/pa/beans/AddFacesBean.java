package megvii.testfacepass.pa.beans;

import java.util.List;

public class AddFacesBean {

    /**
     * method : sync-person
     * person_list : ["Member_18356015"]
     * path : http://113.92.35.143:9001/person-list
     * notify : http://113.92.35.143:9001/person-notify
     * params : {"Hid":"","Ids":["Member_18356015"]}
     */

    private String method;
    private String path;
    private String notify;
    private ParamsBean params;
    private List<String> person_list;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public List<String> getPerson_list() {
        return person_list;
    }

    public void setPerson_list(List<String> person_list) {
        this.person_list = person_list;
    }

    public static class ParamsBean {
        /**
         * Hid :
         * Ids : ["Member_18356015"]
         */

        private String Hid;
        private List<String> Ids;

        public String getHid() {
            return Hid;
        }

        public void setHid(String Hid) {
            this.Hid = Hid;
        }

        public List<String> getIds() {
            return Ids;
        }

        public void setIds(List<String> Ids) {
            this.Ids = Ids;
        }
    }
}
