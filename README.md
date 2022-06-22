# android-iap
Android In App Push Provisioning Sample

## Table of contents
- [Overview](#overview)
- [Getting started](#getting-started)
- [Prerequisite](#prerequisite)
- [Working With Sample](#working-with-sample)
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

### Get Access to Android Push Provisioning API from Google

- To get started with Google Pay push provisioning, submit this request form: https://support.google.com/faqs/contact/pp_api_allowlist?authuser=1
- Application Package name and SHA-256 certificate fingerprint used for Application Signature should be added to Google allow lis, while submit the  Request form please provide these informations.
- To enable OPC debugging access, submit request here https://support.google.com/faqs/contact/pp_api_general
- For OPC Debugging Access provide a gmail account adress which should be configured in Gpay application
- Working With Gpaysandbox mode refer google document for - https://developers.google.com/pay/issuers/resources/sandbox 
- More information follow the steps from Google - https://developers.google.com/pay/issuers/apis/push-provisioning/android


## Working With Sample
- Open settings.gradle file from the cloned repo
- Update the maven file path of Google Tap and pay lib 
 ```maven { url "file:/UR_REPO_PATH/android-iap/CardPushProvision/m2repository/" }```
 latest Libray can be download from https://developers.google.com/pay/issuers/apis/push-provisioning/android/setup
 follow the steps described in developer docs 
 - Open GpayProvisionMngr.kt file includes Push provisioning API required for the Android client.
 - Refer MainActivity.kt file for the Usage of GpayProvisionMngr Class

## Tooling
- Android 5.0 +
- Android Studio Chipmunk | 2021.2.1
- Kotlin 1.6.21

## Solid Dev Center
- [Solid Dev Center](https://www.solidfi.com/docs/introduction)

