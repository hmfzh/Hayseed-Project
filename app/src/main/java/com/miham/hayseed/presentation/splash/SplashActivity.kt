package com.miham.hayseed.presentation.splash

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.miham.hayseed.R
import com.miham.hayseed.presentation.login.LoginActivity
import com.miham.hayseed.presentation.onboard.BoardActivity
import com.miham.hayseed.presentation.onboard.OnBoardActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        delayAndGoToLogin()
    }

    private fun delayAndGoToLogin() {
        Handler(Looper.getMainLooper())
            .postDelayed({
                startActivity<OnBoardActivity >()
                finish()
            }, 1200)
    }
}