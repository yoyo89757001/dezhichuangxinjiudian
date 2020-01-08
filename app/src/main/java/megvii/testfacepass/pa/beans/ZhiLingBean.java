package megvii.testfacepass.pa.beans;

import org.json.JSONObject;

import java.util.List;

public class ZhiLingBean {


    /**
     * person_list : [{"person_id":"Member_18443015","person_name":"李彬","person_type":"1","face_list":[{"img_url":"http://113.92.32.39:9001/Person.aspx/FaceImage?id=1845006","face_id":"1845006"}],"fingerTemplate":null,"id_card":null,"ic_card":null,"sex":"2","birthday":null,"valid_time":{"start_time":"2014-11-22 12:23:04","end_time":"2021-02-01 00:00:00","count":0},"group_id":"Member","group_name":"Member","group_type":"MQActionOwner"}]
     * success : true
     * code : 0
     * message : 同步成功
     */

    private boolean success;
    private int code;
    private String message;
    private List<PersonListBean> person_list;
    private JSONObject jsonObject;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
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

    public List<PersonListBean> getPerson_list() {
        return person_list;
    }

    public void setPerson_list(List<PersonListBean> person_list) {
        this.person_list = person_list;
    }

    public static class PersonListBean {
        /**
         * person_id : Member_18443015
         * person_name : 李彬
         * person_type : 1
         * face_list : [{"img_url":"http://113.92.32.39:9001/Person.aspx/FaceImage?id=1845006","face_id":"1845006"}]
         * fingerTemplate : null
         * id_card : null
         * ic_card : null
         * sex : 2
         * birthday : null
         * valid_time : {"start_time":"2014-11-22 12:23:04","end_time":"2021-02-01 00:00:00","count":0}
         * group_id : Member
         * group_name : Member
         * group_type : MQActionOwner
         */

        private String person_id;
        private String person_name;
        private String person_type;
        private String fingerTemplate;
        private String id_card;
        private String ic_card;
        private String sex;
        private String birthday;
        private ValidTimeBean valid_time;
        private String group_id;
        private String group_name;
        private String group_type;
        private List<FaceListBean> face_list;

        public String getPerson_id() {
            return person_id;
        }

        public void setPerson_id(String person_id) {
            this.person_id = person_id;
        }

        public String getPerson_name() {
            return person_name;
        }

        public void setPerson_name(String person_name) {
            this.person_name = person_name;
        }

        public String getPerson_type() {
            return person_type;
        }

        public void setPerson_type(String person_type) {
            this.person_type = person_type;
        }

        public String getFingerTemplate() {
            return fingerTemplate;
        }

        public void setFingerTemplate(String fingerTemplate) {
            this.fingerTemplate = fingerTemplate;
        }

        public String getId_card() {
            return id_card;
        }

        public void setId_card(String id_card) {
            this.id_card = id_card;
        }

        public String getIc_card() {
            return ic_card;
        }

        public void setIc_card(String ic_card) {
            this.ic_card = ic_card;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public ValidTimeBean getValid_time() {
            return valid_time;
        }

        public void setValid_time(ValidTimeBean valid_time) {
            this.valid_time = valid_time;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getGroup_type() {
            return group_type;
        }

        public void setGroup_type(String group_type) {
            this.group_type = group_type;
        }

        public List<FaceListBean> getFace_list() {
            return face_list;
        }

        public void setFace_list(List<FaceListBean> face_list) {
            this.face_list = face_list;
        }

        public static class ValidTimeBean {
            /**
             * start_time : 2014-11-22 12:23:04
             * end_time : 2021-02-01 00:00:00
             * count : 0
             */

            private String start_time;
            private String end_time;
            private int count;

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }

        public static class FaceListBean {
            /**
             * img_url : http://113.92.32.39:9001/Person.aspx/FaceImage?id=1845006
             * face_id : 1845006
             */

            private String img_url;
            private String face_id;

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getFace_id() {
                return face_id;
            }

            public void setFace_id(String face_id) {
                this.face_id = face_id;
            }
        }
    }
}
