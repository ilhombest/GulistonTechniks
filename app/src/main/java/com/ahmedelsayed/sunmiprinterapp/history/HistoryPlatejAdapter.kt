package com.ahmedelsayed.sunmiprinterapp.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmedelsayed.sunmiprinterapp.R


class HistoryPlatejAdapter(private val mList: List<HistoryViewModel>) : RecyclerView.Adapter<HistoryPlatejAdapter.ViewHolder>() {

    var onItemClick : ((HistoryViewModel)->Unit)? = null
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        holder.tvSumma.text = "Сумма платежа: " +itemsViewModel.summa +" сомони"
        // sets the text to the textview from our itemHolder class
        holder.tvDate.text = "Дата : "+itemsViewModel.date
        holder.who.text = "Принял : "+itemsViewModel.prinyal
        holder.tvComment.text = itemsViewModel.comment
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(itemsViewModel)
        }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvSumma: TextView = itemView.findViewById(R.id.tvHistorySumma)
        val tvDate: TextView = itemView.findViewById(R.id.tvHistoryData)
        val who : TextView = itemView.findViewById(R.id.tvHistoryPrinyal)
        val tvComment : TextView = itemView.findViewById(R.id.tvComment)
    }
}

