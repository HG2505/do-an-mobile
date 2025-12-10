package com.example.giuaki

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories_screen)
        initViews()
    }
    private fun initViews() {
        findViewById<ImageButton>(R.id.btnCategoryBack).setOnClickListener { finish() }
        val rvCategoryList = findViewById<RecyclerView>(R.id.rvCategoryList)
        val categories = listOf(
            Category(1, "CPU - Bộ vi xử lý", R.drawable.cpu, "#E3F2FD", "#2962FF"),
            Category(2, "VGA - Card màn hình", R.drawable.vga, "#FFF3E0", "#FF6D00"),
            Category(3, "RAM - Bộ nhớ trong", R.drawable.ram, "#E8F5E9", "#2E7D32"),
            Category(4, "Mainboard - Bo mạch", R.drawable.main, "#F3E5F5", "#6A1B9A"),
            Category(5, "SSD - Ổ cứng", R.drawable.ssd, "#FFEBEE", "#C62828"),
            Category(6, "Nguồn - PSU", R.drawable.psu, "#FFF8E1", "#FFC107"),
        )
        val adapter = CategoryAdapter(categories) { category ->
            val intent = Intent(this, ProductListActivity::class.java)
            val key = when (category.id) {
                1 -> "cpu"
                2 -> "vga"
                3 -> "ram"
                4 -> "mainboard"
                5 -> "ssd"
                6 -> "psu"
                else -> ""
            }
            if (key.isNotEmpty()) {
                intent.putExtra("CATEGORY_NAME", key)
                startActivity(intent)
            }
        }
        rvCategoryList.adapter = adapter
        rvCategoryList.layoutManager = GridLayoutManager(this, 2)
    }
}