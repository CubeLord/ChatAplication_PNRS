LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := libmyJni
LOCAL_SRC_FILES := MyJni.c
include $(BUILD_SHARED_LIBRARY)