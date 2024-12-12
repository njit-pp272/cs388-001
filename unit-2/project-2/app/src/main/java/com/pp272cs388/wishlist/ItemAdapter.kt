package com.pp272cs388.wishlist

import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil.isValidUrl
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val itemList: MutableList<Item>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.tvItemName)
        val itemDescription: TextView = itemView.findViewById(R.id.tvItemDescription)
        val itemPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemName.text = currentItem.name
//        holder.itemDescription.text = currentItem.description

        if (isValidUrl(currentItem.description)) {
            val htmlDescription = Html.fromHtml("<a href='${currentItem.description}'>${currentItem.description}</a>", Html.FROM_HTML_MODE_LEGACY)
            holder.itemDescription.text = htmlDescription
            holder.itemDescription.movementMethod = LinkMovementMethod.getInstance()
        } else {
            holder.itemDescription.text = currentItem.description
        }

        holder.itemPrice.text = "$${currentItem.price}"


        // Handle delete button click
        holder.itemView.findViewById<Button>(R.id.btnDeleteItem).setOnClickListener {
            val itemName = currentItem.name
            itemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemList.size) // Update positions of remaining items
            Toast.makeText(
                holder.itemView.context,
                "$itemName deleted from wishlist",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItem(item: Item) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }

    private fun isValidUrl(url: String): Boolean {
        return url.matches(Regex("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"))
    }
}
