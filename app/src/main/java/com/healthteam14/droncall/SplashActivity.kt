package com.healthteam14.droncall

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.healthteam14.droncall.ui.activities.FirebaseAuthUIActivity

class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, FirebaseAuthUIActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTimeOut)

    }

}