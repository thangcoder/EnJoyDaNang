package node.com.enjoydanang.ui.fragment.contact;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Contact;

/**
 * Author: Tavv
 * Created on 26/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface ContactUsView extends iBaseView {

    void sendContactSuccess();

    void sendContactFailure(AppError error);

    void onGetInformation(Contact contact);

    void onGetInforFailure(AppError error);
}
