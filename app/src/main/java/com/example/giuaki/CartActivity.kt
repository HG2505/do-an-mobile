package com.example.giuaki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class CartActivity : AppCompatActivity() {

    private lateinit var tvTotalPrice: TextView
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val btnBack = findViewById<ImageButton>(R.id.btnCartBack)
        val rvCartList = findViewById<RecyclerView>(R.id.rvCartList)
        tvTotalPrice = findViewById<TextView>(R.id.tvCartTotalPrice)
        val btnCheckout = findViewById<Button>(R.id.btnCartCheckout)

        btnBack.setOnClickListener {
            finish()
        }

        // Khởi tạo Adapter và truyền vào một hàm (callback)
        // Hàm này sẽ được Adapter gọi mỗi khi số lượng sản phẩm thay đổi
        adapter = CartAdapter(CartRepository.getItems()) {
            updateTotal() // Tính lại tiền ngay lập tức
        }

        rvCartList.layoutManager = LinearLayoutManager(this)
        rvCartList.adapter = adapter

        // Tính tiền lần đầu khi vừa vào màn hình
        updateTotal()

        btnCheckout.setOnClickListener {
            if (CartRepository.getItems().isNotEmpty()) {
                // Chuyển sang màn hình Thanh toán (CheckoutActivity)
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Hàm tính tổng tiền và hiển thị lên TextView
    private fun updateTotal() {
        val formatter = DecimalFormat("#,###")
        val total = CartRepository.getTotalPrice()
        tvTotalPrice.text = "${formatter.format(total)} VND"
    }

    // Khi người dùng quay lại màn hình này (ví dụ từ trang thanh toán về), cập nhật lại dữ liệu
    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
            updateTotal()
        }
    }
}