#include <jni.h>

JNIEXPORT jstring JNICALL
 Java_com_onestore_iap_sample_security_AppSecurity_getPublicKey(JNIEnv *env, jobject instance)
 {
 return (*env)->NewStringUTF(env, "BASE_64_PUBLIC_KEY");
}

