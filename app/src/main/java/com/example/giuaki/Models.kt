package com.example.giuaki

import java.io.Serializable

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val imageUrl: String = "",
    val category: String = ""
) : Serializable

data class CartItem(
    val product: Product,
    var quantity: Int
)
object CartRepository {
    private val items = mutableListOf<CartItem>()
    fun getItems(): MutableList<CartItem> = items
    fun addToCart(product: Product) {
        val existingItem = items.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            items.add(CartItem(product, 1))
        }
    }
    fun getTotalPrice(): Double {
        return items.sumOf { it.product.price * it.quantity }
    }
    fun clearCart() {
        items.clear()
    }
    // Hàm xóa 1 sản phẩm khỏi giỏ (nếu cần sau này)
    fun removeFromCart(product: Product) {
        items.removeIf { it.product.id == product.id }
    }
}