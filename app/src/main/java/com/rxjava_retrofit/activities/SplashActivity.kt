package com.rxjava_retrofit.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rxjava_retrofit.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        var intent:Intent= Intent()
        intent.setClass(SplashActivity@this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }




}