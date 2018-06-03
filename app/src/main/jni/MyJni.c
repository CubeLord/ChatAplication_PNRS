//
// Created by Milorad Markovic on 03-Jun-18.
//

#include <jni.h>
#include "MyJni.h"

JNIEXPORT jstring JNICALL Java_markovic_milorad_chataplication_NdkPackage_MyNDK_encryptString
  (JNIEnv * env, jobject obj, jstring str, jstring key) {
    return str;
  }

JNIEXPORT jstring JNICALL Java_markovic_milorad_chataplication_NdkPackage_MyNDK_decryptString
  (JNIEnv * env, jobject obj, jstring str, jstring key) {
    return str;
  }
