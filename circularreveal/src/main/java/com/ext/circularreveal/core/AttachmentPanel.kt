package com.ext.circularreveal.core

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var isKeyboardVisible = false

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

        // ✅ Monitor keyboard state
        ViewCompat.setOnApplyWindowInsetsListener(container.rootView) { view, insets ->

            val ime = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val nav = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom

            val realKeyboardHeight = ime - nav

            // Save keyboard height when detected
            if (realKeyboardHeight > 200) {
                keyboardHeight = realKeyboardHeight
                isKeyboardVisible = true
            } else {
                isKeyboardVisible = false
            }

            // If keyboard opens while panel is visible, hide panel
            if (isKeyboardVisible && container.visibility == View.VISIBLE) {
                hide()
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

        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        // If panel is already visible → hide it and show keyboard
        if (container.visibility == View.VISIBLE) {
            hide()
            input.requestFocus()
            input.postDelayed({
                imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
            }, 100)
            return
        }

        // Clear focus from input to prevent keyboard from popping up
        input.clearFocus()

        // If keyboard is visible → hide it and show panel
        if (isKeyboardVisible) {
            imm.hideSoftInputFromWindow(input.windowToken, 0)

            // Show panel immediately with keyboard height
            input.postDelayed({
                showPanel()
            }, 50) // Small delay to sync with keyboard closing
        } else {
            // Keyboard not visible → show panel directly
            showPanel()
        }
    }

    private fun showPanel() {

        // Use saved keyboard height or default
        if (keyboardHeight == 0) {
            keyboardHeight = (300 * activity.resources.displayMetrics.density).toInt()
        }

        // Set height and show
        val params = container.layoutParams
        params.height = keyboardHeight
        container.layoutParams = params

        container.visibility = View.VISIBLE

        // Smooth slide-up animation
        container.translationY = keyboardHeight.toFloat()
        container.alpha = 1f

        container.animate()
            .translationY(0f)
            .setDuration(200)
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .start()
    }

    private fun animateHide(onEnd: (() -> Unit)? = null) {
        container.animate()
            .translationY(keyboardHeight.toFloat())
            .setDuration(200)
            .setInterpolator(android.view.animation.AccelerateInterpolator())
            .withEndAction {
                container.visibility = View.GONE
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
        container.translationY = 0f
    }

    fun isVisible(): Boolean {
        return container.visibility == View.VISIBLE
    }
}