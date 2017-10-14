
package node.com.enjoydanang.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chien on 10/8/17.
 */

public class MenuItem {
    @SerializedName("name")
    private String name;
    @SerializedName("icon")
    private String icon;

    public MenuItem(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}