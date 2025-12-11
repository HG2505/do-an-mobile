package com.example.giuaki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

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
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}