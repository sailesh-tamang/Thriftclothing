package com.example.thriftclothing.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.thriftclothing.R
import com.google.firebase.database.*

data class Jacket(val id: String = "", val name: String = "", val price: String = "", val size: String = "", val color: String = "")

class AvailableJacketActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var productsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_jacket)

        database = FirebaseDatabase.getInstance().getReference("Jackets")
        productsContainer = findViewById(R.id.productsContainer)

        val jacketNameEditText = findViewById<EditText>(R.id.jacketNameEditText)
        val priceEditText = findViewById<EditText>(R.id.priceEditText)
        val sizeEditText = findViewById<EditText>(R.id.sizeEditText)
        val colorEditText = findViewById<EditText>(R.id.colorEditText)
        val addProductButton = findViewById<Button>(R.id.addProductButton)

        // Add Product
        addProductButton.setOnClickListener {
            val jacketName = jacketNameEditText.text.toString()
            val price = priceEditText.text.toString()
            val size = sizeEditText.text.toString()
            val color = colorEditText.text.toString()

            if (jacketName.isNotEmpty() && price.isNotEmpty() && size.isNotEmpty() && color.isNotEmpty()) {
                val productId = database.push().key ?: return@setOnClickListener
                val jacket = Jacket(productId, jacketName, price, size, color)

                database.child(productId).setValue(jacket).addOnSuccessListener {
                    Toast.makeText(this, "Jacket added", Toast.LENGTH_SHORT).show()
                    jacketNameEditText.text.clear()
                    priceEditText.text.clear()
                    sizeEditText.text.clear()
                    colorEditText.text.clear()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to add jacket", Toast.LENGTH_SHORT).show()
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
                    val jacket = productSnapshot.getValue(Jacket::class.java)
                    if (jacket != null) {
                        addProductView(jacket)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@AvailableJacketActivity,
                    "Failed to load jackets",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun addProductView(jacket: Jacket) {
        val productView = LinearLayout(this)
        productView.orientation = LinearLayout.VERTICAL
        productView.setPadding(20, 20, 20, 20)
        productView.setBackgroundResource(android.R.color.darker_gray)

        val textView = TextView(this)
        textView.text =
            "Jacket: ${jacket.name}\nPrice: ${jacket.price}\nSize: ${jacket.size}\nColor: ${jacket.color}"
        textView.textSize = 16f
        textView.setPadding(10, 10, 10, 10)

        val updateButton = Button(this)
        updateButton.text = "Update"
        updateButton.setOnClickListener {
            showUpdateDialog(jacket)
        }

        val removeButton = Button(this)
        removeButton.text = "Remove"
        removeButton.setOnClickListener {
            database.child(jacket.id).removeValue()
        }

        productView.addView(textView)
        productView.addView(updateButton)
        productView.addView(removeButton)
        productsContainer.addView(productView)
    }

    private fun showUpdateDialog(jacket: Jacket) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_jacket, null)

        val nameEditText = dialogView.findViewById<EditText>(R.id.updateJacketNameEditText)
        val priceEditText = dialogView.findViewById<EditText>(R.id.updateJacketPriceEditText)
        val sizeEditText = dialogView.findViewById<EditText>(R.id.updateJacketSizeEditText)
        val colorEditText = dialogView.findViewById<EditText>(R.id.updateJacketColorEditText)

        nameEditText.setText(jacket.name)
        priceEditText.setText(jacket.price)
        sizeEditText.setText(jacket.size)
        colorEditText.setText(jacket.color)

        AlertDialog.Builder(this)
            .setTitle("Update Jacket")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedJacket = Jacket(
                    jacket.id,
                    nameEditText.text.toString(),
                    priceEditText.text.toString(),
                    sizeEditText.text.toString(),
                    colorEditText.text.toString()
                )

                database.child(jacket.id).setValue(updatedJacket).addOnSuccessListener {
                    Toast.makeText(this, "Jacket updated", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
