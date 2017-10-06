package node.com.enjoydanang;

/**
 * Created by quangphuoc on 10/16/16.
 */

public interface iBaseView {
    void showToast(String desc);

    void unKnownError();

    void showLoading();

    void hideLoading();
}
