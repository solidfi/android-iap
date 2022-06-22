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
        getOpcForCard()
        gpayProvisionMngr.shouldEnableAddToWalletButton("LAST-4-DIGIT-OF-CARD NUMBER"){ alredyAdded ->
            alredyAdded?.let {
                addToGooglePayButton.visibility = if(it)  View.GONE else View.VISIBLE
            }
        }
    }

    /**
     * call to Solid api for geting OPC bytes from server for the card
     * https://documenter.getpostman.com/view/13543869/TWDfEDwX#ce8c0e57-0dcf-45ea-87d8-6f03a302e027
     */
    private fun getOpcForCard() {//TODO implement the API Call to Solid server
        //Once the card OPC received from server
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
            "OPC-SRINIG-FROM-SOLID-BACK-END", //TODO provide the OPC bytes received from Solid server api call
            "cardHolderName" ,//TODO provide real card holder name
            "addressline1" ,//TODO adress line of billing adress
            "city" ,//TODO provide city of billing adress
            "state",//TODO provide state of billing adress
            "country",//TODO provide country of billing adress
            "postalcode",//TODO provide postal code of billing adress
            "mobileNumb",//TODO provide mobile number of card holder
            "cardLabel",//TODO provide real card label
            "cardLast4")//TODO provide real card last 4 digit
    }
}