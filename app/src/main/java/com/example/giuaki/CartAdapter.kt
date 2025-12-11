package com.example.giuaki

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onCartChanged: () -> Unit) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgThumb: ImageView = view.findViewById(R.id.imgCartThumb)
        val tvName: TextView = view.findViewById(R.id.tvCartItemName)
        val tvPrice: TextView = view.findViewById(R.id.tvCartItemPrice)
        val tvQty: TextView = view.findViewById(R.id.tvCartItemQty)
        val btnMinus: ImageButton = view.findViewById(R.id.btnMinus)
        val btnPlus: ImageButton = view.findViewById(R.id.btnPlus)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        fun bind(item: CartItem) {
            val formatter = DecimalFormat("#,###")
            tvName.text = item.product.name
            tvPrice.text = "${formatter.format(item.product.price)} VND"
            tvQty.text = item.quantity.toString()
            if (item.product.imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(item.product.imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(imgThumb)
            } else {
                imgThumb.setImageResource(android.R.drawable.ic_menu_gallery)
            }
            btnPlus.setOnClickListener {
                CartRepository.updateQuantity(item, item.quantity + 1)
                notifyItemChanged(adapterPosition)
                onCartChanged()
            }

            btnMinus.setOnClickListener {
                if (item.quantity > 1) {
                    CartRepository.updateQuantity(item, item.quantity - 1)
                    notifyItemChanged(adapterPosition)
                } else {
                    CartRepository.removeFromCart(item)
                    notifyDataSetChanged()
                }
                onCartChanged()
            }
            btnDelete.setOnClickListener {
                CartRepository.removeFromCart(item)
                notifyDataSetChanged()
                onCartChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size
}