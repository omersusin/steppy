package com.omersusin.steppy

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            setText(R.string.build_check_message)
            textSize = 20f
            gravity = Gravity.CENTER
            val padding = (24 * resources.displayMetrics.density).toInt()
            setPadding(padding, padding, padding, padding)
            setOnApplyWindowInsetsListener { view, insets ->
                val bars = if (android.os.Build.VERSION.SDK_INT >= 30) {
                    insets.getInsets(android.view.WindowInsets.Type.systemBars())
                } else {
                    null
                }
                if (bars != null) {
                    view.setPadding(
                        padding + bars.left,
                        padding + bars.top,
                        padding + bars.right,
                        padding + bars.bottom,
                    )
                }
                insets
            }
        })
    }
}
