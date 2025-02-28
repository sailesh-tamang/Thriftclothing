package com.example.thriftclothing.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.thriftclothing.R
import com.google.firebase.database.*

data class Jeans(val id: String = "", val name: String = "", val price: String = "", val size: String = "", val color: String = "")

class AvailableJeansActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var productsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_jeans)

        // Initialize Firebase database reference for Jeans
        database = FirebaseDatabase.getInstance().getReference("Jeans")
        productsContainer = findViewById(R.id.productsContainer)

        // Initialize UI elements
        val jeansNameEditText = findViewById<EditText>(R.id.jeansNameEditText)
        val priceEditText = findViewById<EditText>(R.id.priceEditText)
        val sizeEditText = findViewById<EditText>(R.id.sizeEditText)
        val colorEditText = findViewById<EditText>(R.id.colorEditText)
        val addProductButton = findViewById<Button>(R.id.addProductButton)

        // Add Product
        addProductButton.setOnClickListener {
            val jeansName = jeansNameEditText.text.toString()
            val price = priceEditText.text.toString()
            val size = sizeEditText.text.toString()
            val color = colorEditText.text.toString()

            if (jeansName.isNotEmpty() && price.isNotEmpty() && size.isNotEmpty() && color.isNotEmpty()) {
                val productId = database.push().key ?: return@setOnClickListener
                val jeans = Jeans(productId, jeansName, price, size, color)

                database.child(productId).setValue(jeans).addOnSuccessListener {
                    Toast.makeText(this, "Jeans added", Toast.LENGTH_SHORT).show()
                    jeansNameEditText.text.clear()
                    priceEditText.text.clear()
                    sizeEditText.text.clear()
                    colorEditText.text.clear()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to add jeans", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Load products from Firebase
        loadProducts()
    }

    private fun loadProducts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productsContainer.removeAllViews()
                for (productSnapshot in snapshot.children) {
                    val jeans = productSnapshot.getValue(Jeans::class.java)
                    if (jeans != null) {
                        addProductView(jeans)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@AvailableJeansActivity,
                    "Failed to load jeans",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun addProductView(jeans: Jeans) {
        val productView = LinearLayout(this)
        productView.orientation = LinearLayout.VERTICAL
        productView.setPadding(20, 20, 20, 20)
        productView.setBackgroundResource(android.R.color.darker_gray)

        val textView = TextView(this)
        textView.text =
            "Jeans: ${jeans.name}\nPrice: ${jeans.price}\nSize: ${jeans.size}\nColor: ${jeans.color}"
        textView.textSize = 16f
        textView.setPadding(10, 10, 10, 10)

        val updateButton = Button(this)
        updateButton.text = "Update"
        updateButton.setOnClickListener {
            showUpdateDialog(jeans)
        }

        val removeButton = Button(this)
        removeButton.text = "Remove"
        removeButton.setOnClickListener {
            database.child(jeans.id).removeValue()
        }

        productView.addView(textView)
        productView.addView(updateButton)
        productView.addView(removeButton)
        productsContainer.addView(productView)
    }

    private fun showUpdateDialog(jeans: Jeans) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_jeans, null)

        val nameEditText = dialogView.findViewById<EditText>(R.id.updateJeansNameEditText)
        val priceEditText = dialogView.findViewById<EditText>(R.id.updateJeansPriceEditText)
        val sizeEditText = dialogView.findViewById<EditText>(R.id.updateJeansSizeEditText)
        val colorEditText = dialogView.findViewById<EditText>(R.id.updateJeansColorEditText)

        nameEditText.setText(jeans.name)
        priceEditText.setText(jeans.price)
        sizeEditText.setText(jeans.size)
        colorEditText.setText(jeans.color)

        AlertDialog.Builder(this)
            .setTitle("Update Jeans")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedJeans = Jeans(
                    jeans.id,
                    nameEditText.text.toString(),
                    priceEditText.text.toString(),
                    sizeEditText.text.toString(),
                    colorEditText.text.toString()
                )

                database.child(jeans.id).setValue(updatedJeans).addOnSuccessListener {
                    Toast.makeText(this, "Jeans updated", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
