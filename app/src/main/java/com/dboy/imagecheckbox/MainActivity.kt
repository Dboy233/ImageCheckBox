package com.dboy.imagecheckbox

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dboy.image.check.box.ImageCheckBox

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mImageCheckBox = findViewById<ImageCheckBox>(R.id.mImageCheckBox)
        mImageCheckBox.setOnCheckBoxChangeListener {
            Log.d("ImageCheckBox", "check->$it ")
        }
        mImageCheckBox.setOnClickListener {
            Log.d("ImageCheckBox", "onClick ${mImageCheckBox.isCheck}")
        }
        //设置未选中 通知回调
        mImageCheckBox.setCheck(false, true)
        //设置选中 不通知回调
        mImageCheckBox.setCheck(true, false)
    }

    fun onWaitClick(view: View) {
        //设置等待状态 不通知回调
//        mImageCheckBox.setWait(!mImageCheckBox.isWait, false)
//        设置等待状态 通知回调 当isWait==false的时候才会通知选中和未选中状态
        val mImageCheckBox = findViewById<ImageCheckBox>(R.id.mImageCheckBox)
        mImageCheckBox.setWait(!mImageCheckBox.isWait, true)
    }
}
