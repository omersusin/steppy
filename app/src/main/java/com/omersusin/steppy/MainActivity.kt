package com.omersusin.steppy

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val basePadding = (24 * resources.displayMetrics.density).toInt()
        setContentView(TextView(this).apply {
            setText(R.string.build_check_message)
            textSize = 20f
            gravity = Gravity.CENTER
            setPadding(basePadding, basePadding, basePadding, basePadding)
            setOnApplyWindowInsetsListener { view, insets ->
                view.setPadding(
                    basePadding + insets.systemWindowInsetLeft,
                    basePadding + insets.systemWindowInsetTop,
                    basePadding + insets.systemWindowInsetRight,
                    basePadding + insets.systemWindowInsetBottom,
                )
                insets
            }
        })
    }
}
