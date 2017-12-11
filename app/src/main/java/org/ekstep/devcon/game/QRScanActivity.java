package org.ekstep.devcon.game;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.ekstep.devcon.R;

import java.util.List;

/**
 * @author vinayagasundar
 */

public class QRScanActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private DecoratedBarcodeView mBarcodeView;
    private Button mSwitchFlashLightButton;

    private BeepManager mBeepManager;

    private String mLastText;

    private BarcodeCallback mCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(mLastText)) {
                // Prevent duplicate scans
                return;
            }

            mLastText = result.getText();
            mBeepManager.playBeepSoundAndVibrate();

            showQuestionProgress();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        mBarcodeView = findViewById(R.id.barcode_scanner);
        mBarcodeView.setTorchListener(this);

        mBarcodeView.decodeContinuous(mCallback);

        mBeepManager = new BeepManager(this);

        mSwitchFlashLightButton = findViewById(R.id.switch_flashlight);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            mSwitchFlashLightButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBarcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBarcodeView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTorchOn() {
        mSwitchFlashLightButton.setText(R.string.turn_off_flashlight);
    }

    @Override
    public void onTorchOff() {
        mSwitchFlashLightButton.setText(R.string.turn_on_flashlight);
    }

    /**
     * Check if the device's camera has a Flashlight.
     *
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void showQuestionProgress() {
        if (TextUtils.isEmpty(mLastText)) {
            return;
        }

        QuestionDetailDialogFragment questionDetailDialogFragment =
                QuestionDetailDialogFragment.newInstance(mLastText);
        questionDetailDialogFragment.show(getSupportFragmentManager(),
                QuestionDetailDialogFragment.class.toString());
    }

    public void switchFlashlight(View view) {
        if (getString(R.string.turn_on_flashlight).equals(mSwitchFlashLightButton.getText())) {
            mBarcodeView.setTorchOn();
        } else {
            mBarcodeView.setTorchOff();
        }
    }


}
