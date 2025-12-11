package com.example.giuaki

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AddressActivity : AppCompatActivity() {

    private lateinit var edtName: TextInputEditText
    private lateinit var edtPhone: TextInputEditText
    private lateinit var edtAddress: TextInputEditText
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        val btnBack = findViewById<ImageButton>(R.id.btnBackAddress)
        val btnSave = findViewById<Button>(R.id.btnSaveAddress)
        edtName = findViewById(R.id.edtAddressName)
        edtPhone = findViewById(R.id.edtAddressPhone)
        edtAddress = findViewById(R.id.edtAddressDetail)
        btnBack.setOnClickListener { finish() }
        loadCurrentAddress()
        btnSave.setOnClickListener {
            saveAddressToFirebase()
        }
    }

    private fun loadCurrentAddress() {
        val userId = auth.currentUser?.uid
        if (userId == null) return

        db.collection("Users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    edtName.setText(document.getString("fullName"))
                    edtPhone.setText(document.getString("phoneNumber"))
                    edtAddress.setText(document.getString("address"))
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Lỗi tải địa chỉ", Toast.LENGTH_SHORT).show()
            }
    }
    private fun saveAddressToFirebase() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
            return
        }
        val name = edtName.text.toString().trim()
        val phone = edtPhone.text.toString().trim()
        val address = edtAddress.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }
        val userMap = hashMapOf(
            "fullName" to name,
            "phoneNumber" to phone,
            "address" to address
        )
        db.collection("Users").document(userId)
            .set(userMap, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Lưu địa chỉ thành công!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Lỗi lưu: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}