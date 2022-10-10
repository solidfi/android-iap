/*
 * ********************************************************************
 *   Copyright @2021 Solidfi. Company
 *   You may obtain a copy of the License at https://www.solidfi.com
 *  *********************************************************************
 *
 */

package com.solid.android.cardpushprovision

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tapandpay.TapAndPay
import com.google.android.gms.tapandpay.TapAndPayClient
import com.google.android.gms.tapandpay.TapAndPayStatusCodes
import com.google.android.gms.tapandpay.issuer.IsTokenizedRequest
import com.google.android.gms.tapandpay.issuer.PushTokenizeRequest
import com.google.android.gms.tapandpay.issuer.UserAddress
import com.google.android.gms.tasks.Task

private const val ISSUERNAME = "Solid App"
const val REQUEST_CODE_PUSH_TOKENIZE: Int = 3
class GpayProvisionMngr constructor( private val activity: AppCompatActivity){

    private var tapAndPayClient: TapAndPayClient = TapAndPay.getClient(activity)
    private var clientCustomerId: String? = null
    private var deviceId: String? = null
    private var cardNeedVerification = false
    private var cardTokenReferenceId = ""

    init {
        TapAndPay.getClient(activity)?.activeWalletId?.addOnCompleteListener {
            if (it.isSuccessful) {
                clientCustomerId = it.result
            } else {
                val error = it.exception?.localizedMessage
                Toast.makeText(activity,"Unable to get google pay wallet id.".plus("  ").plus(error),Toast.LENGTH_LONG).show()
            }
        }
        tapAndPayClient?.stableHardwareId?.addOnCompleteListener{
            if (it.isSuccessful) {
                deviceId = it.result
            }
        }
    }

