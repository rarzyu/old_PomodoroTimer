package com.rarzyu.pomodorotimer.Common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdBanner(modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            AdView(context).apply {
                adUnitId = "ca-app-pub-7531671018761430/1829244269"
//                adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test
                setAdSize(AdSize.BANNER)
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { view ->
            // Update view.
        }
    )
}

//fun loadInterstitial(){
//    var interstitial: InterstitialAd? = null
//    val adRequest = AdRequest.Builder().build()
//    InterstitialAd.load(
//        this,
//        "ca-app-pub-7531671018761430/8282971970",
//        adRequest,
//        object : InterstitialAdLoadCallback(){
//            //広告ロード成功時
//            override fun onAdLoaded(p0: InterstitialAd) {
//                interstitial = p0
//                interstitial?.fullScreenContentCallback = object : FullScreenContentCallback(){
//
//                    //広告を閉じた時
//                    override fun onAdDismissedFullScreenContent() {
//                    }
//
//                    //広告表示成功時
//                    override fun onAdShowedFullScreenContent() {
//                        interstitial = null
//                    }
//                }
//            }
//
//            //広告ロード失敗
//            override fun onAdFailedToLoad(p0: LoadAdError) {
//                interstitial = null
//            }
//        }
//    )
//}