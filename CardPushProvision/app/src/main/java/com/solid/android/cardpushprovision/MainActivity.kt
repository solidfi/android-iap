package com.solid.android.cardpushprovision

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var addToGooglePayButton: RelativeLayout
    private lateinit var gpayProvisionMngr :GpayProvisionMngr

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addToGooglePayButton = findViewById(R.id.add_to_google_pay_button)
        addToGooglePayButton.setOnClickListener(this)
        gpayProvisionMngr = GpayProvisionMngr(this)
        gpayProvisionMngr.shouldEnableAddToWalletButton("LAST-4-DIGIT-OF-CARD NUMBER"){ alredyAdded ->
            alredyAdded?.let {
                addToGooglePayButton.visibility = if(it)  View.GONE else View.VISIBLE
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PUSH_TOKENIZE) {
            gpayProvisionMngr.handleTokenizationResult(resultCode, data){
                Toast.makeText(this,it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(p0: View?) {
        gpayProvisionMngr.handleAddToGooglePayClick(
            "OPC-SRINIG-FROM-SOLID-BACK-END", //Provide the OPC bytes received from Solid server api call
            "cardHolderName" ,//Provide real card holder name
            "addressline1" ,// Adress line of billing adress
            "city" ,//Provide city of billing adress
            "state",//Provide state of billing adress
            "country",//Provide country of billing adress
            "postalcode",//Provide postal code of billing adress
            "mobileNumb",//Provide mobile number of card holder
            "cardLabel",//Provide real card label
            "cardLast4")//Provide real card last 4 digit
    }
}