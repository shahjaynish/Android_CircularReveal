package com.ext.circularreveal.core

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ext.circularreveal.R
import com.ext.circularreveal.ui.AttachmentAdapter

class AttachmentPanel private constructor(
    private val activity: Activity,
    private val container: FrameLayout
) {

    private var keyboardHeight = 0

    companion object {
        fun attachTo(
            activity: Activity,
            container: FrameLayout,
            block: AttachmentMenuBuilder.() -> Unit
        ): AttachmentPanel {

            val panel = AttachmentPanel(activity, container)
            panel.init(block)
            return panel
        }
    }

    private fun init(block: AttachmentMenuBuilder.() -> Unit) {

        // ✅ Capture keyboard height while keyboard is open
        ViewCompat.setOnApplyWindowInsetsListener(container.rootView) { _, insets ->

            val ime = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val nav = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom

            val realKeyboardHeight = ime - nav

            if (realKeyboardHeight > 200) {
                keyboardHeight = realKeyboardHeight
            }

            // ✅ If keyboard opens, hide panel automatically
            if (ime > 200 && container.visibility == View.VISIBLE) {
                animateHide()
            }

            insets
        }


        val builder = AttachmentMenuBuilder().apply(block)

        val view = LayoutInflater.from(activity)
            .inflate(R.layout.attachment_panel, container, false)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerMenu)
        recycler.layoutManager = GridLayoutManager(activity, 3)
        recycler.adapter = AttachmentAdapter(builder.items)

        container.removeAllViews()
        container.addView(view)
    }

    fun toggle(input: EditText) {

        val imm =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE)
                    as InputMethodManager

        // ✅ If panel already visible → hide with animation, then show keyboard
        if (container.visibility == View.VISIBLE) {

            animateHide {
                input.requestFocus()
                input.post {
                    imm.showSoftInput(input, 0)
                }
            }
            return
        }

        // ✅ Hide keyboard first
        imm.hideSoftInputFromWindow(input.windowToken, 0)

        // ✅ Wait a bit, then animate panel up
        container.postDelayed({

            container.layoutParams.height = keyboardHeight
            container.requestLayout()

            animateShow()

        }, 180)
    }




    private fun waitForKeyboardToClose(onClosed: () -> Unit) {

        container.post(object : Runnable {
            override fun run() {

                val rootInsets =
                    ViewCompat.getRootWindowInsets(activity.window.decorView)

                val imeVisible =
                    rootInsets?.isVisible(WindowInsetsCompat.Type.ime()) == true

                if (!imeVisible) {
                    onClosed()
                } else {
                    container.postDelayed(this, 50)
                }
            }
        })
    }
    private fun showPanel() {

        if (keyboardHeight == 0) keyboardHeight = 700

        // ✅ Prevent panel becoming too large
        val maxHeight = activity.window.decorView.height / 2
        if (keyboardHeight > maxHeight) {
            keyboardHeight = maxHeight
        }

        container.layoutParams.height = keyboardHeight
        container.requestLayout()
        container.visibility = View.VISIBLE
    }

    private fun animateShow() {
        container.alpha = 0f
        container.translationY = keyboardHeight.toFloat()

        container.visibility = View.VISIBLE

        container.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(250)
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .start()
    }

    private fun animateHide(onEnd: (() -> Unit)? = null) {
        container.animate()
            .alpha(0f)
            .translationY(keyboardHeight.toFloat())
            .setDuration(250)
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .withEndAction {
                container.visibility = View.GONE
                container.alpha = 1f
                container.translationY = 0f
                onEnd?.invoke()
            }
            .start()
    }
    fun hideSmooth() {
        if (container.visibility == View.VISIBLE) {
            animateHide()
        }
    }

    fun hide() {
        container.visibility = View.GONE
    }

    fun isVisible(): Boolean {
        return container.visibility == View.VISIBLE
    }
}
