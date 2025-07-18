package com.tera.balloon_simple

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.forEach
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.DefaultLifecycleObserver

/**
 * Balloon реализует настраиваемые всплывающие подсказки со стрелками и анимацией.
 *
 * @param context Контекст для создания и доступа к внутренним ресурсам.
 * @param builder [Balloon.Builder] для создания экземпляра [Balloon].
 */
class Balloon private constructor(
    private val context: Context,
    private val builder: Builder,
) : DefaultLifecycleObserver {

    /** Обозначает, отображается всплывающее окно или нет */
    var isShowing: Boolean = false
        private set

    /** Означает, что всплывающее окно уже уничтожено изнутри.. */
    private var destroyed: Boolean = false

    /** Обработчик для запуска [autoDismissRunnable] */
    private val handler: Handler by lazy(LazyThreadSafetyMode.NONE) {
        Handler(Looper.getMainLooper())
    }

    /** Исполняемый файл для закрытия всплывающего окна */
    private val autoDismissRunnable: AutoDismissRunnable by lazy(
        LazyThreadSafetyMode.NONE,
    ) { AutoDismissRunnable(this) }

    private var mView: View? = null
    private val mFrame: FrameLayout? = null
    private var main: FrameLayout
    private var balloonWrapper: FrameLayout
    private var balloonCard: FrameLayout
    private var balloonContent: FrameLayout
    private var balloonArrow: ImageView

    private var bodyWindow = PopupWindow()

    init {

        val inflater = LayoutInflater.from(context)
        mView = inflater.inflate(R.layout.body_layout, mFrame, false)
        main = mView!!.findViewById(R.id.main) // Корневой элемент
        balloonWrapper = mView!!.findViewById(R.id.balloonWrapper)
        balloonCard = mView!!.findViewById(R.id.balloonCard)
        balloonContent = mView!!.findViewById(R.id.balloonContent)
        balloonArrow = mView!!.findViewById(R.id.balloonArrow)

        bodyWindow = PopupWindow(
            main,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
        )
        createByBuilder()
    }

    private fun createByBuilder(){
        // 1. Инициализировать фон
        initializeBackground()
        // 3. Инициализировать окно Balloon
        initializeBalloonWindow()
        // 4. Инициализировать пользовательский макет
        initializeBalloonLayout()
        // 5. Инициализировать содержимое Balloon. Здесь определяется высота
        initializeBalloonContent()
        // 6. Инициализировать слушатель. Слашатель нужен. Без него высота нормальная
        //initializeBalloonListeners()

    }

    // 1. Инициализировать фон
    private fun initializeBackground() {
        with(balloonCard) {
            val radiusLayout = RadiusLayout(context)
            radiusLayout.radius = builder.cornerRadius
            ViewCompat.setElevation(this, builder.elevation)
            background = builder.backgroundDrawable ?: GradientDrawable().apply {
                setColor(builder.backgroundColor)
                cornerRadius = builder.cornerRadius
            }
            setPadding(
                builder.paddingLeft,
                builder.paddingTop,
                builder.paddingRight,
                builder.paddingBottom,
            )
        }
    }

    // 3. Инициализировать окно Balloon
    private fun initializeBalloonWindow() {
        with(this.bodyWindow) {
            isOutsideTouchable = true
            isFocusable = builder.isFocusable
            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            elevation = builder.elevation
        }
    }

    // 4. Инициализировать пользовательский макет
    private fun initializeBalloonLayout() {
        if (hasCustomLayout()) {
            initializeCustomLayout() // Пользовательский макет
        }
    }

    // 5. Инициализировать содержимое Balloon. Здесь определяется высота
    private fun initializeBalloonContent() {
        val paddingSize = builder.arrowSize - offset()
        val elevation = builder.elevation.toInt()
        balloonContent.setPadding(0, 0, 0, paddingSize.coerceAtLeast(elevation))
    }

    /** Проверьте, имеет ли [Balloon.Builder] пользовательский макет [Balloon.Builder.layoutRes] или [Balloon.Builder.layout] */
    private fun hasCustomLayout(): Boolean {
        return builder.layoutRes != null || builder.layout != null
    }

    /** Инициализирует содержимое Boolean, используя пользовательский макет. */
    private fun initializeCustomLayout() {
        val layout = builder.layoutRes?.let {
            LayoutInflater.from(context).inflate(it, balloonCard, false)

        } ?: builder.layout ?: throw IllegalArgumentException("Пользовательский макет имеет значение null.")
        val parentView = layout.parent as? ViewGroup
        parentView?.removeView(layout)
        balloonCard.removeAllViews()
        balloonCard.addView(layout)
        traverseAndMeasureTextWidth(balloonCard)
    }

    /**
     * Обойти иерархию представлений [ViewGroup] и измерить каждое [TextView] для измерения
     * конкретной высоты [TextView] и расчета правильного размера высоты выноски.
     *
     * @param parent родительское представление для обхода и измерения.
     */
    private fun traverseAndMeasureTextWidth(parent: ViewGroup) {
        parent.forEach { child ->
            if (child is TextView) {
                child.maxWidth = builder.width
            }
            else if (child is ViewGroup) {
                traverseAndMeasureTextWidth(child)
            }
        }
    }

    // Инициализировать стрелку.
    private fun initializeArrow(anchor: View) {
        with(balloonArrow) {
            // Размер стрелки
            layoutParams = FrameLayout.LayoutParams(builder.arrowSize, builder.arrowSize)
            builder.arrowDrawable?.let { setImageDrawable(it) }

            // Цвет
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(builder.backgroundColor))

            balloonCard.post {
                with(balloonArrow) {
                    x = balloonWrapper.width / 2 - builder.arrowSize / 2f
                    y = balloonCard.y + balloonCard.height - offset()
                }
                visible(builder.isVisibleArrow)
            }
        }
    }

    private fun offset(): Int {
        return (builder.arrowSize * 0.09).toInt()
    }

    fun dismiss(){
        if (bodyWindow.isShowing) {
            bodyWindow.dismiss()
        }
    }

    /** Закрыть всплывающее меню с задержкой в миллисекунды */
    fun dismissWithDelay(delay: Long): Boolean =
        handler.postDelayed(autoDismissRunnable, delay)

    //************* show ************

    fun showBalloon(anchor: View, xOff: Int = 0, yOff: Int = 0) {
        show(BalloonPlacement(anchor = anchor, xOff = xOff, yOff = yOff))
    }

    private fun show(placement: BalloonPlacement) {
        val mainAnchor = placement.anchor

//        Log.d("myLogs", "show, placement: $placement")

        if (canShowBalloonWindow(mainAnchor)) {
            mainAnchor.post {
                canShowBalloonWindow(mainAnchor).takeIf { it } ?: return@post

                main.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                bodyWindow.width = getMeasuredWidth()
                bodyWindow.height = getMeasuredHeight()

                // Инициализировать стрелку
                initializeArrow(mainAnchor)
                // Инициализировать содержимое Balloon
                initializeBalloonContent()

                // Рассчитать смещение
                val (xOff, yOff) = calculateOffset(placement)
                bodyWindow.showAsDropDown(mainAnchor, xOff, yOff)

            }
        }
    }

    // Рассчитать смещение
    private fun calculateOffset(placement: BalloonPlacement): Pair<Int, Int> {
        val anchor = placement.anchor
        val wA = anchor.measuredWidth
        val hA = anchor.measuredHeight
        val wA2 = wA / 2
        val wB = getMeasuredWidth()
        val hB = getMeasuredHeight()
        val wB2 = wB / 2
        val xOff = placement.xOff
        val yOff = placement.yOff

        return (wA2 - wB2 + xOff) to -(hB + hA) + yOff
    }

    // Может показать Window Balloon
    private fun canShowBalloonWindow(anchor: View): Boolean {
        // Если Balloon уже виден, не стоит пытаться показать его снова.
        if (isShowing) return false

        // Если воздушный шар уже уничтожен в зависимости от жизненного цикла,
        // Мы не должны разрешать показ popupWindow, это связано с методом `relay()`. (#46)
        if (destroyed) return false

        // Если Activity завершается, мы не можем прикрепить popupWindow к окну Activity. (#92)
        if ((context as? Activity)?.isFinishing == true) return false

        // Мы должны проверить, что contentView уже прикреплен к decorView или backgroundView в popupWindow.
        // Иногда возникает проблема параллелизма между показом и закрытием popupWindow. (#149)
        if (bodyWindow.contentView.parent != null) return false

        // Нам следует проверить, является ли токен окна якорного представления действительным.
        if (!anchor.isAttachedToWindow) return false

        // Нам следует проверить, прикреплен ли вид привязки к родительскому окну. (#730)
        return anchor.windowToken.isBinderAlive
    }

    /** Получить виды содержимого всплывающего окна  */
    fun getContentView(): ViewGroup {
        return balloonCard
    }

    /** Получает измеренную ширину всплывающего окна. */
    fun getMeasuredWidth(): Int {
        if (builder.width != WRAP) {
            return builder.width
        }
        return main.measuredWidth
    }

    /** Получает измеренную высоту всплывающего окна. */
    fun getMeasuredHeight(): Int {
        if (builder.height != WRAP) {
            return builder.height
        }
        return main.measuredHeight

    }

    //********** Builder **************

    /** Класс Builder для создания Balloon */
    class  Builder(private val context: Context) {

        var width: Int = WRAP
        var isFocusable: Boolean = true
        var elevation: Float = 2f.dp
        var layout: View? = null

        var layoutRes: Int? = null
        var height: Int = WRAP

        var backgroundColor: Int = Color.BLACK
        var cornerRadius: Float = 5f.dp
        var backgroundDrawable: Drawable? = null

        var paddingLeft: Int = 0
        var paddingTop: Int = 0
        var paddingRight: Int = 0
        var paddingBottom: Int = 0

        var arrowSize: Int = 12.dp
        var arrowDrawable: Drawable? = null
        var isVisibleArrow: Boolean = true

        //************************

        /** задает размер стрелки. */
        fun setArrowSize(value: Int): Builder = apply {
            this.arrowSize =
                if (value == WRAP) {
                    WRAP
                } else {
                    value.dp
                }
        }

        /** задает размер ширины. */
        fun setWidth(value: Int): Builder = apply {
            require(
                value > 0 || value == WRAP,
            ) { "Ширина Balloon должна быть больше нуля." }
            this.width = value.dp
        }

        /** задает размер высоты */
        fun setHeight(value: Int): Builder = apply {
            require(
                value > 0 || value == WRAP,
            ) { "Высота Balloon должна быть больше нуля." }
            this.height = value.dp
        }

        /** Задает цвет фона стрелки и всплывающего окна. */
        fun setBackgroundColor(value: Int): Builder =
            apply { this.backgroundColor = value }

        /** Задает радиус углов всплывающего окна. */
        fun setCornerRadius(value: Float): Builder = apply {
            this.cornerRadius = value.dp
        }

        /** Задает видимость стрелки. */
        fun setIsVisibleArrow(value: Boolean):Builder =
            apply { this.isVisibleArrow = value }


        /** Задает пользовательский ресурс макета для Balloon */
        fun setLayout(layoutRes: Int): Builder =
            apply { this.layoutRes = layoutRes }

        /**
         * Создать новый экземпляр [Balloon], включающий настроенные атрибуты.
         *
         * @return Новый созданный экземпляр [Balloon].
         */
        fun build(): Balloon = Balloon(
            context = context,
            builder = this@Builder,
        )
    }

}