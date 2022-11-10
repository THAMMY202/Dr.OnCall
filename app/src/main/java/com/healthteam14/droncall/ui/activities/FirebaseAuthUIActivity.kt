package com.healthteam14.droncall.ui.activities

import android.content.Intent
import android.os.Bundle
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
            val signInLauncher =
                registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
                    onSignInResult(result)
                }

            val providers = listOf(PhoneBuilder().build())

            val signInIntent =
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                    .build()
            signInLauncher.launch(signInIntent)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {

        if (result.resultCode == RESULT_OK) {
            val userMetadata = FirebaseAuth.getInstance().currentUser?.metadata

            if (userMetadata != null) {
                if (userMetadata.creationTimestamp == userMetadata.lastSignInTimestamp) {
                    val userKey: String = FirebaseUtils.firebaseUser?.uid.toString()
                    val userPhone: String? =  FirebaseAuth.getInstance().currentUser?.phoneNumber
                    navToNextStep(userKey,userPhone)
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

    private fun navToNextStep(id: String,phoneNumber: String?) {
        val intent = Intent(this@FirebaseAuthUIActivity, SecondRegStepActivity::class.java)
        intent.putExtra(UserKey, id)
        intent.putExtra(UserPhone, phoneNumber)
        startActivity(intent)
        finish()
    }

    companion object {
        const val UserKey: String = "UserKey"
        const val UserPhone: String = "Phone"
    }

}