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
    private val onCartChanged: () -> Unit // Callback để báo cho Activity biết cần tính lại tổng tiền
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Ánh xạ các view từ layout item_cart.xml
        val imgThumb: ImageView = view.findViewById(R.id.imgCartThumb)
        val tvName: TextView = view.findViewById(R.id.tvCartItemName)
        val tvPrice: TextView = view.findViewById(R.id.tvCartItemPrice)
        val tvQty: TextView = view.findViewById(R.id.tvCartItemQty)

        // Các nút bấm điều chỉnh số lượng
        val btnMinus: ImageButton = view.findViewById(R.id.btnMinus)
        val btnPlus: ImageButton = view.findViewById(R.id.btnPlus)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)

        fun bind(item: CartItem) {
            // Định dạng giá tiền
            val formatter = DecimalFormat("#,###")
            tvName.text = item.product.name
            tvPrice.text = "${formatter.format(item.product.price)} VND"
            tvQty.text = item.quantity.toString()

            // Sử dụng Glide để load ảnh
            // Ưu tiên URL từ Firebase, nếu không có thì dùng ảnh mặc định
            if (item.product.imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(item.product.imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery) // Ảnh chờ
                    .error(android.R.drawable.ic_menu_report_image) // Ảnh lỗi
                    .into(imgThumb)
            } else {
                // Trường hợp sản phẩm không có URL ảnh (ví dụ dữ liệu cũ hoặc lỗi)
                imgThumb.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            // --- Xử lý sự kiện nút Tăng (+) ---
            btnPlus.setOnClickListener {
                CartRepository.updateQuantity(item, item.quantity + 1)
                notifyItemChanged(adapterPosition) // Cập nhật lại dòng này để hiển thị số mới
                onCartChanged() // Gọi về Activity để tính lại tổng tiền
            }

            // --- Xử lý sự kiện nút Giảm (-) ---
            btnMinus.setOnClickListener {
                if (item.quantity > 1) {
                    CartRepository.updateQuantity(item, item.quantity - 1)
                    notifyItemChanged(adapterPosition)
                } else {
                    // Nếu số lượng là 1 mà bấm giảm -> Xóa luôn
                    CartRepository.removeFromCart(item)
                    notifyDataSetChanged() // Reload toàn bộ list vì số lượng dòng thay đổi
                }
                onCartChanged()
            }

            // --- Xử lý sự kiện nút Xóa (Thùng rác) ---
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