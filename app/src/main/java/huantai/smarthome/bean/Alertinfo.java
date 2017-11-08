package huantai.smarthome.bean;

import com.orm.dsl.Table;

/**
 * Created by lj_xwl on 2017/11/6.
 */
@Table
public class Alertinfo {

    public String alertcontent;//报警记录内容
    public String alerttime;//报警记录时间
    public String bindgiz;//绑定的网关
    public Long id;
    public Alertinfo(){

    }


    public Alertinfo(String alertcontent, String alerttime, String bindgiz, Long id) {
        this.alertcontent = alertcontent;
        this.alerttime = alerttime;
        this.bindgiz = bindgiz;
        this.id = id;
    }

    public String getBindgiz() {
        return bindgiz;
    }

    public void setBindgiz(String bindgiz) {
        this.bindgiz = bindgiz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                ", bindgiz='" + bindgiz + '\'' +
                ", id=" + id +
                '}';
    }
}
