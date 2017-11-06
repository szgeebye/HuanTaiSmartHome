package huantai.smarthome.bean;

import com.orm.dsl.Table;

/**
 * Created by lj_xwl on 2017/11/6.
 */
@Table
public class Alertinfo {

    public String alertcontent;//报警记录内容
    public String alerttime;//报警记录时间

    public Alertinfo(String alertcontent, String alerttime) {
        this.alertcontent = alertcontent;
        this.alerttime = alerttime;
    }

    public String getAlertcontent() {
        return alertcontent;
    }

    public void setAlertcontent(String alertcontent) {
        this.alertcontent = alertcontent;
    }

    public String getAlerttime() {
        return alerttime;
    }

    public void setAlerttime(String alerttime) {
        this.alerttime = alerttime;
    }

    @Override
    public String toString() {
        return "Alertinfo{" +
                "alertcontent='" + alertcontent + '\'' +
                ", alerttime='" + alerttime + '\'' +
                '}';
    }
}
