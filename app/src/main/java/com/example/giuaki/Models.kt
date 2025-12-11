package com.example.giuaki

import java.io.Serializable


data class Category(
    val id: Int,
    val name: String,
    val icon: Int,
    val bgColor: String,
    val textColor: String
)
data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val imageUrl: String = "",
    val category: String = ""
) : Serializable
data class CartItem(
    val product: Product = Product(),
    var quantity: Int = 0
)
data class Order(
    var id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhone: String = "",
    val userAddress: String = "",
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val paymentMethod: String = "",
    val status: String = "Đang xử lý",
    val orderDate: Long = System.currentTimeMillis()
) : Serializable

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
    fun removeFromCart(item: CartItem) {
        items.remove(item)
    }
    fun updateQuantity(item: CartItem, newQuantity: Int) {
        if (newQuantity > 0) {
            item.quantity = newQuantity
        } else {
            items.remove(item)
        }
    }
    fun getTotalPrice(): Double {
        return items.sumOf { it.product.price * it.quantity }
    }
    fun clearCart() {
        items.clear()
    }
}