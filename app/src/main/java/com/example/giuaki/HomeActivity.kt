package com.example.giuaki


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        val imgCart = findViewById<ImageView>(R.id.imgCart)
        val imgCategory = findViewById<ImageView>(R.id.imgCategories)
        imgCart.setOnClickListener {

            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        imgCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
    }
}