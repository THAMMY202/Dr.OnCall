package com.healthteam14.droncall.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.PhoneBuilder
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.healthteam14.droncall.MainActivity
import com.healthteam14.droncall.databinding.ActivityAuthUiBinding
import com.healthteam14.droncall.utils.FirebaseUtils

class FirebaseAuthUIActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAuthUiBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this@FirebaseAuthUIActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val signInLauncher = registerForActivityResult(
                FirebaseAuthUIActivityResultContract()
            ) { result -> onSignInResult(result) }

            val providers = listOf(
                PhoneBuilder().build()
            )

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val userMetadata = FirebaseAuth.getInstance().currentUser?.metadata

            if (userMetadata != null) {
                if (userMetadata.creationTimestamp == userMetadata.lastSignInTimestamp) {
                    val userKey: String = FirebaseUtils.firebaseUser?.uid.toString()
                    saveUserDetails(userKey)
                } else {
                    val intent = Intent(this@FirebaseAuthUIActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        } else {
            finish()
        }
    }

    private fun saveUserDetails(userKey: String) {

    }

    private fun showMsg(msg: String) {
        val toast = Toast.makeText(this@FirebaseAuthUIActivity, msg, Toast.LENGTH_LONG)
        toast.show()
    }

    companion object {
        const val defaultUserRole: String = "Patient"
        private const val TAG = "FirebaseAuthUIActivity"
    }
}