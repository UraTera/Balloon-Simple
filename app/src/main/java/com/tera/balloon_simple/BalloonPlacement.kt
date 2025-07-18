package com.tera.balloon_simple

import android.view.View

data class BalloonPlacement(
    val anchor: View,
    val xOff: Int = 0,
    val yOff: Int = 0
    )
