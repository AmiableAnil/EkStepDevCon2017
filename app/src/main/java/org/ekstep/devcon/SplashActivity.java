package org.ekstep.devcon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


/**
 * Created by swayangjit on 8/12/17.
 */

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        View view_ekstep = findViewById(R.id.img_ekstep);
        View view_text = findViewById(R.id.layout_text);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);
        view_text.setAnimation(bottomAnim);

        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.anim_from_top);
        view_ekstep.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    },2000);

}
}
