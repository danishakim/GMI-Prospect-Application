package my.edu.gmi.prospect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import my.edu.gmi.prospect.data.CourseRepository;

/** Branded splash screen: animates the logo while course data is pre-loaded. */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 2200;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable openHome = this::openHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.splash_logo);
        Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.fade_up);
        findViewById(R.id.logo_container).startAnimation(logoAnim);
        findViewById(R.id.txt_app_name).startAnimation(textAnim);
        findViewById(R.id.txt_tagline).startAnimation(textAnim);

        // Load the JSON course data in the background so the next screens open instantly
        new Thread(() -> CourseRepository.getInstance(getApplicationContext())).start();

        handler.postDelayed(openHome, SPLASH_DELAY_MS);
    }

    private void openHome() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(openHome);
        super.onDestroy();
    }
}
