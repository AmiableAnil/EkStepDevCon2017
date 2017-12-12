package org.ekstep.devcon.game;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.ekstep.devcon.R;
import org.ekstep.devcon.game.models.QuestionModel;

import java.util.List;

/**
 * @author vinayagasundar
 */

public class QRScanActivity extends AppCompatActivity
        implements DecoratedBarcodeView.TorchListener {

    private static final int REQUEST_CODE_CAMERA = 486;

    private DecoratedBarcodeView mBarcodeView;
    private ImageView mSwitchFlashLightButton;

    private BeepManager mBeepManager;

    private GameEngine mGameEngine;

    private String mLastText;
    private boolean mIsTouchOn = false;

    private QuestionModel mQuestionModel;

    private BarcodeCallback mCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(mLastText)) {
                // Prevent duplicate scans
                return;
            }

            mLastText = result.getText();
            mBeepManager.playBeepSoundAndVibrate();

            try {
                mGameEngine.verifyQR(mLastText);
            } catch (GameException e) {
                Toast.makeText(QRScanActivity.this, "Wrong QR Code",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };
    private QuestionDetailDialogFragment questionDetailDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        mBarcodeView = findViewById(R.id.barcode_scanner);
        mBarcodeView.setTorchListener(this);

        GameEngine.initGame(this, new OnGameInitiatedListener() {
            @Override
            public void onGameInitiated() {

            }

            @Override
            public void nextHint(String hint) {
                questionDetailDialogFragment.dismiss();
                showHint(hint);
            }

            @Override
            public void nextQuestion(QuestionModel questionModel) {
                mQuestionModel = questionModel;
                showQuestionProgress();
            }

            @Override
            public void gameCompleted() {
                questionDetailDialogFragment.dismiss();
                Toast.makeText(QRScanActivity.this, "You're Winner", Toast.LENGTH_LONG)
                        .show();
            }
        });

        mGameEngine = GameEngine.getEngine();
        startQRScanning();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQRScanning();
            } else {
                Toast.makeText(this, "Camera Permission is request",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
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
        mSwitchFlashLightButton.setImageResource(R.drawable.ic_flash_on_white_24dp);
        mIsTouchOn = true;
    }

    @Override
    public void onTorchOff() {
        mSwitchFlashLightButton.setImageResource(R.drawable.ic_flash_off_white_24dp);
        mIsTouchOn = false;
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
        questionDetailDialogFragment = QuestionDetailDialogFragment.newInstance(mQuestionModel);
        questionDetailDialogFragment.show(getSupportFragmentManager(),
                QuestionDetailDialogFragment.class.toString());
    }

    private void showHint(String hintText) {
        final Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_hint, null);

        TextView questionText = view.findViewById(R.id.hint_text);
        View button = view.findViewById(R.id.scan_qr_code);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        questionText.setText(hintText);

        dialog.setContentView(view);
        dialog.show();
    }

    public void switchFlashlight(View view) {
        if (!mIsTouchOn) {
            mBarcodeView.setTorchOn();
        } else {
            mBarcodeView.setTorchOff();
        }
    }

    private void startQRScanning() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            return;
        }

        mBarcodeView.decodeContinuous(mCallback);

        mBeepManager = new BeepManager(this);

        mSwitchFlashLightButton = findViewById(R.id.switch_flashlight);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            mSwitchFlashLightButton.setVisibility(View.GONE);
        }

        mBarcodeView.resume();
    }


}
