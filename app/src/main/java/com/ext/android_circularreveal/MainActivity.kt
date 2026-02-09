package com.ext.android_circularreveal

import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.ext.android_circularreveal.databinding.ActivityMainBinding
import com.ext.circularreveal.core.AttachmentPanel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var panel: AttachmentPanel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val container = findViewById<FrameLayout>(R.id.attachmentContainer)

        val btnAttach = findViewById<ImageView>(R.id.btnAttach)
        val input = findViewById<EditText>(R.id.messageInput)

        panel = AttachmentPanel.attachTo(
            activity = this,
            container = binding.attachmentContainer
        ) {
            addItem("gallery", "Gallery", R.drawable.ic_gallery) {}
            addItem("camera", "Camera", R.drawable.ic_camera) {}
            addItem("document", "Document", R.drawable.ic_document) {}
        }
        binding.btnAttach.setOnClickListener {
            panel.toggle(binding.messageInput)
        }
        binding.messageInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && panel.isVisible()) {
                panel.hide()
            }
        }
    }

    override fun onBackPressed() {
        if (panel.isVisible()) {
            panel.hide()
        } else {
            super.onBackPressed()
        }
    }
}
