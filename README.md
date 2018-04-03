# ONE Store InApp Purchase V5

## Setup guide for IAP Sample Application

If you want to use this application to test the onestore IAP, you must do the following:

1. Rename package name (applicationId in gradle)

    * The default package name ('com.onestore.iap.sample') is not available. If you're using it, change it to something else.


2. Add your product name to the development center as described in the guide below.

    * Guide : https://dev.onestore.co.kr/devpoc/reference/view/IAP_v17_04_preparation (Tab 1)


3. Add the IAP products to the development center.

    * Use the "AppConstant.java" info to add product IDs of IAP to the development site.

    * Guide : https://dev.onestore.co.kr/devpoc/reference/view/IAP_v17_04_preparation (Tab 3-1)


4. Check the public key on the development center and add a public key in the the app(jni/public_keys.c).

    * Guide : https://dev.onestore.co.kr/devpoc/reference/view/IAP_v17_04_preparation (Tab 4)


5.  Use the guide below to change your account to SENDBOX mode.

    * guide : https://dev.onestore.co.kr/devpoc/reference/view/IAP_v17_06_test 
