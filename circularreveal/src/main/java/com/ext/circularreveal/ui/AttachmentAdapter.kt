package com.ext.circularreveal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext.circularreveal.R
import com.ext.circularreveal.core.AttachmentItem


class AttachmentAdapter(
    private val items: List<AttachmentItem>,
    private val onItemClick: (() -> Unit)? = null
) : RecyclerView.Adapter<AttachmentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.itemIcon)
        val title: TextView = view.findViewById(R.id.itemTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attachment, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.icon.setImageResource(item.iconRes)
        holder.title.text = item.title

        holder.itemView.setOnClickListener {

            // Run item click
            item.onClick?.invoke()

            // Close panel/dialog
            onItemClick?.invoke()
        }
    }

    override fun getItemCount() = items.size
}
