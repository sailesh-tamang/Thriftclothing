package com.example.thriftclothing.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thriftclothing.R
import com.example.thriftclothing.LoginPage
import com.example.thriftclothing.ui.activity.AvailableShoesActivity
import com.example.thriftclothing.ui.activity.AvailableJeansActivity
import com.example.thriftclothing.ui.activity.AvailableJacketActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Handle Logout button click
        val logoutButton = view.findViewById<View>(R.id.logout) // Get the logout button (CardView)
        logoutButton.setOnClickListener {
            // Navigate to LoginPage
            val intent = Intent(activity, LoginPage::class.java)
            startActivity(intent)
            activity?.finish() // Close the current fragment or activity after navigating
        }

        // Handle Add Products button click
        val addProductButton = view.findViewById<View>(R.id.settings) // "Add Products" button

        addProductButton.setOnClickListener {
            val productOptions = arrayOf("Shoes", "Jeans", "Jacket")

            AlertDialog.Builder(requireContext())
                .setTitle("Choose Product Type")
                .setItems(productOptions) { _, which ->
                    val selectedProduct = productOptions[which]
                    val intent = when (selectedProduct) {
                        "Shoes" -> Intent(activity, AvailableShoesActivity::class.java)
                        "Jeans" -> Intent(activity, AvailableJeansActivity::class.java)
                        "Jacket" -> Intent(activity, AvailableJacketActivity::class.java)
                        else -> null
                    }
                    intent?.let { startActivity(it) }
                }
                .show()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
