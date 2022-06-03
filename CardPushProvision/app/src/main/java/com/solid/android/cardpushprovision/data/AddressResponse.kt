/*
 * ********************************************************************
 *   Copyright @2021 Solidfi. Company
 *   You may obtain a copy of the License at https://www.solidfi.com
 *  *********************************************************************
 *
 */

package com.solid.android.cardpushprovision.data

data class AddressResponse(
    var addressType: String?, var line1: String?, var line2: String?, var city: String?,
    var state: String?, var country: String?, var postalCode: String?,
)