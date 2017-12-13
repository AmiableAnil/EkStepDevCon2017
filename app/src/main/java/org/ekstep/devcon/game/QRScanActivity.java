package org.ekstep.devcon.game;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.ekstep.devcon.R;
import org.ekstep.devcon.customview.DonutProgress;
import org.ekstep.devcon.game.models.QuestionModel;
import org.ekstep.devcon.telemetry.ImpressionType;
import org.ekstep.devcon.telemetry.TelemetryBuilder;
import org.ekstep.devcon.telemetry.TelemetryHandler;
import org.ekstep.devcon.ui.HomeFragment;

import java.util.List;

/**
 * @author vinayagasundar
 */

public class QRScanActivity extends AppCompatActivity
        implements DecoratedBarcodeView.TorchListener {

    private static final int REQUEST_CODE_CAMERA = 486;
    private static final int GAME_TIME = 30;
    private DecoratedBarcodeView mBarcodeView;
    private ImageView mSwitchFlashLightButton;
    private View mHintContainerView;
    private BeepManager mBeepManager;
    private GameEngine mGameEngine;
    private String mLastText;
    private String mHint = null;
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

    private String getFormattedTimerText(int timeRemainingInSecs) {
        int minutes = timeRemainingInSecs / 60;
        int seconds = timeRemainingInSecs % 60;
        @SuppressLint("DefaultLocale") String str = String.format("%02d:%02d", minutes, seconds);
        return str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        final DonutProgress donutProgress = findViewById(R.id.donut_progress);
        donutProgress.setMax((int) (GameEngine.GAME_TIME / 1000));

        mBarcodeView = findViewById(R.id.barcode_scanner);

        mHintContainerView = findViewById(R.id.hint_view);
        mBarcodeView.setTorchListener(this);

        try {
            GameEngine.initGame(this, new OnGameInitiatedListener() {
                @Override
                public void onGameInitiated() {

                }

                @Override
                public void nextHint(String hint) {
                    questionDetailDialogFragment.dismiss();
                    mBarcodeView.resume();
                    showHint(hint);
                }

                @Override
                public void nextQuestion(QuestionModel questionModel) {
                    mHint = null;
                    mHintContainerView.setVisibility(View.GONE);
                    mQuestionModel = questionModel;
                    mBarcodeView.pause();
                    showQuestionProgress();
                }

                @Override
                public void gameCompleted() {
                    questionDetailDialogFragment.dismiss();
                    Intent intent = new Intent(HomeFragment.WINNER_ACTION);
                    LocalBroadcastManager.getInstance(QRScanActivity.this).sendBroadcast(intent);

                    TelemetryHandler.saveTelemetry(TelemetryBuilder.buildImpressionEvent("GameCompleted-Winner",
                            ImpressionType.VIEW, null));

                    finish();
                }

                @Override
                public void timeFinished() {
                    questionDetailDialogFragment.dismiss();
                    Toast.makeText(QRScanActivity.this, "Time over, you can't play again!!", Toast.LENGTH_LONG)
                            .show();
                    TelemetryHandler.saveTelemetry(TelemetryBuilder.buildImpressionEvent("GameCompleted-TimeOver",
                            ImpressionType.VIEW, null));

                    finish();
                }

                @Override
                public void timeLapse(long timeRemainingInSeconds) {
                    donutProgress.setDonut_progress(String.valueOf((int) (GameEngine.GAME_TIME / 1000) - timeRemainingInSeconds));
                    donutProgress.setText(getFormattedTimerText((int) timeRemainingInSeconds));
                }
            });
        } catch (GameException e) {
            Toast.makeText(QRScanActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

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
                Toast.makeText(this, "Camera Permission is required",
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
        mHint = hintText;
//        mHintView.setVisibility(View.VISIBLE);
        displayHint();
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

    private void displayHint() {
        if (TextUtils.isEmpty(mHint)) {
            return;
        }

        mHintContainerView.setVisibility(View.VISIBLE);

        TextView questionText = mHintContainerView.findViewById(R.id.hint_text);
        questionText.setText(mHint);
    }


    public void showHint(View view) {
        displayHint();
    }
}
