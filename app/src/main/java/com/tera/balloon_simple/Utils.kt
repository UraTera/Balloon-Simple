package com.tera.balloon_simple

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import kotlin.math.roundToInt

/** Получить Int значение в px из Int значения в dp. */
internal  val Int.dp: Int
    inline get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics,
    ).roundToInt()

/** Возвращает Float значение в px из Float значения в dp. */
internal  val Float.dp: Float
    inline get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics,
    )

/** Установить видимость  */
internal fun View.visible(shouldVisible: Boolean) {
    visibility = if (shouldVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}


/** определение не-значения типа Int. */
internal const val NO_INT_VALUE: Int = Int.MIN_VALUE

const val WRAP: Int = NO_INT_VALUE