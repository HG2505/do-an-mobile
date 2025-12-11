package com.example.giuaki

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var tvRegister: TextView
    private lateinit var btnGoogleLogin: ImageView
    private lateinit var auth: FirebaseAuth

    // Thêm biến Client của Google
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        auth = FirebaseAuth.getInstance()

        // 1. Cấu hình Google Sign In
        setupGoogleSignIn()

        initViews()
        setupListeners()
    }

    private fun setupGoogleSignIn() {
        // R.string.default_web_client_id tự động được tạo ra từ file google-services.json
        // Nếu báo đỏ, hãy Build -> Rebuild Project
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("705303677459-td0pt3s6099rccprrin17tcnoah2d2a9.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun initViews() {
        tilEmail = findViewById(R.id.til_email)
        etEmail = findViewById(R.id.et_email)
        tilPassword = findViewById(R.id.til_password)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        tvRegister = findViewById(R.id.tv_register)

        // Lưu ý: Trong XML bạn để trong CardView, nhưng bắt sự kiện vào ImageView vẫn được
        btnGoogleLogin = findViewById(R.id.imageView3)
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            handleLogin()
        }
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 2. Bắt sự kiện click nút Google
        btnGoogleLogin.setOnClickListener {
            signInGoogle()
        }
    }

    // 3. Launcher để nhận kết quả từ Google (Thay thế startActivityForResult cũ)
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            // Đăng nhập Google thành công -> Lấy tài khoản
            val account = task.getResult(ApiException::class.java)!!
            // Dùng token để xác thực với Firebase
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(this, "Google sign in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Đăng nhập Firebase thành công
                    Toast.makeText(this, "Đăng nhập Google thành công!", Toast.LENGTH_SHORT).show()
                    goToHome()
                } else {
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
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
                        goToHome()
                    } else {
                        val errorMessage = task.exception?.message ?: "Đăng nhập thất bại"
                        Toast.makeText(this, "Lỗi: $errorMessage", Toast.LENGTH_LONG).show()

                    }
                }
        }
    }
}