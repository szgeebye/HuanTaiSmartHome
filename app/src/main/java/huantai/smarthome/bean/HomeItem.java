package huantai.smarthome.bean;

import com.google.gson.annotations.Expose;
import com.orm.dsl.Table;

/**
 * Created by lj_xwl on 2017/9/8.
 */
@Table
public class HomeItem {
//    @SerializedName("id")
//    @Expose
    public int position;
    @Expose
    public String name;
    @Expose
    public String content;
    @Expose
    public int picture;

    public HomeItem(){

    }

    public HomeItem(String name, String content, int picture) {
        this.name = name;
        this.content = content;
        this.picture = picture;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "HomeItem{" +
                "position=" + position +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", picture=" + picture +
                '}';
    }
}
