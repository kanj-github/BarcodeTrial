package apps.test.kanj.barcodetrial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private int count;
    private SwipeButton btn;
    private static final String PERMISSIONS[] = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (SwipeButton) findViewById(R.id.btn);
        btn.setOnSwipeListener(new SwipeButton.OnSwipeListener() {
            @Override
            public void onSwipe() {
                Log.v("Kanj", "works");
                startScan(btn);
            }
        });
        count = 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Kanj", "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check permissions for M concept
        boolean gotPermissions = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            gotPermissions = false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            gotPermissions = false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            gotPermissions = false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            gotPermissions = false;
        }

        if (!gotPermissions) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void startScan(View v) {
        // Launched stolen zxing app
        /*Intent intent = new Intent("com.opinio.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "ONE_D_MODE");
        startActivityForResult(intent, 0);*/
        // Launch ZXing app
        /*IntentIntegrator scanIntent = new IntentIntegrator(this);
        scanIntent.initiateScan();*/
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("Kanj", "Running on UI thread");
                            if (btn != null) {
                                btn.setText("Set");
                            } else {
                                Log.v("Kanj", "Button points to null");
                            }
                        }
                    });
                } catch (InterruptedException ie) {
                    Log.v("Kanj", "Sleep interrupted");
                }
            }
        }).start();
        Intent i = new Intent(this, TestService.class);
        i.putExtra("extra", ""+count++);
        startService(i);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String text = scanResult.getFormatName() + " - " + scanResult.getContents();
            Log.v("Kanj", text);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        } else {
            Log.v("Kanj", "Null result");
        }*/

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                String text = format + " - " + contents;
                Log.v("Kanj", text);
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Log.v("Kanj", "Cancelled");
            }
        }
    }
}
