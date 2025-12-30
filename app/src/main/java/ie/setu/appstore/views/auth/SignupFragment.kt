package ie.setu.appstore.views.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import ie.setu.appstore.R
import ie.setu.appstore.databinding.FragmentSignupBinding

class SignupFragment: Fragment(R.layout.fragment_signup) {
    private var binding: FragmentSignupBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignupBinding.bind(view)
        firebaseAuth = FirebaseAuth.getInstance()

        // https://www.youtube.com/watch?v=idbxxkF1l6k
        binding!!.btnEdit.setOnClickListener {
            val user = binding!!.username.text.toString()
            val pass = binding!!.password.text.toString()
            val pass2 = binding!!.passwordConfirm.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty() && pass2.isNotEmpty() && pass.length >= 6) {
                if (pass == pass2) {
                    firebaseAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener {
                        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                    }
                } else {
                    Toast.makeText(activity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Fields Missing", Toast.LENGTH_SHORT).show()
            }
        }
    }
}