package com.solid.android.cardpushprovision

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.solid.android.cardpushprovision.data.CardResponse

class MainActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var addToGooglePayButton: RelativeLayout
    private lateinit var gpayProvisionMngr :GpayProvisionMngr
    private val REQUEST_CODE_PUSH_TOKENIZE: Int = 0X32121

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addToGooglePayButton = findViewById(R.id.add_to_google_pay_button)
        addToGooglePayButton.setOnClickListener(this)
        gpayProvisionMngr = GpayProvisionMngr(this)
        getOpcForCard()
        gpayProvisionMngr.shouldEnableAddToWalletButton("LAST-4-DIGIT-OF-CARD NUMBER"){
            addToGooglePayButton.visibility = if(it)  View.GONE else View.VISIBLE
        }
    }

    /**
     * call to Solid api for geting OPC bytes from server for the card
     * https://documenter.getpostman.com/view/13543869/TWDfEDwX#ce8c0e57-0dcf-45ea-87d8-6f03a302e027
     */
    private fun getOpcForCard() {
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
        val card = CardResponse()//This should receive from Server or Provide valid information
        gpayProvisionMngr.handleAddToGooglePayClick("OPC-SRINIG-FROM-SOLID-BACK-END",card)
    }
}