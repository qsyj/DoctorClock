package com.wqlin.clock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wqlin.clock.ui.MainActivity;
import com.wqlin.clock.utils.AppUtils;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/15 9:18
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_BOOT)) {
            Toast.makeText(context, "DoctorClock开机启动", Toast.LENGTH_LONG).show();
            Intent oot = new Intent(context, MainActivity.class);
            oot.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(oot);
            AppUtils.gotoHome(context);
        }
    }
}
