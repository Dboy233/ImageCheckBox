package com.dboy.imagecheckbox

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mImageCheckBox.setOnCheckListener {
            Log.d("ImageCheckBox", "check = " + it)
        }
        mImageCheckBox.setCheckChangeListener(false)
    }

    fun onWaitClick(view: View) {
        mImageCheckBox.isWait = true
    }
}
