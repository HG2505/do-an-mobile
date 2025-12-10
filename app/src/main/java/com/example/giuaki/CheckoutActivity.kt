package com.example.giuaki

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat
import kotlin.collections.toList
import kotlin.text.isEmpty
import kotlin.text.trim

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val btnBack = findViewById<ImageButton>(R.id.btnBackCheckout)
        val tvTotal = findViewById<TextView>(R.id.tvCheckoutTotal)
        val edtName = findViewById<EditText>(R.id.edtReceiverName)
        val edtPhone = findViewById<EditText>(R.id.edtReceiverPhone)
        val edtAddress = findViewById<EditText>(R.id.edtReceiverAddress)

        val rgPayment = findViewById<RadioGroup>(R.id.rgPaymentMethod)
        val layoutBankInfo = findViewById<LinearLayout>(R.id.layoutBankInfo)
        val layoutMomoInfo = findViewById<LinearLayout>(R.id.layoutMomoInfo)

        val btnConfirm = findViewById<Button>(R.id.btnPlaceOrder)

        // Tính tổng tiền
        val formatter = DecimalFormat("#,###")
        val shipFee = 30000.0
        val subTotal = CartRepository.getTotalPrice()
        val finalTotal = subTotal + shipFee

        tvTotal.text = "${formatter.format(finalTotal)} VND"

        // Tự động điền tên
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            edtName.setText(currentUser.displayName)
        }

        // Xử lý sự kiện chọn phương thức thanh toán để hiện thông tin
        rgPayment.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbBankTransfer -> {
                    layoutBankInfo.visibility = View.VISIBLE
                    layoutMomoInfo.visibility = View.GONE
                }
                R.id.rbMomo -> {
                    layoutBankInfo.visibility = View.GONE
                    layoutMomoInfo.visibility = View.VISIBLE
                }
                else -> { // COD
                    layoutBankInfo.visibility = View.GONE
                    layoutMomoInfo.visibility = View.GONE
                }
            }
        }

        btnBack.setOnClickListener { finish() }

        btnConfirm.setOnClickListener {
            val name = edtName.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val address = edtAddress.text.toString().trim()

            val selectedPaymentId = rgPayment.checkedRadioButtonId

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
            } else if (selectedPaymentId == -1) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show()
            } else {
                val selectedRadioButton = findViewById<RadioButton>(selectedPaymentId)
                val paymentMethod = selectedRadioButton.text.toString()

                saveOrderToFirebase(name, phone, address, finalTotal, paymentMethod)
            }
        }
    }

    private fun saveOrderToFirebase(name: String, phone: String, address: String, total: Double, paymentMethod: String) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "GUEST"
        val orderRef = db.collection("Orders").document()
        val orderId = orderRef.id

        val order = Order(
            id = orderId,
            userId = userId,
            userName = name,
            userPhone = phone,
            userAddress = address,
            items = CartRepository.getItems().toList(),
            totalPrice = total,
            paymentMethod = paymentMethod,
            status = "Đang xử lý",
            orderDate = System.currentTimeMillis()
        )

        orderRef.set(order)
            .addOnSuccessListener {
                CartRepository.clearCart()
                Toast.makeText(this, "Đặt hàng thành công! Mã đơn: $orderId", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Lỗi đặt hàng: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}