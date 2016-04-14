package net.sourceforge.simcpux.util;

import android.content.Context;

/**
 * Created by ren on 2016/4/14 0014.
 */
public class ToastUtil {
    /**
     * 在子线程显示一个短时间的Toast
     * @param activity
     * @param msg
     */
    public static void shortToastInBackgroundThread(Context activity, String msg) {
//        Looper.prepare();
//        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
//        Looper.loop();
    }
}
