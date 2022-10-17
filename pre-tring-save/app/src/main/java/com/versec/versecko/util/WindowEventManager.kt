package com.versec.versecko.util

import android.app.Activity
import android.view.WindowManager

class WindowEventManager {

    companion object {

        fun blockUserInteraction (activity: Activity) {

            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        fun openUserInteraction (activity: Activity) {

            activity.window.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }
}