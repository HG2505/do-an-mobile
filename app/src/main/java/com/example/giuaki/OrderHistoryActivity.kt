package com.example.giuaki

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var rvOrderHistory: RecyclerView
    private lateinit var adapter: OrderAdapter
    private val orderList = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        val btnBack = findViewById<ImageButton>(R.id.btnBackHistory)
        rvOrderHistory = findViewById(R.id.rvOrderHistory)
        rvOrderHistory.layoutManager = LinearLayoutManager(this)
        adapter = OrderAdapter(orderList)
        rvOrderHistory.adapter = adapter

        btnBack.setOnClickListener {
            finish()
        }
        loadOrderHistory()
    }

    private fun loadOrderHistory() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
            return
        }
        val db = FirebaseFirestore.getInstance()
        db.collection("Orders")
            .whereEqualTo("userId", user.uid)
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                orderList.clear()
                for (document in documents) {
                    try {
                        val order = document.toObject(Order::class.java)
                        order.id = document.id
                        orderList.add(order)
                    } catch (e: Exception) {
                        Log.e("OrderHistory", "Lỗi convert data: ${e.message}")
                    }
                }

                adapter.notifyDataSetChanged()
                if (orderList.isEmpty()) {
                    Toast.makeText(this, "Bạn chưa có đơn hàng nào", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("OrderHistory", "Lỗi lấy dữ liệu: ", exception)
                Toast.makeText(this, "Lỗi tải lịch sử: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}