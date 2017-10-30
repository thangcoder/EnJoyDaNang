package node.com.enjoydanang.utils.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import node.com.enjoydanang.model.Contact;
import node.com.enjoydanang.utils.event.OnDrawableClickListener;
import node.com.enjoydanang.utils.event.OnItemClickListener;

import static node.com.enjoydanang.R.id.editText;

/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class CustomEditText extends AppCompatEditText {

    private final int DRAWABLE_LEFT = 0;

    private final int DRAWABLE_RIGHT = 2;

    private OnDrawableClickListener onClickListener;


    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomEditText(Context context, OnDrawableClickListener onClickListener) {
        super(context);
        this.onClickListener = onClickListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (getRight() - getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                onClickListener.onDrawableRightClick(this);
                return true;
            } else if(event.getRawX() <= (getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())){
                onClickListener.onDrawableLeftClick(this);
                return true;
            }
        }
        return false;
    }
}
