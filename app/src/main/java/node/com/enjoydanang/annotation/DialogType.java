package node.com.enjoydanang.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Author: Tavv
 * Created on 02/11/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, PARAMETER, FIELD, LOCAL_VARIABLE})
@IntDef({DialogType.INFO, DialogType.HELP, DialogType.WRONG, DialogType.SUCCESS, DialogType.WARNING})
public @interface DialogType {
    int INFO = 0;
    int HELP = 1;
    int WRONG = 2;
    int SUCCESS = 3;
    int WARNING = 4;
}
