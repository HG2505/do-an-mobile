package com.example.giuaki

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter(private val orders: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    // ViewHolder giúp ánh xạ các view từ layout item_order.xml
    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvOrderDate: TextView = view.findViewById(R.id.tvOrderDate)
        val tvOrderSummary: TextView = view.findViewById(R.id.tvOrderSummary)
        val tvOrderTotal: TextView = view.findViewById(R.id.tvOrderTotal)
        val tvOrderStatus: TextView = view.findViewById(R.id.tvOrderStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        // Nạp layout item_order.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        // 1. Hiển thị Mã đơn hàng (An toàn: kiểm tra null trước khi xử lý chuỗi)
        val orderId = order.id ?: "" // Nếu id null thì gán chuỗi rỗng
        val shortId = if (orderId.length > 6) "..." + orderId.takeLast(6).uppercase() else orderId
        holder.tvOrderId.text = "Mã đơn: $shortId"

        // 2. Format ngày tháng
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.tvOrderDate.text = sdf.format(Date(order.orderDate))

        // 3. Format tổng tiền
        val formatter = DecimalFormat("#,###")
        holder.tvOrderTotal.text = "${formatter.format(order.totalPrice)} VND"

        // 4. Hiển thị trạng thái
        holder.tvOrderStatus.text = order.status ?: "Đang xử lý"

        // 5. Tóm tắt danh sách sản phẩm (ĐÃ SỬA LỖI CRASH TẠI ĐÂY)
        // Dùng try-catch và kiểm tra null để tránh app bị tắt đột ngột nếu dữ liệu lỗi
        try {
            if (order.items != null && order.items.isNotEmpty()) {
                val summary = order.items.joinToString(", ") { item ->
                    // Kiểm tra kỹ: nếu item.product bị null thì hiện tên mặc định
                    val productName = item.product?.name ?: "Sản phẩm (?)"
                    "$productName x${item.quantity}"
                }
                holder.tvOrderSummary.text = summary
            } else {
                holder.tvOrderSummary.text = "Không có thông tin chi tiết"
            }
        } catch (e: Exception) {
            // Nếu có bất kỳ lỗi gì khi xử lý chuỗi, app vẫn chạy tiếp và hiện thông báo lỗi nhẹ
            holder.tvOrderSummary.text = "Lỗi hiển thị danh sách"
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int = orders.size
}