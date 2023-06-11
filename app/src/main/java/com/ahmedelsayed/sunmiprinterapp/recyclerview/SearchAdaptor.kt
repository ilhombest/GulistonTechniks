import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmedelsayed.sunmiprinterapp.R
import com.ahmedelsayed.sunmiprinterapp.recyclerview.SearchViewModel

class SearchAdaptor(private val mList: List<SearchViewModel>) : RecyclerView.Adapter<SearchAdaptor.ViewHolder>() {

    var onItemClick : ((SearchViewModel)->Unit)? = null
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        holder.tvName.text = itemsViewModel.name
        // sets the text to the textview from our itemHolder class
        holder.address.text = itemsViewModel.address
        holder.balance.text = itemsViewModel.balance
        holder.tvId.text = itemsViewModel.tvId
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
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val address: TextView = itemView.findViewById(R.id.tvAddress)
        val balance : TextView = itemView.findViewById(R.id.tvBalance)
        val tvId : TextView = itemView.findViewById(R.id.tvId)
        }
    }