    /**
     * check the Gpay environment - Production or sandbox
     */
    fun checkforGpayEnvironment(){
        tapAndPayClient?.environment?.addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(activity,"Gpay-Env - ".plus(it.result),Toast.LENGTH_LONG).show()
            }
        }
    }

    fun isCardProvisionStarted():Boolean{
        return cardNeedVerification
    }
    /**
     * Check the card is already added to Gpay ,
     * Provide the last 4 digit of the card number and callback method which will respond with true or false
     */
    fun shouldEnableAddToWalletButton(cardLast4 :String,callBack: (showAddButton: Boolean?) -> Unit){
        val request = IsTokenizedRequest.Builder()
            .setIdentifier(cardLast4)
            .setNetwork(TapAndPay.CARD_NETWORK_VISA)
            .setTokenServiceProvider(TapAndPay.TOKEN_PROVIDER_VISA)
            .build()
        tapAndPayClient.isTokenized(request)
            .addOnCompleteListener { task: Task<Boolean?> ->
                if (task.isSuccessful) {
                    callBack(task.result)
                    checkTokenStatus(cardLast4,callBack)
                    //Handle the visibility of button with task.getResult();
                }
            }
    }

    private fun checkTokenStatus(lastFour :String,callBack: (showAddButton: Boolean?) -> Unit){
        tapAndPayClient?.listTokens()?.addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                for (token in task.result) {
                    if (token.fpanLastFour.equals(lastFour, true) && ISSUERNAME.equals(token.issuerName, true)) {
                        cardNeedVerification = (token.tokenState == TapAndPay.TOKEN_STATE_NEEDS_IDENTITY_VERIFICATION)
                        cardTokenReferenceId = token.issuerTokenId
                        callBack(cardNeedVerification)
                    }
                }
            }else{
                Toast.makeText(activity,activity.getString(R.string.gpay_token_status),Toast.LENGTH_LONG).show()
            }
        }
    }//End


    /**
     * Provide the OPC bytes reciced from server and Card Details object
     * This method will initiate Card provisioning call to Gpay via TAP and PAY library
     */
    fun handleAddToGooglePayClick(opaquePaymentCard: String,
                                  cardHolderName :String,
                                  addressline1 :String,
                                  city :String,
                                  state:String,
                                  country:String,
                                  postalcode:String,
                                  mobileNumb:String,
                                  cardLabel:String,
                                  cardLast4:String) {

        val opcBytes: ByteArray = opaquePaymentCard?.toByteArray()!!

        val userAddress = UserAddress.newBuilder()
            .setName(cardHolderName)
            .setAddress1(addressline1)
            .setLocality(city)
            .setAdministrativeArea(state)
            .setCountryCode(country)
            .setPostalCode(postalcode)
            .setPhoneNumber(mobileNumb)
            .build()

        val pushTokenizeRequest: PushTokenizeRequest = PushTokenizeRequest.Builder()
            .setOpaquePaymentCard(opcBytes)
            .setNetwork(TapAndPay.CARD_NETWORK_VISA)
            .setTokenServiceProvider(TapAndPay.TOKEN_PROVIDER_VISA)
            .setDisplayName(cardLabel)
            .setLastDigits(cardLast4)
            .setUserAddress(userAddress)
            .build();

        tapAndPayClient?.pushTokenize(activity, pushTokenizeRequest, REQUEST_CODE_PUSH_TOKENIZE)
    }

    fun resumeCardPushProvisioning(cardLabel:String,){
        tapAndPayClient?.tokenize(
            activity, cardTokenReferenceId, TapAndPay.TOKEN_PROVIDER_VISA,
            cardLabel, TapAndPay.CARD_NETWORK_VISA, REQUEST_CODE_PUSH_TOKENIZE
        )
    }

    /**
     * This methond will validate the staus of Push To Tokensize call,
     * After handleAddToGooglePayClick call, OnActivity result will receive the call back with Error and success status.
     */
    fun handleTokenizationResult(resultCode: Int, data: Intent?,callBack: (message: String) -> Unit) {
        when (resultCode) {
            TapAndPayStatusCodes.TAP_AND_PAY_NO_ACTIVE_WALLET -> {
                callBack( "TAP_AND_PAY_NO_ACTIVE_WALLET - 15002")
            }
            TapAndPayStatusCodes.TAP_AND_PAY_TOKEN_NOT_FOUND -> {
                callBack( "TAP_AND_PAY_TOKEN_NOT_FOUND - 15003")
            }
            TapAndPayStatusCodes.TAP_AND_PAY_INVALID_TOKEN_STATE -> {
                callBack( "TAP_AND_PAY_INVALID_TOKEN_STATE - 15004")
            }
            TapAndPayStatusCodes.TAP_AND_PAY_ATTESTATION_ERROR -> {
                callBack( "TAP_AND_PAY_ATTESTATION_ERROR - 15005")
            }
            TapAndPayStatusCodes.TAP_AND_PAY_UNAVAILABLE -> {
                callBack( "TAP_AND_PAY_UNAVAILABLE - 15009")
            }
            TapAndPayStatusCodes.TAP_AND_PAY_SAVE_CARD_ERROR -> {
                callBack( "TAP_AND_PAY_SAVE_CARD_ERROR - 15019")
            }
            TapAndPayStatusCodes.TAP_AND_PAY_INELIGIBLE_FOR_TOKENIZATION -> {
                callBack( "TAP_AND_PAY_INELIGIBLE_FOR_TOKENIZATION - 15021")
            }
            TapAndPayStatusCodes.TAP_AND_PAY_TOKENIZATION_DECLINED -> {
                callBack( "TAP_AND_PAY_TOKENIZATION_DECLINED - 15022")
            }
            TapAndPayStatusCodes.TAP_AND_PAY_CHECK_ELIGIBILITY_ERROR -> {
                callBack("TAP_AND_PAY_CHECK_ELIGIBILITY_ERROR - 15023")
            }
            TapAndPayStatusCodes.TAP_AND_PAY_TOKENIZE_ERROR -> {
                callBack( "TAP_AND_PAY_TOKENIZE_ERROR - 15024")
            }
            TapAndPayStatusCodes.TAP_AND_PAY_TOKEN_ACTIVATION_REQUIRED -> {
                callBack("TAP_AND_PAY_TOKEN_ACTIVATION_REQUIRED - 15025")
            }
            AppCompatActivity.RESULT_OK -> {
                callBack( "Card link success")
            }
            AppCompatActivity.RESULT_CANCELED -> {
                callBack( "Card link canceled")
            }
        }
    }
}