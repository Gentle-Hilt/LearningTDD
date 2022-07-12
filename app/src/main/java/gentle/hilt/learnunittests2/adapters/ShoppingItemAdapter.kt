package gentle.hilt.learnunittests2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import gentle.hilt.learnunittests2.databinding.ItemShoppingBinding
import gentle.hilt.learnunittests2.db.ShoppingItem
import javax.inject.Inject

class ShoppingItemAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemShoppingBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var shoppingItems: List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ItemShoppingBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]

        holder.binding.apply {
            holder.itemView.apply {
                glide.load(shoppingItem.imageUrl).into(ivShoppingImage)

                tvName.text = shoppingItem.name
                val amountText = "${shoppingItem.amount}x"
                tvShoppingItemAmount.text = amountText
                val priceText = "${shoppingItem.price} $ each"
                tvShoppingItemPrice.text = priceText
            }
        }
    }

}