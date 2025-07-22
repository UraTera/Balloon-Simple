package com.tera.balloon_simple

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tera.balloon_simple.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

        initButton()

    }

    private fun initButton() = with(binding) {
        // Top image
        button1.setOnClickListener {
            createBalloonTop()
        }
        // Center
        button2.setOnClickListener {
            createBalloonCenter()
        }
    }

    private fun createBalloonTop() {
        val balloon = Balloon.Builder(this)
            .setWidth(170)
            .setCornerRadius(10f)
            .setLayout(R.layout.custom_layout)
            .build()

        val tvTitle: TextView = balloon.content().findViewById(R.id.tvTitle)
        val tvMessage: TextView = balloon.content().findViewById(R.id.tvMessage)
        val bnOk: Button = balloon.content().findViewById(R.id.bnOk)

        val title = "Weather"
        tvTitle.text = title
        val message = getString(R.string.message)
        tvMessage.text = message

        val anchor = binding.imMark
        balloon.showTop(anchor)
//        balloon.showTop(anchor, 0, -15)
        //balloon.dismissWithDelay(2000)

        bnOk.setOnClickListener {
            balloon.dismiss()
        }
    }

    private fun createBalloonCenter() {
        val balloon = Balloon.Builder(this)
            .setArrowSize(0)
            .setIsVisibleArrow(false)
            .setWidth(170)
            .setCornerRadius(10f)
            .setLayout(R.layout.custom_layout)
            .setAnimation(true)
            .build()

        val tvTitle: TextView = balloon.content().findViewById(R.id.tvTitle)
        val tvMessage: TextView = balloon.content().findViewById(R.id.tvMessage)
        val bnOk: Button = balloon.content().findViewById(R.id.bnOk)

        val title = "Weather"
        tvTitle.text = title
        val message = getString(R.string.message2)
        tvMessage.text = message

        balloon.showCenter()
//        balloon.showCenter(0, -32)
//        balloon.dismissWithDelay(2000)

        bnOk.setOnClickListener {
            balloon.dismiss()
        }
    }

}