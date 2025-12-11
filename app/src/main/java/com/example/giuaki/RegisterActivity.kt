package com.example.giuaki

import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var tilFullName: TextInputLayout
    private lateinit var etFullName: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etPassword: TextInputEditText
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnRegister: MaterialButton
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_screen)
        initViews()
        setupListeners()
    }

    private fun initViews() {
        tilFullName = findViewById(R.id.til_full_name)
        etFullName = findViewById(R.id.et_full_name)

        tilEmail = findViewById(R.id.til_email_register)
        etEmail = findViewById(R.id.et_email_register)

        tilPassword = findViewById(R.id.til_password_register)
        etPassword = findViewById(R.id.et_password_register)

        tilConfirmPassword = findViewById(R.id.til_confirm_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)

        btnRegister = findViewById(R.id.btn_register)
        tvLogin = findViewById(R.id.tv_login)
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener {
            handleRegister()
        }
        tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun handleRegister() {
        val fullName = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()
        tilFullName.error = null
        tilEmail.error = null
        tilPassword.error = null
        tilConfirmPassword.error = null

        var isValid = true

        if (fullName.isEmpty()) {
            tilFullName.error = "Vui lòng nhập họ và tên"
            isValid = false
        }

        if (email.isEmpty()) {
            tilEmail.error = "Vui lòng nhập Email"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Địa chỉ Email không hợp lệ"
            isValid = false
        }
        if (password.isEmpty()) {
            tilPassword.error = "Vui lòng nhập mật khẩu"
            isValid = false
        } else if (password.length < 6) {
            tilPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
            isValid = false
        }
        if (confirmPassword.isEmpty()) {
            tilConfirmPassword.error = "Vui lòng xác nhận mật khẩu"
            isValid = false
        } else if (password != confirmPassword) {
            tilConfirmPassword.error = "Mật khẩu xác nhận không khớp"
            isValid = false
        }
        if (isValid) {
            // TODO: Gọi API đăng ký hoặc Firebase SignUp tại đây
            Toast.makeText(this, "Đăng ký thành công tài khoản: $email", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}