package megvii.testfacepass.pa.beans;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;


/**
 * Created by Administrator on 2018/5/31.
 */
@Entity
public class Subject  {


    public Subject() {

    }

    @Id(assignable = true)
    private Long id;
    private String person_id;
    private String person_name;
    private String person_type;
    private String fingerTemplate;
    private String id_card;
    private String ic_card;
    private String sex;
    private String birthday;
    private String start_time;
    private String end_time;
    private int count;
    private String group_id;
    private String group_name;
    private String group_type;
    private String teZhengMa;



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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getTeZhengMa() {
        return teZhengMa;
    }

    public void setTeZhengMa(String teZhengMa) {
        this.teZhengMa = teZhengMa;
    }


}
