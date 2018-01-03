package node.com.enjoydanang.utils.event;

import android.content.DialogInterface;

/**
 * Author: Tavv
 * Created on 23/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface OnBackFragmentListener {
    void onBack(boolean isBack);
    void onDismiss(DialogInterface dialogInterface, boolean isBack, boolean isNeedRefresh);
}
