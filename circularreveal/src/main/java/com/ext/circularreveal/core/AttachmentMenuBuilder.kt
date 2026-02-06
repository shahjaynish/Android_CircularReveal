package com.ext.circularreveal.core

class AttachmentMenuBuilder {

    internal val items = mutableListOf<AttachmentItem>()

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
}