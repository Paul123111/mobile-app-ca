package ie.setu.appstore.views.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import ie.setu.appstore.R
import ie.setu.appstore.activities.MainActivity
import ie.setu.appstore.databinding.FragmentLoginBinding
import ie.setu.appstore.databinding.FragmentSignupBinding

class LoginFragment: Fragment(R.layout.fragment_login) {
    private var binding: FragmentLoginBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        firebaseAuth = FirebaseAuth.getInstance()

        binding!!.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding!!.btnEdit.setOnClickListener {
            val user = binding!!.username.text.toString()
            val pass = binding!!.password.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    (activity as MainActivity).updateDrawer()
                    findNavController().navigate(R.id.action_loginFragment_to_homeViewFragment)
                } else {
                    Toast.makeText(
                        activity,
                        "Incorrect username or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            } else {
                Toast.makeText(activity, "Fields Missing", Toast.LENGTH_SHORT).show()
            }
        }
    }
}