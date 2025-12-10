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
    private lateinit var tvEmptyState: TextView // Dùng để hiện thông báo nếu không có đơn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history) // File xml của màn hình lịch sử

        // 1. Ánh xạ View
        val btnBack = findViewById<ImageButton>(R.id.btnBackHistory)
        rvOrderHistory = findViewById(R.id.rvOrderHistory)

        // Bạn có thể thêm 1 TextView vào layout activity_order_history.xml
        // để hiện chữ "Chưa có đơn hàng nào" và ánh xạ nó vào đây.
        // Ví dụ: tvEmptyState = findViewById(R.id.tvEmptyState)

        // 2. Setup RecyclerView
        rvOrderHistory.layoutManager = LinearLayoutManager(this)
        adapter = OrderAdapter(orderList)
        rvOrderHistory.adapter = adapter

        // 3. Nút Back
        btnBack.setOnClickListener {
            finish()
        }

        // 4. Gọi hàm lấy dữ liệu
        loadOrderHistory()
    }

    private fun loadOrderHistory() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()

        // Truy vấn: Lấy đơn hàng của User ID hiện tại, sắp xếp ngày mới nhất lên đầu
        db.collection("Orders")
            .whereEqualTo("userId", user.uid)
            // Lưu ý: Để dùng orderBy kết hợp whereEqualTo, bạn có thể cần tạo Index trên Firebase Console
            // Nếu chạy bị lỗi, hãy xem Logcat nó sẽ gửi 1 đường link để tạo Index.
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                orderList.clear()
                for (document in documents) {
                    try {
                        // Convert dữ liệu Firestore sang Object Order
                        val order = document.toObject(Order::class.java)
                        // Gán ID của document vào object để sau này dùng (ví dụ để huỷ đơn)
                        order.id = document.id
                        orderList.add(order)
                    } catch (e: Exception) {
                        Log.e("OrderHistory", "Lỗi convert data: ${e.message}")
                    }
                }

                adapter.notifyDataSetChanged()

                // Kiểm tra nếu danh sách rỗng
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