/*
 * ********************************************************************
 *   Copyright @2021 Solidfi. Company
 *   You may obtain a copy of the License at https://www.solidfi.com
 *  *********************************************************************
 *
 */

package com.solid.android.cardpushprovision.data

data class CardHolderResponse(
    var id: String? = null,
    var personId: String? = null,
    var billingAddress: AddressResponse? = null,
    var createdAt: String? = null,
    var modifiedAt: String? = null,
    var name: String? = null
)