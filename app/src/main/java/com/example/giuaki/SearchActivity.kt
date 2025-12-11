package com.example.giuaki

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : AppCompatActivity() {
    private lateinit var rvSearchResult: RecyclerView
    private lateinit var edtSearchInput: EditText
    private lateinit var adapter: ProductAdapter
    private val allProducts = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        rvSearchResult = findViewById(R.id.rvSearchResult)
        edtSearchInput = findViewById(R.id.edtSearchInput)
        val btnBack = findViewById<ImageButton>(R.id.btnBackSearch)

        rvSearchResult.layoutManager = GridLayoutManager(this, 2)
        adapter = ProductAdapter(mutableListOf())
        rvSearchResult.adapter = adapter

        btnBack.setOnClickListener { finish() }

        loadAllProducts()

        edtSearchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }
        })

        edtSearchInput.requestFocus()
    }
    private fun loadAllProducts() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Product")
            .get()
            .addOnSuccessListener { documents ->
                allProducts.clear()
                for (document in documents) {
                    try {
                        val id = document.id
                        val name = document.getString("name") ?: ""
                        val price = document.getDouble("price") ?: 0.0
                        val description = document.getString("description") ?: ""
                        val imageUrl = document.getString("imageUrl") ?: ""
                        val category = document.getString("category") ?: ""

                        val product = Product(id, name, price, description, imageUrl, category)
                        allProducts.add(product)
                    } catch (e: Exception) {
                        Log.e("SearchErr", "Lỗi convert: ${e.message}")
                    }
                }
                updateList(allProducts)
            }
    }
    private fun filterProducts(query: String) {
        val searchText = query.lowercase().trim()

        if (searchText.isEmpty()) {
            updateList(allProducts)
        } else {
            val filteredList = allProducts.filter {
                it.name.lowercase().contains(searchText)
            }
            updateList(filteredList)
        }
    }
    private fun updateList(list: List<Product>) {
        adapter = ProductAdapter(list)
        rvSearchResult.adapter = adapter
    }
}