package com.example.giuaki

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class CartAdapter(
    private val cartItems: List<CartItem>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgThumb: ImageView = view.findViewById(R.id.imgCartThumb)
        val tvName: TextView = view.findViewById(R.id.tvCartItemName)
        val tvPrice: TextView = view.findViewById(R.id.tvCartItemPrice)
        val tvQty: TextView = view.findViewById(R.id.tvCartItemQty)

        fun bind(item: CartItem) {
            val formatter = DecimalFormat("#,###")
            tvName.text = item.product.name
            tvPrice.text = "${formatter.format(item.product.price)} VND"
            tvQty.text = "x${item.quantity}"
            imgThumb.setImageResource(android.R.drawable.ic_menu_gallery)
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