# android-iap
Android In App Push Provisioning Sample

## Table of contents
- [Overview](#overview)
- [Getting started](#getting-started)
- [Prerequisite](#prerequisite)
- [Working With Sample](#working-with-sample)
- [API details for running the Sample](#api details for running the Sample)
- [Tooling](#tooling)
- [ Solid Dev center](#solid-dev-center)



## Overview
Google Pay In App Provisioning is the way to provision your Cards to Google Pay Wallet.

## Getting started

- Clone repository
```groovy
git clone git@github.com:solidfi/android-iap.git
OR
git clone https://github.com/solidfi/android-iap.git
```
- Open it in [Android Studio](https://developer.android.com/studio)
- Run the project and test it out.

## Prerequisite

- To get started, complete the steps as mentioed in : [Google Pay Entitlements](https://help.solidfi.com/hc/en-us/articles/6343456222875) 
- Application Package name and SHA-256 certificate fingerprint used for Application Signature should be added to Google allow lis, while submit the  Request form please provide these informations.
- To enable OPC debugging access, submit request here https://support.google.com/faqs/contact/pp_api_general
- For OPC Debugging Access provide a gmail account adress which should be configured in Gpay application
- Working With Gpaysandbox mode refer google document for - https://developers.google.com/pay/issuers/resources/sandbox 
- More information follow the steps from Google - https://developers.google.com/pay/issuers/apis/push-provisioning/android


## Working With Sample
- Easy to import the project to android studio to Run the sample
- Open GpayProvisionMngr.kt file includes Push provisioning API required for the Android client.
- Refer MainActivity.kt file for the Usage of GpayProvisionMngr Class

- If you facing issues on gradle sync or compilation, please follow below steps
- Open settings.gradle file from the cloned repo
- Update the maven file path of Google Tap and pay lib
  ``` maven { url "file:$rootDir/../CardPushProvision/m2repository" }```
  latest Libray can be download from https://developers.google.com/pay/issuers/apis/push-provisioning/android/setup
  follow the steps described in developer docs

## API details for running the Sample
This sample only describes how to communicate with GooglePay Sdk Api for push Provisioning,
So implementation team has to take care of the following back end api implementations

According to google pay documentation, we need to have the following fields to invoke provision API call to GPay
```
val pushTokenizeRequest: PushTokenizeRequest = PushTokenizeRequest.Builder()
   .setOpaquePaymentCard(opcBytes)
   .setNetwork(TapAndPay.CARD_NETWORK_VISA)
    .setTokenServiceProvider(TapAndPay.TOKEN_PROVIDER_VISA)
    .setDisplayName(cardLabel)
   .setLastDigits(cardLast4)
    .setUserAddress(userAddress)
   .build();
```
- Call Card Api from solid platform to get Card information.
- Call /provision Api available in Solid platform to get `opcBytes(opaquePaymentCard)`
- /provision api required `clientCustomerId and deviceId ` as input parameters, these argument values already available inside the sample repo ,
  file named `GpayProvisionMngr.kt`
- Response received from provision api  from solid platform use the value `opaquePaymentCard` as input to above `PushTokenizeRequest` 
 

## Tooling
- Android 5.0 +
- Android Studio Chipmunk | 2021.2.1 Patch 1
- Kotlin 1.6.21
- Compile android SDK - API 31

## Solid Dev Center
- [Solid Dev Center](https://www.solidfi.com/docs/introduction)
