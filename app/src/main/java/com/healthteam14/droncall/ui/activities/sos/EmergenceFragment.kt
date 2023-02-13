package com.healthteam14.droncall.ui.activities.sos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.healthteam14.droncall.R
import com.healthteam14.droncall.models.User
import com.healthteam14.droncall.ui.activities.SecondRegStepActivity
import kotlinx.android.synthetic.main.fragment_emergence.*

class EmergenceFragment : Fragment() {

    lateinit var callerImage:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_emergence, container, false)
        callerImage = view.findViewById(R.id.imageView3)

        callerImage.setOnClickListener {
            call()
        }
        return view
    }

    private fun call() {

        if(edtPhoneCall.text !=null && edtPhoneCall.text.isNotEmpty()){
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + edtPhoneCall.text.toString())
            startActivity(dialIntent)
        }else{
            edtPhoneCall.error = "Number is required"
            showMsg("Phone number is empty")
        }

    }

    private fun showMsg(msg: String) {
        val toast = Toast.makeText(activity, msg, Toast.LENGTH_LONG)
        toast.show()
    }

}