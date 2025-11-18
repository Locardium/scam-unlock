package com.locardium.scamunlock;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setVersionInfo();
        Button killButton = findViewById(R.id.killCameraButton);
        killButton.setOnClickListener(v ->  killCamera());
    }

    private void setVersionInfo() {
        TextView appVersionText = findViewById(R.id.appVersion);
        TextView camVersionText = findViewById(R.id.camVersion);
        PackageManager pm = getPackageManager();

        // Version app
        try {
            PackageInfo pInfo = pm.getPackageInfo(getPackageName(), 0);
            appVersionText.setText("App: " + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            appVersionText.setText("App: Error");
        }

        // Version sm cam
        try {
            PackageInfo pInfo = pm.getPackageInfo("com.sec.android.app.camera", 0);
            camVersionText.setText("Samsung Camera: " + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            camVersionText.setText("Samsung Camera: No found");
        }
    }

    private void killCamera() {
        try {
            Process suProcess = Runtime.getRuntime().exec("su -c am force-stop com.sec.android.app.camera");

            int exitValue = suProcess.waitFor();

            if (exitValue == 0) {
                Toast.makeText(this, "Process killed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error (code: " + exitValue + ")", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error to execute 'su' command: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}