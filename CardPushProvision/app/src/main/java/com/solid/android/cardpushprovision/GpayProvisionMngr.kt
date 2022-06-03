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
import com.google.android.gms.tapandpay.issuer.PushTokenizeRequest
import com.google.android.gms.tapandpay.issuer.UserAddress
import com.solid.android.cardpushprovision.data.CardResponse

class GpayProvisionMngr constructor( private val activity: AppCompatActivity){

    private val REQUEST_CODE_PUSH_TOKENIZE: Int = 3
    private var tapAndPayClient: TapAndPayClient = TapAndPay.getClient(activity)
    private var walletId: String? = null
    private var stableHardwareId: String? = null

    init {
        tapAndPayClient?.activeWalletId?.addOnCompleteListener {
            if (it.isSuccessful) {
                walletId = it.result
            } else {
                val error = it.exception?.localizedMessage
                Toast.makeText(activity,"Unable to get google pay wallet id.".plus("  ").plus(error),Toast.LENGTH_LONG).show()
            }
        }
        tapAndPayClient?.stableHardwareId?.addOnCompleteListener{
            if (it.isSuccessful) {
                stableHardwareId = it.result
            }
        }
    }

    fun checkforGpayEnvironment(){
        tapAndPayClient?.environment?.addOnCompleteListener{
            if(it.isSuccessful){
               Toast.makeText(activity,"Gpay-Env - ".plus(it.result),Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Check the card is already added to Gpay ,
     * Provide the last 4 digit of the card number and callback method which will repond with true or false
     */
    fun shouldEnableAddToWalletButton(cardLast4 :String,callBack: (showAddButton: Boolean) -> Unit){
        tapAndPayClient?.listTokens()?.addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                var alreadyAdded = false
                task.result?.forEach { tokeninfo ->
                    if(cardLast4.equals(tokeninfo.fpanLastFour?.trim())){
                        alreadyAdded = true
                        return@forEach
                    }
                }//end
                callBack(alreadyAdded)
            }
        }
    }

    /**
     * Provide the OPC bytes reciced from server and Card Details object
     * This method will initiate Card provisioning call to Gpay via TAP and PAY library
     */
    fun handleAddToGooglePayClick(opaquePaymentCard: String,cardResponse: CardResponse) {
        val opcBytes: ByteArray = opaquePaymentCard?.toByteArray()!!

        val userAddress: UserAddress = UserAddress.newBuilder()
            .setName(cardResponse?.cardholder?.name!!)
            .setAddress1(cardResponse?.cardholder?.billingAddress?.line1!!)
            .setLocality(cardResponse?.cardholder?.billingAddress?.city!!)
            .setAdministrativeArea(cardResponse?.cardholder?.billingAddress?.state!!)
            .setCountryCode(cardResponse?.cardholder?.billingAddress?.country!!)
            .setPostalCode(cardResponse?.cardholder?.billingAddress?.postalCode!!)
            .setPhoneNumber(cardResponse.mobile!!)
            .build()

        val pushTokenizeRequest: PushTokenizeRequest = PushTokenizeRequest.Builder()
            .setOpaquePaymentCard(opcBytes)
            .setNetwork(TapAndPay.CARD_NETWORK_VISA)
            .setTokenServiceProvider(TapAndPay.TOKEN_PROVIDER_VISA)
            .setDisplayName(cardResponse?.label!!)
            .setLastDigits(cardResponse?.last4!!)
            .setUserAddress(userAddress)
            .build()

        tapAndPayClient?.pushTokenize(activity, pushTokenizeRequest, REQUEST_CODE_PUSH_TOKENIZE)
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