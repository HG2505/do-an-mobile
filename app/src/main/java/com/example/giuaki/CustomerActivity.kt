package com.example.giuaki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class CustomerActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_activity)

        auth = FirebaseAuth.getInstance()

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val tvCustomerName = findViewById<TextView>(R.id.tvCustomerName)
        val btnOrderHistory = findViewById<LinearLayout>(R.id.btnOrderHistory)
        val user = auth.currentUser
        if (user != null) {
            val name = user.displayName ?: user.email ?: "Khách hàng"
            tvCustomerName.text = name
        }
        btnOrderHistory.setOnClickListener {
            val intent = Intent(this, OrderHistoryActivity::class.java)
            startActivity(intent)
        }

        val btnAddress = findViewById<LinearLayout>(R.id.btnAddress)
        btnAddress.setOnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            // 1. Đăng xuất khỏi Firebase
            auth.signOut()

            // 2. Cấu hình để lấy GoogleSignInClient (cần thiết để gọi lệnh đăng xuất)
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)

            // 3. Thực hiện đăng xuất khỏi Google (Xóa cache tài khoản)
            googleSignInClient.signOut().addOnCompleteListener(this) {
                // Sau khi xóa cache Google thành công thì mới chuyển màn hình
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}