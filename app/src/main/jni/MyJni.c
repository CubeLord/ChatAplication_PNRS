//
// Created by Milorad Markovic on 03-Jun-18.
//

#include <jni.h>
#include "MyJni.h"

JNIEXPORT jstring JNICALL Java_markovic_milorad_chataplication_NdkPackage_MyNDK_getMyString
  (JNIEnv * env, jobject thisObj) {
    return (*env)->NewStringUTF(env, "Hello world ...");
  }
