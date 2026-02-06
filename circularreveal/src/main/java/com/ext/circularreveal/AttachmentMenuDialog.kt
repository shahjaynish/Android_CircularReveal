package com.ext.circularreveal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ext.circularreveal.core.AttachmentItem
import com.ext.circularreveal.ui.AttachmentAdapter

class AttachmentMenuDialog(
    private val items: List<AttachmentItem>
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.dialog_attachment_menu,
            container,
            false
        )

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerMenu)

        recycler.layoutManager = GridLayoutManager(requireContext(), 3)
        recycler.adapter = AttachmentAdapter(items) {
            dismiss()
        }

        return view
    }
}
