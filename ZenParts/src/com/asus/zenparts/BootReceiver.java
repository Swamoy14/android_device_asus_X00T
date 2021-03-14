/*
 * Copyright (C) 2018 The Asus-SDM660 Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.asus.zenparts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.asus.zenparts.kcal.KcalConstants;
import com.asus.zenparts.preferences.vibration.VibrationConstants;
import com.asus.zenparts.preferences.vibration.VibrationUtils;
import com.asus.zenparts.settings.ScreenOffGesture;

public class BootReceiver extends BroadcastReceiver implements KcalConstants {

    private VibrationUtils vibrationUtils;
    private Context mContext;
    private SharedPreferences sharedPrefs;

    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
            return;

        Log.v("ZenParts: BootReceiver", "Called");

        mContext = context;
        vibrationUtils = new VibrationUtils(context);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        // Restore all preferences
        restoreGestureControl();
        restoreVibrationStrength();
        restoreKCal();
        restoreMiscPrefs();
    }

    private void restoreGestureControl() {
        GestureNodeControl.enableGestures(
                sharedPrefs.getBoolean(ScreenOffGesture.PREF_GESTURE_ENABLE, true));
    }

    private void restoreVibrationStrength() {
        for (int i = 0; i <= VibrationConstants.vibrationPaths.length - 1; i++) {
            vibrationUtils.restore(VibrationConstants.vibrationPaths[i], VibrationConstants.vibrationKeys[i]);
        }
    }

    private void restoreKCal() {
        if (Settings.Secure.getInt(mContext.getContentResolver(), PREF_ENABLED, 0) == 1) {
            FileUtils.setValue(KCAL_ENABLE, Settings.Secure.getInt(mContext.getContentResolver(),
                    PREF_ENABLED, 0));

            String rgbValue = Settings.Secure.getInt(mContext.getContentResolver(),
                    PREF_RED, RED_DEFAULT) + " " +
                    Settings.Secure.getInt(mContext.getContentResolver(), PREF_GREEN,
                            GREEN_DEFAULT) + " " +
                    Settings.Secure.getInt(mContext.getContentResolver(), PREF_BLUE,
                            BLUE_DEFAULT);

            FileUtils.setValue(KCAL_RGB, rgbValue);
            FileUtils.setValue(KCAL_MIN, Settings.Secure.getInt(mContext.getContentResolver(),
                    PREF_MINIMUM, MINIMUM_DEFAULT));
            FileUtils.setValue(KCAL_SAT, Settings.Secure.getInt(mContext.getContentResolver(),
                    PREF_GRAYSCALE, 0) == 1 ? 128 :
                    Settings.Secure.getInt(mContext.getContentResolver(),
                            PREF_SATURATION, SATURATION_DEFAULT) + SATURATION_OFFSET);
            FileUtils.setValue(KCAL_VAL, Settings.Secure.getInt(mContext.getContentResolver(),
                    PREF_VALUE, VALUE_DEFAULT) + VALUE_OFFSET);
            FileUtils.setValue(KCAL_CONT, Settings.Secure.getInt(mContext.getContentResolver(),
                    PREF_CONTRAST, CONTRAST_DEFAULT) + CONTRAST_OFFSET);
            FileUtils.setValue(KCAL_HUE, Settings.Secure.getInt(mContext.getContentResolver(),
                    PREF_HUE, HUE_DEFAULT));
        }
    }

    private void restoreMiscPrefs() {
        // Paths
        String TORCH_1_BRIGHTNESS_PATH = "/sys/devices/soc/800f000.qcom,spmi/spmi-0/" +
                "spmi0-03/800f000.qcom,spmi:qcom,pm660l@3:qcom,leds@d300/leds/led:torch_0/" +
                "max_brightness";
        String TORCH_2_BRIGHTNESS_PATH = "/sys/devices/soc/800f000.qcom,spmi/spmi-0/" +
                "spmi0-03/800f000.qcom,spmi:qcom,pm660l@3:qcom,leds@d300/leds/led:torch_1/" +
                "max_brightness";
        String HEADPHONE_GAIN_PATH = "/sys/kernel/sound_control/headphone_gain";
        String MICROPHONE_GAIN_PATH = "/sys/kernel/sound_control/mic_gain";

        // Restore preferences
        FileUtils.setValue(TORCH_1_BRIGHTNESS_PATH,
                Settings.Secure.getInt(mContext.getContentResolver(),
                        DeviceSettings.PREF_TORCH_BRIGHTNESS, 100));
        FileUtils.setValue(TORCH_2_BRIGHTNESS_PATH,
                Settings.Secure.getInt(mContext.getContentResolver(),
                        DeviceSettings.PREF_TORCH_BRIGHTNESS, 100));
        int gain = Settings.Secure.getInt(mContext.getContentResolver(),
                DeviceSettings.PREF_HEADPHONE_GAIN, 0);
        FileUtils.setValue(HEADPHONE_GAIN_PATH, gain + " " + gain);
        FileUtils.setValue(MICROPHONE_GAIN_PATH, Settings.Secure.getInt(mContext.getContentResolver(),
                DeviceSettings.PREF_MICROPHONE_GAIN, 0));
        FileUtils.setValue(DeviceSettings.BACKLIGHT_DIMMER_PATH, Settings.Secure.getInt(mContext.getContentResolver(),
                DeviceSettings.PREF_BACKLIGHT_DIMMER, 0));

        boolean enabled = sharedPrefs.getBoolean(DeviceSettings.PREF_KEY_FPS_INFO, false);
        if (enabled)
            mContext.startService(new Intent(mContext, FPSInfoService.class));
    }
}
