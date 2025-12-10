package com.example.giuaki

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val btnBack = findViewById<ImageButton>(R.id.btnCartBack)
        val rvCartList = findViewById<RecyclerView>(R.id.rvCartList)
        val tvTotalPrice = findViewById<TextView>(R.id.tvCartTotalPrice)
        val btnCheckout = findViewById<Button>(R.id.btnCartCheckout)

        btnBack.setOnClickListener {
            finish()
        }

        val adapter = CartAdapter(CartRepository.getItems())
        rvCartList.layoutManager = LinearLayoutManager(this)
        rvCartList.adapter = adapter

        val formatter = DecimalFormat("#,###")
        val total = CartRepository.getTotalPrice()
        tvTotalPrice.text = "${formatter.format(total)} VND"

        btnCheckout.setOnClickListener {
            if (CartRepository.getItems().isNotEmpty()) {
                CartRepository.clearCart()
                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}