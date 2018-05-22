package com.fca.agenda.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by fabiano.alvarenga on 28/04/18.
 */

public class PermissionCheck{

    /**
     * The method responsible for verifying and requesting permission to read and write
     * to external storage.
     *
     * @param context
     * @return
     */
    public static boolean readAndWriteExternalStorage(Context context){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }else{
            return true;
        }

    }

}
