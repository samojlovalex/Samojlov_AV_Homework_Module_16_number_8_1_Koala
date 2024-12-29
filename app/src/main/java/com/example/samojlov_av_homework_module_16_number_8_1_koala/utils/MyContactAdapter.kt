package com.example.samojlov_av_homework_module_16_number_8_1_koala.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_16_number_8_1_koala.R
import com.example.samojlov_av_homework_module_16_number_8_1_koala.models.Contact

class MyContactAdapter(private val list: List<Contact>) :
    RecyclerView.Adapter<MyContactAdapter.ContactViewHolder>() {

    private var onContactClickListener: OnContactClickListener? = null

    interface OnContactClickListener {
        fun onContactClickListener(
            contact: Contact,
            position: Int,
        )
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameContactTV)
        val phone: TextView = itemView.findViewById(R.id.phoneContactTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val name = list[position].name
        val phone = list[position].phone

        holder.name.text = name
        holder.phone.text = phone

        holder.itemView.setOnClickListener {
            if (onContactClickListener != null) {
                onContactClickListener!!.onContactClickListener(
                    list[position],
                    position
                )
            }
        }
    }

    fun setContactClickListener(onContactClickListener: OnContactClickListener) {
        this.onContactClickListener = onContactClickListener
    }
}