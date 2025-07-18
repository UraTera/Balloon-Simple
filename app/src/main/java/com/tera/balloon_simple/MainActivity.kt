package com.tera.balloon_simple

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tera.balloon_simple.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var balloon: Balloon

    private lateinit var imMark1: ImageView
    private lateinit var imMark2: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imMark1 = findViewById(R.id.imMark1)
        imMark2 = findViewById(R.id.imMark2)

        initCustomBallon()
        initButton()
    }

    private fun initCustomBallon() {
        val groundColor = ContextCompat.getColor(this, R.color.white)

        balloon = Balloon.Builder(this)
            .setArrowSize(20)
            .setIsVisibleArrow(true)
            .setWidth(170)
            .setBackgroundColor(groundColor)
            .setCornerRadius(10f)
            .setLayout(R.layout.custom_layout)
            .build()
    }

    private fun initButton() = with(binding) {
        // Верхняя картинка
        button1.setOnClickListener {
            val message = getString(R.string.message)
            createBalloon(imMark1, message)
        }

        button2.setOnClickListener {
            val message = getString(R.string.message2)
            createBalloon(imMark2, message)
        }
    }

    private fun createBalloon(view: View, message: String) {
        val title = "Weather"
        val tvTitle: TextView = balloon.getContentView().findViewById(R.id.tvTitle)
        val tvMessage: TextView = balloon.getContentView().findViewById(R.id.tvMessage)
        val bnOk: Button = balloon.getContentView().findViewById(R.id.bnOk)
        tvTitle.text = title
        tvMessage.text = message
        balloon.showBalloon(view)
        //balloon.showBalloon(view, 0, 150)
        //balloon.dismissWithDelay(2000)

        bnOk.setOnClickListener {
            balloon.dismiss()
        }
    }
}