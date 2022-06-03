

package com.solid.android.cardpushprovision.data

import com.solid.android.cardpushprovision.data.CardHolderResponse

data class CardResponse(
    var id: String? = null,
    var mobile: String? = null,
    var accountId: String? = null,
    var businessId: String? = null,
    var programId: String? = null,
    var cardholder: CardHolderResponse? = null,
    var label: String? = null,
    var limitAmount: String? = null,
    var currency: String? = null,
    var last4: String? = null,
    var activatedAt: String? = null,
    var createdAt: String? = null,
    var modifiedAt: String? = null,
    var cardNumberAlias: String? = null,
    var cvcAlias: String? = null,
    var cvv: String? = null,
    var cardNumber: String? = null,
    var expiryMonth: String? = null,
    var expiryYear: String? = null,
    var showToken: String? = null,
    var embossingPerson: String? = null,
    var embossingBusiness: String? = null,
    var bin : String? = null
)
