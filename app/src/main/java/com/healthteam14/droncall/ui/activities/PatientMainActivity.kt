package com.healthteam14.droncall.ui.activities

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.healthteam14.droncall.R
import com.healthteam14.droncall.databinding.ActivityPatientMainBinding
import com.healthteam14.droncall.models.User
import com.healthteam14.droncall.utils.FirebaseUtils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class PatientMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPatientMainBinding
    private lateinit var firstname: String
    private lateinit var disease: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPatientMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarPatientMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_patient_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration =
            AppBarConfiguration(
                setOf(R.id.nav_home, R.id.nav_sos, R.id.nav_hosp, R.id.nav_about),
                drawerLayout
            )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        getUserDetails()

        btn_disease.setOnClickListener {

            val symptom1: String = spSymptom1.selectedItem.toString()
            val symptom2: String = spSymptom2.selectedItem.toString()
            val symptom3: String = spSymptom3.selectedItem.toString()
            val symptom4: String = spSymptom4.selectedItem.toString()
            val symptom5: String = spSymptom5.selectedItem.toString()
            val symptom6: String = spSymptom6.selectedItem.toString()
            val symptom7: String = edSymptom7?.text.toString()

            if (symptom1.contains("Vomiting") || symptom2.contains("Vomiting") || symptom3.contains(
                    "Vomiting"
                )
            ) {
                disease = "Gastroenteritis"
                showDialog(disease)

            } else if (symptom1.contains("Sweating") || symptom2.contains("Sweating") || symptom3.contains(
                    "Sweating"
                )
            ) {

                disease = "Hyperhidrosis (hi-pur-hi-DROE-sis)"

                showDialog(disease)

            } else if (symptom1.contains("Headaches") || symptom2.contains("Headaches") || symptom3.contains(
                    "Headaches"
                )
            ) {

                disease = "TTH is the most common primary headache disorder"

                showDialog(disease)
            } else if (symptom1.contains("Abdominal Pain") || symptom2.contains("Abdominal Pain") || symptom3.contains(
                    "Abdominal Pain"
                )
            ) {
                disease = "Functional dyspepsia. Gallstones"

                showDialog(disease)
            } else if (symptom1.contains("Chest Pain") || symptom2.contains("Chest Pain") || symptom3.contains(
                    "Chest Pain"
                )
            ) {

                disease = "Angina"
                showDialog(disease)

            } else if (symptom1.contains("Anxiety") || symptom2.contains("Anxiety") || symptom3.contains(
                    "Anxiety"
                )
            ) {

                disease = " Obsessive-Compulsive Disorder (OCD)\n" +
                        " Panic Disorder \n" +
                        " Post-Traumatic Stress Disorder (PTSD) \n" +
                        " Social Phobia (or Social Anxiety Disorder)"

                showDialog(disease)

            } else if (symptom1.contains("Thirsty Than Usual") || symptom2.contains("Thirsty Than Usual") || symptom3.contains(
                    "Thirsty Than Usual"
                )
            ) {

                disease = "diabetes"

                showDialog(disease)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.patient_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_signOut -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Please confirm")
                    setMessage("Are you sure you want to Sign out?")

                    setPositiveButton("Yes") { _, _ ->
                        signOut()
                    }

                    setNegativeButton("No") { _, _ ->
                    }
                    setCancelable(true)

                }.create().show()
                true
            }
            R.id.action_share -> {
                showSharingDialog(
                    "Download",
                    "https://drive.google.com/file/d/182NQaSD8ULmrIYSRD8VGz1e73-Va1VNT/view?usp=sharing"
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_patient_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getUserDetails() {

        var userKey: String = FirebaseUtils.firebaseUser?.uid.toString()
        val myRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child(userKey)

        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)

                if (user == null) {
                    firstname = ""
                    return
                } else {
                    firstname = user.firstName.toString()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                //Failed to read value
            }
        })

    }

    private fun showSharingDialog(text: String, url: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "$text: $url")
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, FirebaseAuthUIActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showDialog(disease: String) {

        val dialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.show_disease_dialog, null)
        val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)

        val showDisease = view.findViewById<TextView>(R.id.tvDisease)
        val showMoreInfoDisease = view.findViewById<TextView>(R.id.tvDiseaseMoreinfo)

        if (this::firstname.isInitialized) {
            showDisease.text = "Hi $firstname, you are suffering from $disease , "
        } else {
            showDisease.text = "Hi, you are suffering from $disease , "
        }

        //Open google with
        showMoreInfoDisease.setOnClickListener {
            openBrowserIntent()
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)

        dialog.setContentView(view)

        dialog.show()
    }

    private fun openBrowserIntent() {
        val intent = Intent(Intent.ACTION_WEB_SEARCH)
        intent.putExtra(SearchManager.QUERY, disease)
        startActivity(intent)
    }

    private fun showMsg(msg: String) {
        val toast = Toast.makeText(this@PatientMainActivity, msg, Toast.LENGTH_LONG)
        toast.show()
    }
}