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
import kotlin.collections.joinToString
import kotlin.text.takeLast
import kotlin.text.uppercase

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

        // 1. Hiển thị Mã đơn hàng (cắt ngắn 6 ký tự cuối cho gọn và uppercase)
        val shortId = if (order.id.length > 6) "..." + order.id.takeLast(6).uppercase() else order.id
        holder.tvOrderId.text = "Mã đơn: $shortId"

        // 2. Format ngày tháng từ timestamp (Long) sang chuỗi dễ đọc
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.tvOrderDate.text = sdf.format(Date(order.orderDate))

        // 3. Format tổng tiền
        val formatter = DecimalFormat("#,###")
        holder.tvOrderTotal.text = "${formatter.format(order.totalPrice)} VND"

        // 4. Hiển thị trạng thái đơn hàng (Mặc định là "Đang xử lý")
        holder.tvOrderStatus.text = order.status

        // 5. Tóm tắt danh sách sản phẩm (Ví dụ: "CPU i9 x1, RAM 8GB x2")
        // Dùng joinToString để nối tên các sản phẩm lại với nhau
        val summary = order.items.joinToString(", ") { "${it.product.name} x${it.quantity}" }
        holder.tvOrderSummary.text = summary
    }

    override fun getItemCount(): Int = orders.size
}