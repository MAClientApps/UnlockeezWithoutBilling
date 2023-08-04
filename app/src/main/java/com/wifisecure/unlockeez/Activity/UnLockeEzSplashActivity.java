package com.wifisecure.unlockeez.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.wifisecure.unlockeez.R;
import com.wifisecure.unlockeez.UnLockeEzMainPageActivity;
import com.wifisecure.unlockeez.Utils;

import java.util.concurrent.TimeUnit;

public class UnLockeEzSplashActivity extends AppCompatActivity implements MaxAdListener, MaxAdRevenueListener {


    MaxInterstitialAd interstitialAd;
    int tryAdAttempt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_un_locke_ez);

        if (Utils.isNetworkAvailable(UnLockeEzSplashActivity.this)) {
            loadAds();
        } else {
            new Handler().postDelayed(this::gotoHome, 2000);
        }

    }


    public void loadAds() {
        interstitialAd = new MaxInterstitialAd(Utils.INTER, this);
        interstitialAd.setListener(this);
        interstitialAd.setRevenueListener(this);
        // Load the first ad.
        interstitialAd.loadAd();
    }

    public void gotoHome() {
        startActivity(new Intent(UnLockeEzSplashActivity.this, UnLockeEzMainPageActivity.class));
        finish();
    }

    @Override
    public void onAdLoaded(MaxAd maxAd) {
        if (interstitialAd.isReady()) {
            interstitialAd.showAd();
        }
    }

    @Override
    public void onAdDisplayed(MaxAd maxAd) {

    }

    @Override
    public void onAdHidden(MaxAd maxAd) {
        gotoHome();
    }

    @Override
    public void onAdClicked(MaxAd maxAd) {

    }

    @Override
    public void onAdLoadFailed(String s, MaxError maxError) {
        tryAdAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, tryAdAttempt)));
        new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);

    }

    @Override
    public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
        interstitialAd.loadAd();
    }

    @Override
    public void onAdRevenuePaid(MaxAd maxAd) {

    }
}