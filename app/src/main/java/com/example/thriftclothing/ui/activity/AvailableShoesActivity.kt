package com.example.thriftclothing.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.thriftclothing.R
import com.google.firebase.database.*

data class Shoe(val id: String = "", val name: String = "", val price: String = "", val size: String = "", val color: String = "")

class AvailableShoesActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var productsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_shoes)

        database = FirebaseDatabase.getInstance().getReference("Shoes")
        productsContainer = findViewById(R.id.productsContainer)

        val shoeNameEditText = findViewById<EditText>(R.id.shoeNameEditText)
        val priceEditText = findViewById<EditText>(R.id.priceEditText)
        val sizeEditText = findViewById<EditText>(R.id.sizeEditText)
        val colorEditText = findViewById<EditText>(R.id.colorEditText)
        val addProductButton = findViewById<Button>(R.id.addProductButton)

        addProductButton.setOnClickListener {
            val shoeName = shoeNameEditText.text.toString()
            val price = priceEditText.text.toString()
            val size = sizeEditText.text.toString()
            val color = colorEditText.text.toString()

            if (shoeName.isNotEmpty() && price.isNotEmpty() && size.isNotEmpty() && color.isNotEmpty()) {
                val productId = database.push().key ?: return@setOnClickListener
                val shoe = Shoe(productId, shoeName, price, size, color)

                database.child(productId).setValue(shoe).addOnSuccessListener {
                    Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show()
                    shoeNameEditText.text.clear()
                    priceEditText.text.clear()
                    sizeEditText.text.clear()
                    colorEditText.text.clear()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
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
                    val shoe = productSnapshot.getValue(Shoe::class.java)
                    if (shoe != null) {
                        addProductView(shoe)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AvailableShoesActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addProductView(shoe: Shoe) {
        val productView = LinearLayout(this)
        productView.orientation = LinearLayout.VERTICAL
        productView.setPadding(20, 20, 20, 20)
        productView.setBackgroundResource(android.R.color.darker_gray)

        val textView = TextView(this)
        textView.text = "Shoe: ${shoe.name}\nPrice: ${shoe.price}\nSize: ${shoe.size}\nColor: ${shoe.color}"
        textView.textSize = 16f
        textView.setPadding(10, 10, 10, 10)

        val updateButton = Button(this)
        updateButton.text = "Update"
        updateButton.setOnClickListener {
            showUpdateDialog(shoe)
        }

        val removeButton = Button(this)
        removeButton.text = "Remove"
        removeButton.setOnClickListener {
            database.child(shoe.id).removeValue()
        }

        productView.addView(textView)
        productView.addView(updateButton)
        productView.addView(removeButton)
        productsContainer.addView(productView)
    }

    private fun showUpdateDialog(shoe: Shoe) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_product, null)
        val shoeNameEditText = dialogView.findViewById<EditText>(R.id.updateShoeNameEditText)
        val priceEditText = dialogView.findViewById<EditText>(R.id.updatePriceEditText)
        val sizeEditText = dialogView.findViewById<EditText>(R.id.updateSizeEditText)
        val colorEditText = dialogView.findViewById<EditText>(R.id.updateColorEditText)

        shoeNameEditText.setText(shoe.name)
        priceEditText.setText(shoe.price)
        sizeEditText.setText(shoe.size)
        colorEditText.setText(shoe.color)

        AlertDialog.Builder(this)
            .setTitle("Update Product")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedShoe = Shoe(
                    shoe.id,
                    shoeNameEditText.text.toString(),
                    priceEditText.text.toString(),
                    sizeEditText.text.toString(),
                    colorEditText.text.toString()
                )

                database.child(shoe.id).setValue(updatedShoe).addOnSuccessListener {
                    Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
