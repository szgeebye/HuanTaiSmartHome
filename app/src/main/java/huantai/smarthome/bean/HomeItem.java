package huantai.smarthome.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.dsl.Table;

/**
 * Created by lj_xwl on 2017/9/8.
 */
@Table
public class HomeItem {
    @SerializedName("id")
    @Expose
    public Long id;
    @Expose
    public String name;
    @Expose
    public String content;
    @Expose
    public int picture;
    @Expose
    public boolean isDelete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public HomeItem(){

    }

    public HomeItem(String name, String content, int picture) {
        this.name = name;
        this.content = content;
        this.picture = picture;
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

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    @Override
    public String toString() {
        return "HomeItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", picture=" + picture +
                ", isDelete=" + isDelete +
                '}';
    }
}
