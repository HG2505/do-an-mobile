package com.example.giuaki

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val btnAddCart: ImageButton = itemView.findViewById(R.id.btnAddCart)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.tvProductName.text = product.name
        val formatter = DecimalFormat("#,###")
        val formattedPrice = formatter.format(product.price)
        holder.tvProductPrice.text = "$formattedPrice đ"
        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(android.R.drawable.ic_menu_report_image)
            .into(holder.imgProduct)
        // ---------------------------------------------------------

        holder.btnAddCart.setOnClickListener {
            CartRepository.addToCart(product)
            Toast.makeText(
                holder.itemView.context,
                "Đã thêm ${product.name} vào giỏ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    override fun getItemCount(): Int = productList.size
}