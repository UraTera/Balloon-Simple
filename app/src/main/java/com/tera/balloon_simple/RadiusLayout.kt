package com.tera.balloon_simple

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.Px

/**
 * Обрезает углы внутренних макетов в зависимости от размера радиуса.
 */
class RadiusLayout @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attr, defStyle) {

  /** path для сглаживания угла контейнера. */
  private val path = Path()

  /** Радиус скругления углов  */
  var radius: Float by viewProperty(0f)

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int, ) {
    super.onSizeChanged(w, h, oldw, oldh)

    path.apply {
      addRoundRect(
          RectF(0f, 0f, w.toFloat(), h.toFloat()),
        radius,
        radius,
        Path.Direction.CW,
      )
    }
  }

  override fun dispatchDraw(canvas: Canvas) {
    canvas.clipPath(path)
    super.dispatchDraw(canvas)
  }
}