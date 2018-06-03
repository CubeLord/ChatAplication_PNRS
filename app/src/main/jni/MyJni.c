//
// Created by Milorad Markovic on 03-Jun-18.
//

#include <jni.h>
#include "MyJni.h"

JNIEXPORT jstring JNICALL Java_markovic_milorad_chataplication_NdkPackage_MyNDK_encryptString
  (JNIEnv * env, jobject obj, jstring str, jstring key) {
    const char *charStr = (*env)->GetStringUTFChars(env, str, JNI_FALSE);
    const char *charKey = (*env)->GetStringUTFChars(env, key, JNI_FALSE);
    int i = 0;
    int Strlen = (*env)->GetStringLength(env, str);
    int Keylen = (*env)->GetStringLength(env, key);

    char encrypted[Strlen + 1];

    for(i =0; i<Strlen; i++) {
        encrypted[i] = charStr[i] ^ charKey[i % Keylen];
    }
    encrypted[Strlen] = '\0';

    (*env)->ReleaseStringUTFChars(env, str, charStr);
    (*env)->ReleaseStringUTFChars(env, key, charKey);

    jstring retVal = (*env)->NewStringUTF(env, encrypted);
    return retVal;
  }

JNIEXPORT jstring JNICALL Java_markovic_milorad_chataplication_NdkPackage_MyNDK_decryptString
  (JNIEnv * env, jobject obj, jstring str, jstring key) {
    return str;
  }
