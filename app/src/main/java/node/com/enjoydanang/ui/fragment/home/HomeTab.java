package node.com.enjoydanang.ui.fragment.home;

/**
 * Created by chientruong on 2/6/17.
 */

public enum HomeTab {
    Home,Search,Profile,None;

    public static HomeTab getCurrentTab(int index){
        HomeTab homeTab = Home;
        switch (index){
            case 0:
                homeTab = Home;
                break;
            case 1:
                homeTab = Search;
                break;
            case 2:
                homeTab = Profile;
                break;
            case 3:
                homeTab = None;
                break;
        }
        return homeTab;
    }

}
