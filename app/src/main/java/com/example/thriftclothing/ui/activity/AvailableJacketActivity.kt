package com.example.thriftclothing.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.thriftclothing.R

class AvailableJacketActivity : AppCompatActivity() {
    private lateinit var subtotalAmount1: TextView
    private lateinit var totalAmount1: TextView
    private lateinit var subtotalAmount2: TextView
    private lateinit var totalAmount2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_available_jacket)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize TextViews
        subtotalAmount1 = findViewById(R.id.subtotalAmount1)
        totalAmount1 = findViewById(R.id.totalAmount1)
        subtotalAmount2 = findViewById(R.id.subtotalAmount2)
        totalAmount2 = findViewById(R.id.totalAmount2)

        // Buttons for first product
        val addProductButton = findViewById<Button>(R.id.addProductButton)
        val deleteProductButton = findViewById<Button>(R.id.deleteProductButton)
        val updatePriceButton = findViewById<Button>(R.id.updatePriceButton)

        // Buttons for second product
        val addProductButton1 = findViewById<Button>(R.id.addProductButton1)
        val deleteProductButton1 = findViewById<Button>(R.id.deleteProductButton1)
        val updatePriceButton1 = findViewById<Button>(R.id.updatePriceButton1)

        // Button actions
        addProductButton.setOnClickListener { addProduct(1) }
        deleteProductButton.setOnClickListener { deleteProduct(1) }
        updatePriceButton.setOnClickListener { updatePrice(1) }

        addProductButton1.setOnClickListener { addProduct(2) }
        deleteProductButton1.setOnClickListener { deleteProduct(2) }
        updatePriceButton1.setOnClickListener { updatePrice(2) }
    }

    private fun addProduct(productId: Int) {
        Toast.makeText(this, "Product $productId added to cart!", Toast.LENGTH_SHORT).show()
    }

    private fun deleteProduct(productId: Int) {
        Toast.makeText(this, "Product $productId removed from cart!", Toast.LENGTH_SHORT).show()
    }

    private fun updatePrice(productId: Int) {
        if (productId == 1) {
            subtotalAmount1.text = "$320.00"
            totalAmount1.text = "$320.00"
        } else {
            subtotalAmount2.text = "$410.00"
            totalAmount2.text = "$410.00"
        }
        Toast.makeText(this, "Price for Product $productId updated!", Toast.LENGTH_SHORT).show()
    }
}
