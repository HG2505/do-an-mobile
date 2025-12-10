package com.example.giuaki

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val categoryList: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCategory: ImageView = itemView.findViewById(R.id.imgCategory)
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)

        // Để nullable (?) để an toàn, nếu file xml quên đặt ID thì app không bị văng (Crash)
        val cardView: CardView? = itemView.findViewById(R.id.cvCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        // Đảm bảo tên file layout đúng là 'layout_item'
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]

        holder.tvCategoryName.text = category.name

        // --- QUAN TRỌNG: Dùng setImageResource để load ảnh từ res/drawable ---
        holder.imgCategory.setImageResource(category.icon)
        // ---------------------------------------------------------------------

        // Xử lý đổi màu nền (Dùng ?. để nếu cardView null thì bỏ qua, không lỗi)
        try {
            holder.cardView?.setCardBackgroundColor(Color.parseColor(category.bgColor))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Bắt sự kiện click
        holder.itemView.setOnClickListener {
            onItemClick(category)
        }
    }

    override fun getItemCount(): Int = categoryList.size
}