package com.example.giuaki

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail)

        // 1. Nhận dữ liệu Product từ Intent
        val product = intent.getSerializableExtra("PRODUCT_DATA") as? Product

        // Kiểm tra nếu không có dữ liệu thì thoát
        if (product == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 2. Ánh xạ View
        val imgDetail: ImageView = findViewById(R.id.imgDetailProduct)
        val tvName: TextView = findViewById(R.id.tvDetailName)
        val tvPrice: TextView = findViewById(R.id.tvDetailPrice)
        val tvDesc: TextView = findViewById(R.id.tvDetailDescription)
        val btnBack: ImageButton = findViewById(R.id.btnDetailBack)
        val btnAddCart: Button = findViewById(R.id.btnDetailAddToCart)

        // 3. Hiển thị dữ liệu lên màn hình
        tvName.text = product.name
        tvDesc.text = product.description

        val formatter = DecimalFormat("#,###")
        tvPrice.text = "${formatter.format(product.price)} đ"

        Glide.with(this)
            .load(product.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(imgDetail)

        // 4. Xử lý sự kiện nút Back
        btnBack.setOnClickListener {
            finish()
        }

        // 5. Xử lý nút Thêm vào giỏ
        btnAddCart.setOnClickListener {
            CartRepository.addToCart(product)
            Toast.makeText(this, "Đã thêm ${product.name} vào giỏ", Toast.LENGTH_SHORT).show()
        }
    }
}