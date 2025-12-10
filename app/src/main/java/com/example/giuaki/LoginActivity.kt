package com.example.giuaki

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var tvRegister: TextView
    private lateinit var btnGoogleLogin: ImageView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        auth = FirebaseAuth.getInstance()
        initViews()
        setupListeners()
    }

    private fun initViews() {
        tilEmail = findViewById(R.id.til_email)
        etEmail = findViewById(R.id.et_email)
        tilPassword = findViewById(R.id.til_password)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        tvRegister = findViewById(R.id.tv_register)
        btnGoogleLogin = findViewById(R.id.imageView3) // Nút Google (đang là imageView3 trong XML)

    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            handleLogin()
        }
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnGoogleLogin.setOnClickListener {
            Toast.makeText(this, "Chức năng đăng nhập Google đang phát triển", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun handleLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        tilEmail.error = null
        tilPassword.error = null
        var isValid = true
        if (email.isEmpty()) {
            tilEmail.error = "Vui lòng nhập Email"
            isValid = false
        }
        if (password.isEmpty()) {
            tilPassword.error = "Vui lòng nhập mật khẩu"
            isValid = false
        } else if (password.length < 6) {
            tilPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
            isValid = false
        }
        if (isValid) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorMessage = task.exception?.message ?: "Đăng nhập thất bại"

                        Toast.makeText(this, "Lỗi: $errorMessage", Toast.LENGTH_LONG).show()

                    }
                }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Nếu user hiện tại khác null -> Đã đăng nhập -> Chuyển thẳng vào Home
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}