package com.wifisecure.unlockeez;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdkUtils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Utils {
    public static MaxInterstitialAd sMaxInterstitialAd;
    @SuppressLint("StaticFieldLeak")
    public static MaxAdView sMaxBannerAd;
    public static String INTER = "ea73034de715a1ac";
    public static String BANNER = "8851d35dea24574c";

    private static int tryAdAttempt;

    public static int ADS_COUNTER = 0;


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null && cm.getActiveNetworkInfo() != null) && cm
                .getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static void loadMaxInterstitialAd(Activity activity) {
        try {
            if (sMaxInterstitialAd == null) {
                sMaxInterstitialAd = new MaxInterstitialAd(INTER, activity);
                sMaxInterstitialAd.setListener(new MaxAdListener() {
                    @Override
                    public void onAdLoaded(MaxAd maxAd) {
                        tryAdAttempt =0;
                    }

                    @Override
                    public void onAdDisplayed(MaxAd maxAd) {

                    }

                    @Override
                    public void onAdHidden(MaxAd maxAd) {

                    }

                    @Override
                    public void onAdClicked(MaxAd maxAd) {

                    }

                    @Override
                    public void onAdLoadFailed(String s, MaxError maxError) {
                        tryAdAttempt++;
                        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, tryAdAttempt)));
                        new Handler().postDelayed(() -> sMaxInterstitialAd.loadAd(), delayMillis);
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                        sMaxInterstitialAd.loadAd();
                    }
                });
                sMaxInterstitialAd.loadAd();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showInterstitialAd() {
        try {
            ADS_COUNTER++;
            if (ADS_COUNTER == 3) {
                if (sMaxInterstitialAd != null && sMaxInterstitialAd.isReady()) {
                    sMaxInterstitialAd.showAd();
                }
                ADS_COUNTER = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showBannerAds(RelativeLayout adContainer, Activity activity) {

        sMaxBannerAd = new MaxAdView(BANNER, activity);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;

        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize(activity).getHeight();
        int heightTabletDp = MaxAdFormat.LEADER.getAdaptiveSize(activity).getHeight();
        //int heightPx = AppLovinSdkUtils.dpToPx(activity, heightDp);
        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? heightTabletDp : heightDp);

        sMaxBannerAd.setLayoutParams(new RelativeLayout.LayoutParams(width, heightPx));
        sMaxBannerAd.setExtraParameter("adaptive_banner", "true");
        adContainer.addView(sMaxBannerAd);
        sMaxBannerAd.startAutoRefresh();
        sMaxBannerAd.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                // adContainer.removeAllViews();

            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
            }
        });

        sMaxBannerAd.loadAd();
    }
}
