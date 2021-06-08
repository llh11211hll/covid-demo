package com.example.dao;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity//一个“疫情信息”实体
@Data
public class COVIDinfo implements Serializable
{
    @Id//表的主键
    private String id;//

    private String name;//地区名
    private String confirm;//多种病例数
    private String storeConfirm;
    private String heal;
    private String died;
    private String asymptomatic;
    private String lastUpdateTime;//时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getStoreConfirm() {
        return storeConfirm;
    }

    public void setStoreConfirm(String storeConfirm) {
        this.storeConfirm = storeConfirm;
    }

    public String getHeal() {
        return heal;
    }

    public void setHeal(String heal) {
        this.heal = heal;
    }

    public String getDied() {
        return died;
    }

    public void setDied(String died) {
        this.died = died;
    }

    public String getAsymptomatic() {
        return asymptomatic;
    }

    public void setAsymptomatic(String asymptomatic) {
        this.asymptomatic = asymptomatic;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    //构造方法
    public COVIDinfo()
    {

    }


    public COVIDinfo(String id,String confirm,String asymptomatic,String heal,String died,String storeConfirm,String name,String lastUpdateTime)
    {
        this.id=id;

        this.confirm=confirm;
        this.asymptomatic=asymptomatic;
        this.heal=heal;
        this.died=died;
        this.storeConfirm=storeConfirm;
        this.name=name;
        this.lastUpdateTime=lastUpdateTime;
    }

    //重写输出,也是往数据库里插入时的“顺序”
    @Override
    public String toString()
    {
        return id+"\t"+name+"\t"+confirm+"\t"+storeConfirm+"\t"+heal+"\t"+died+"\t"+asymptomatic+"\t"+lastUpdateTime;
    }


}
