package com.example.giuaki

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var rvFeaturedProducts: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val featuredList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setupFeaturedRecyclerView()
        loadFeaturedProducts()

        val imgCart = findViewById<ImageView>(R.id.imgCart)
        val imgCategory = findViewById<ImageView>(R.id.imgCategories)
        val imgCustomer = findViewById<ImageView>(R.id.imgavatar)
        val search= findViewById<TextView>(R.id.tvSearch)
        val imgCPU = findViewById<ImageView>(R.id.imgCPU)
        val imgVGA = findViewById<ImageView>(R.id.imgVGA)
        val imgRAM = findViewById<ImageView>(R.id.imgRAM)
        val imgMB = findViewById<ImageView>(R.id.imgMB)
        val imgPSU = findViewById<ImageView>(R.id.imgPSU)
        val imgSSD = findViewById<ImageView>(R.id.imgSSD)

        imgCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        imgCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
        imgCustomer.setOnClickListener {
            val intent = Intent(this, CustomerActivity::class.java)
            startActivity(intent)
        }
        search.setOnClickListener { val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        // ... (Giữ nguyên các sự kiện click category) ...
        imgCPU.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("CATEGORY_NAME", "cpu")
            startActivity(intent)
        }
        imgVGA.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("CATEGORY_NAME", "vga")
            startActivity(intent)
        }
        imgRAM.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("CATEGORY_NAME", "ram")
            startActivity(intent)
        }
        imgMB.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("CATEGORY_NAME", "mainboard")
            startActivity(intent)
        }
        imgSSD.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("CATEGORY_NAME", "ssd")
            startActivity(intent)
        }
        imgPSU.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("CATEGORY_NAME", "psu")
            startActivity(intent)
        }
    }

    private fun setupFeaturedRecyclerView() {
        rvFeaturedProducts = findViewById(R.id.rvFeaturedProducts)
        adapter = ProductAdapter(featuredList)
        rvFeaturedProducts.adapter = adapter
        rvFeaturedProducts.layoutManager = GridLayoutManager(this, 2)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadFeaturedProducts() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Product")
            .get()
            .addOnSuccessListener { documents ->
                featuredList.clear()
                val tempAllProducts = mutableListOf<Product>()
                for (document in documents) {
                    try {
                        val product = Product(
                            id = document.id,
                            name = document.getString("name") ?: "No Name",
                            price = document.getDouble("price") ?: 0.0,
                            description = document.getString("description") ?: "",
                            imageUrl = document.getString("imageUrl") ?: "",
                            category = document.getString("category")
                                ?: ""
                        )
                        tempAllProducts.add(product)
                    } catch (e: Exception) {
                        Log.e("FirestoreError", "Lỗi convert: ${e.message}")
                    }
                }

                val categories = listOf("cpu", "vga", "ram", "mainboard", "ssd", "psu")

                for (cat in categories) {
                    val randomTop2 = tempAllProducts
                        .filter { it.category == cat }
                        .shuffled()
                        .take(2)

                    featuredList.addAll(randomTop2)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Lỗi tải sản phẩm: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}