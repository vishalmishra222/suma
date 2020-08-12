package com.app.dusmile.recording.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionUtils {

    public static boolean IsPermissionEnabled(Context context, String permission)
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public static boolean IsPermissionsEnabled(Context context, String[] permissionsList)
    {
        for (String permission : permissionsList)
        {
            if (!IsPermissionEnabled(context, permission))
            {
                return false;
            }
        }

        return true;
    }
}
