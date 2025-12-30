package ie.setu.appstore.views.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import ie.setu.appstore.R
import ie.setu.appstore.activities.MainActivity
import ie.setu.appstore.databinding.FragmentLoginBinding
import kotlinx.coroutines.runBlocking
import timber.log.Timber.i

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

        binding!!.btnGoogle.setOnClickListener {
            try {
                runBlocking { signInGoogle() }
            } catch(e: Exception) {

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    (activity as MainActivity).updateDrawer()
                    findNavController().navigate(R.id.action_loginFragment_to_homeViewFragment)
                } else {
                    // If sign in fails, display a message to the user
                }
            }
    }

    suspend fun signInGoogle() {
        // Instantiate a Google sign-in request
//        val googleIdOption = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false)
//            // Your server's client ID, not your Android client ID.
//            .setServerClientId(getString(R.string.default_web_client_id))
//            // Only show accounts previously used to sign in.
//            .setFilterByAuthorizedAccounts(true)
//            .build()

        val signInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(serverClientId = requireContext().getString(R.string.default_web_client_id))
            .build()

        // Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val result = CredentialManager.create(requireContext()).getCredential(
            request = request,
            context = requireContext()
        )

        val credential = result.credential

        i(credential.type)

        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            i("b")

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        }
    }
}