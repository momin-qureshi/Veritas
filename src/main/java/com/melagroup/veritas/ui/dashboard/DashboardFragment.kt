package com.melagroup.veritas.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.melagroup.veritas.R
import com.melagroup.veritas.ui.login.LoginActivity

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private val auth : FirebaseAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val logoutButton = root.findViewById<Button>(R.id.button_logout)
        logoutButton.setOnClickListener(View.OnClickListener {
            auth.signOut()
            activity?.finish()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })

        return root
    }
}