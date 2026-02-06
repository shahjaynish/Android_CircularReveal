package com.ext.circularreveal.core

data class AttachmentItem(
    val id: String,
    val title: String,
    val iconRes: Int,
    val onClick: (() -> Unit)? = null
)