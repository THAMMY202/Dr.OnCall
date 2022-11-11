package com.healthteam14.droncall.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import com.healthteam14.droncall.R
import com.healthteam14.droncall.models.User
import com.healthteam14.droncall.utils.FirebaseUtils
import java.util.*

class SecondRegStepActivity : AppCompatActivity() {

    private lateinit var userID: String
    private  var userPhone: String? = null
    private lateinit var gender: String
    private lateinit var datePicker : DatePicker
    private lateinit var nameEditText : EditText
    private lateinit var surnameEditText : EditText
    private lateinit var emailEditText : EditText
    private lateinit var signUpButton : Button
    private  var yearOfBirth : Int = 0
    private lateinit var user :User

    var radioGroup: RadioGroup? = null
    lateinit var radioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_reg_step)

        userID = intent.getStringExtra(UserKey).toString()
        userPhone = intent.getStringExtra(UserPhone).toString()

        showMsg(userID)

        intiViews()
    }

    private fun valDUserData(){
        if(nameEditText.text.isNullOrBlank()){
            nameEditText.error = "first name required"
            return
        }else if(surnameEditText.text.isNullOrBlank()){
            surnameEditText.error = "surname is required"
            return
        }else if(yearOfBirth == 0){
            showMsg("birth year is required")
            return
        }

        // Getting the checked radio button id
        // from the radio group
        val selectedOption: Int = radioGroup!!.checkedRadioButtonId

        // Assigning id of the checked radio button
        radioButton = findViewById(selectedOption)
        if(radioButton.text.isNullOrBlank()){
            showMsg("select your gender")
            return
        }else{
            gender = radioButton.text.toString()
        }


    }

    private fun intiViews(){
        datePicker = findViewById(R.id.dateOfBirthPicker)
        nameEditText = findViewById(R.id.edtFirstName)
        surnameEditText = findViewById(R.id.edtSurname)
        emailEditText = findViewById(R.id.edtEmailAddress)
        signUpButton = findViewById(R.id.btnSignUp)
        radioGroup = findViewById(R.id.rgGender)

        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            yearOfBirth = year
            val month = month + 1
            val msg = "You Selected: $day/$month/$year"
           // showMsg(msg)
        }

        signUpButton.setOnClickListener {
            //Validate user data
            valDUserData()

             user = User(emailEditText.text.toString(),nameEditText.text.toString(),surnameEditText.text.toString(),userPhone,defaultUserRole,userID,gender
             ,yearOfBirth.toString())

            saveUserDetails(user)
        }

    }

    private fun saveUserDetails(user:User){
        FirebaseUtils.database = FirebaseDatabase.getInstance().reference.child("Users")
        FirebaseUtils.database.child(user.key).setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this@SecondRegStepActivity, PatientMainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                showMsg(it.toString())
            }
    }

    private fun showMsg(msg: String) {
        val toast = Toast.makeText(this@SecondRegStepActivity, msg, Toast.LENGTH_LONG)
        toast.show()
    }

    companion object {
        const val defaultUserRole: String = "Patient"
        const val UserKey: String = "UserKey"
        const val UserPhone: String = "Phone"
    }
}