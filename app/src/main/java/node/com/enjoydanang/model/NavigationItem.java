package node.com.enjoydanang.model;

/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class NavigationItem {
    private String title;
    private int icon;
    private boolean isHeader;
    private boolean isVisible = true;
    private boolean isChecked = !isVisible;

    public NavigationItem(String title, int icon, boolean isHeader, boolean visible) {
        this.title = title;
        this.icon = icon;
        this.isHeader = isHeader;
    }

    public NavigationItem(String title, int icon, boolean header) {
        this(title, icon, header, true);
    }

    public NavigationItem(String title, int icon) {
        this(title, icon, false);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
