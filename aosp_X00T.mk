#
# Copyright (C) 2020 The LineageOS Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/product_launched_with_o_mr1.mk)

# Inherit some common PixelReloaded stuff
$(call inherit-product, vendor/aosp/config/common_full_phone.mk)

# Inherit from X00T device
$(call inherit-product, $(LOCAL_PATH)/device.mk)

# Bootanimation
TARGET_BOOT_ANIMATION_RES := 1080

# Gapps Config
TARGET_GAPPS_ARCH := arm64

PRODUCT_BRAND := ASUS
PRODUCT_DEVICE := X00T
PRODUCT_MANUFACTURER := ASUS
PRODUCT_NAME := aosp_X00T
PRODUCT_MODEL := Zenfone Max Pro M1

PRODUCT_GMS_CLIENTID_BASE := android-asus

TARGET_VENDOR := asus
TARGET_VENDOR_PRODUCT_NAME := X00T
PRODUCT_BUILD_PROP_OVERRIDES += PRIVATE_BUILD_DESC="X00T-user 11 RQ3A.210905.001 7511028 release-keys"

# Set BUILD_FINGERPRINT variable to be picked up by both system and vendor build.prop
BUILD_FINGERPRINT := asus/X00T/X00T:11/RQ3A.210905.001/7511028:user/release-keys
