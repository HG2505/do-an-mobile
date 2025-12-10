package com.example.giuaki

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.jvm.java

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var rvOrderHistory: RecyclerView
    private lateinit var adapter: OrderAdapter
    // Danh sách lưu trữ các đơn hàng tải về từ Firestore
    private val orderList = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history) // Đảm bảo bạn đã có file layout này

        // 1. Ánh xạ View
        val btnBack = findViewById<ImageButton>(R.id.btnBackHistory)
        rvOrderHistory = findViewById(R.id.rvOrderHistory)

        // 2. Xử lý nút Back
        btnBack.setOnClickListener {
            finish()
        }

        // 3. Cài đặt RecyclerView
        rvOrderHistory.layoutManager = LinearLayoutManager(this)
        adapter = OrderAdapter(orderList)
        rvOrderHistory.adapter = adapter

        // 4. Tải dữ liệu đơn hàng
        loadOrderHistory()
    }

    private fun loadOrderHistory() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Kiểm tra đăng nhập
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem lịch sử!", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        val userId = currentUser.uid

        // Truy vấn Firestore: Lấy đơn hàng của User hiện tại, sắp xếp theo ngày mới nhất
        db.collection("Orders")
            .whereEqualTo("userId", userId)
            .orderBy("orderDate", Query.Direction.DESCENDING) // Sắp xếp giảm dần theo thời gian
            .get()
            .addOnSuccessListener { documents ->
                orderList.clear() // Xóa dữ liệu cũ trước khi thêm mới
                for (document in documents) {
                    try {
                        // Chuyển đổi dữ liệu Firestore thành object Order
                        val order = document.toObject(Order::class.java)
                        // Gán lại ID từ document ID để đảm bảo chính xác (tuỳ chọn)
                        // order.id = document.id
                        orderList.add(order)
                    } catch (e: Exception) {
                        Log.e("OrderHistory", "Lỗi convert đơn hàng: ${e.message}")
                    }
                }

                // Cập nhật giao diện
                adapter.notifyDataSetChanged()

                if (orderList.isEmpty()) {
                    Toast.makeText(this, "Bạn chưa có đơn hàng nào.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Lỗi tải lịch sử: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("OrderHistory", "Error getting documents: ", exception)
            }
    }
}