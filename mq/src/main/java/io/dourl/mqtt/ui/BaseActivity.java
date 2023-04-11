package io.dourl.mqtt.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import java.util.List;

import io.dourl.mqtt.R;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static Handler sHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        methodRequiresTwoPermission();
    }

    public static void runOnUi(Runnable runnable) {
        sHandler.post(runnable);
    }

    protected void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, 200);
                    return;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
          //  checkPermission();
        }
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, "returned_from_app_settings_to_activity", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "允许", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "禁止", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
            //弹出个对话框 可以自定义
        }
    }

    public static final int RC_CAMERA = 1; // requestCode
    public static final int RC_AUDIO = 2; // requestCode
    public static final int RC_STORAGE = 3; // requestCode
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        cameraTask();
        storageTask();
    }
    /**
     * 单个申请
     */
    public void cameraTask() {
        if(hasCameraPermission()){
            Toast.makeText(this, "TODO: Camera things", Toast.LENGTH_LONG).show();
        }else{
            EasyPermissions.requestPermissions(this, "申请相机权限",
                    RC_CAMERA, Manifest.permission.CAMERA);
        }
    }
    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA);
    }

    private boolean hasStoragePermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    public void storageTask() {

        if(hasStoragePermission()){
            Toast.makeText(this, "TODO: 存储 things", Toast.LENGTH_LONG).show();
        }else{
            EasyPermissions.requestPermissions(this, "申请存储权限",
                    RC_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }



}