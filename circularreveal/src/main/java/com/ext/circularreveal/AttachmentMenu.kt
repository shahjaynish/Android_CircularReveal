package com.ext.circularreveal

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.ext.circularreveal.core.AttachmentItem

class AttachmentMenu private constructor() {

    private val items = mutableListOf<AttachmentItem>()

    fun addItem(
        id: String,
        title: String,
        iconRes: Int,
        onClick: () -> Unit
    ) {
        items.add(
            AttachmentItem(id, title, iconRes, onClick)
        )
    }

    fun removeItem(id: String) {
        items.removeAll { it.id == id }
    }

    fun show(activity: FragmentActivity) {
        AttachmentMenuDialog(items).show(
            activity.supportFragmentManager,
            "AttachmentMenu"
        )
    }

    companion object {
        fun create(block: AttachmentMenu.() -> Unit): AttachmentMenu {
            return AttachmentMenu().apply(block)
        }
    }
}
