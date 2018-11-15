package es.source.code.activity;

import java.io.Serializable;

public class PullFoodlistBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String icon_name;
    private int price;
    private boolean isadd;
    private int icon;

    public PullFoodlistBean(String icon_name, int price,
                                Boolean isadd, int icon) {
        super();
        this.icon_name = icon_name;
        this.price = price;
        this.isadd = isadd;
        this.icon = icon;
    }

    public String getIcon_name() {
        return icon_name;
    }

    public void setIcon_name(String icon_name) {
        this.icon_name = icon_name;
    }

    public int getPrice() {
        return price;
    }

    public Boolean getIsadd() {
        return isadd;
    }

    public void setIsadd(boolean isadd) {
        this.isadd = isadd;
    }

    public int  getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
