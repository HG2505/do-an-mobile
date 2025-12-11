package com.example.giuaki

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ProductListActivity : AppCompatActivity() {

    private lateinit var rvProductList: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_list)
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: ""
        val tvTitle = findViewById<TextView>(R.id.tvCategoryTitle)
        tvTitle.text = "Danh mục: ${categoryName.uppercase()}"
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
        setupRecyclerView()
        loadProductsFromFirebase(categoryName)
    }
    private fun setupRecyclerView() {
        rvProductList = findViewById(R.id.rvProductList)
        adapter = ProductAdapter(productList)
        rvProductList.adapter = adapter
        rvProductList.layoutManager = GridLayoutManager(this, 2)
    }
    private fun loadProductsFromFirebase(category: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Product")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { documents ->
                productList.clear()
                for (document in documents) {
                    try {
                        val id = document.id
                        val name = document.getString("name") ?: "No Name"
                        val price = document.getDouble("price") ?: 0.0
                        val description = document.getString("description") ?: ""
                        val imageUrl = document.getString("imageUrl") ?: ""
                        val product = Product(
                            id = id,
                            name = name,
                            price = price,
                            description = description,
                            imageUrl = imageUrl,
                            category = category
                        )
                        productList.add(product)
                    } catch (e: Exception) {
                        Log.e("FirestoreError", "Lỗi convert data: ${e.message}")
                    }
                }
                adapter.notifyDataSetChanged()
                if (productList.isEmpty()) {
                    Toast.makeText(this, "Chưa có sản phẩm nào cho mục $category", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Lỗi tải dữ liệu: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }
    }
}