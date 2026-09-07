package com.dareewss.myapplication.data

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun isSetup(): Boolean = prefs.getBoolean(KEY_SET, false)

    fun loadProfile(): UserProfile? {
        if (!isSetup()) return null
        val fat = prefs.getFloat(KEY_BODY_FAT, -1f)
        return UserProfile(
            isMale = prefs.getBoolean(KEY_MALE, true),
            age = prefs.getInt(KEY_AGE, 25),
            weightKg = prefs.getFloat(KEY_WEIGHT, 0f),
            heightCm = prefs.getFloat(KEY_HEIGHT, 0f),
            sleepHours = prefs.getInt(KEY_SLEEP, 8),
            activity = ActivityLevel.fromMultiplier(prefs.getFloat(KEY_ACTIVE, 1.2f)),
            bodyFatPercent = fat.takeIf { it > 0f }
        )
    }

    fun saveProfile(profile: UserProfile) {
        val editor = prefs.edit()
            .putBoolean(KEY_MALE, profile.isMale)
            .putInt(KEY_AGE, profile.age)
            .putFloat(KEY_WEIGHT, profile.weightKg)
            .putFloat(KEY_HEIGHT, profile.heightCm)
            .putInt(KEY_SLEEP, profile.sleepHours)
            .putFloat(KEY_ACTIVE, profile.activity.multiplier)
            .putBoolean(KEY_SET, true)
        if (profile.bodyFatPercent != null) {
            editor.putFloat(KEY_BODY_FAT, profile.bodyFatPercent)
        } else {
            editor.remove(KEY_BODY_FAT)
        }
        editor.apply()
    }

    fun loadTodayLog(): MacroLog {
        val today = today()
        if (prefs.getString(KEY_LOG_DATE, "") != today) {
            prefs.edit()
                .putFloat(KEY_PROT, 0f)
                .putFloat(KEY_CARBS, 0f)
                .putFloat(KEY_FAT, 0f)
                .putString(KEY_LOG_DATE, today)
                .apply()
        }
        return MacroLog(
            proteinG = prefs.getFloat(KEY_PROT, 0f),
            carbsG = prefs.getFloat(KEY_CARBS, 0f),
            fatG = prefs.getFloat(KEY_FAT, 0f)
        )
    }

    fun saveLog(log: MacroLog) {
        prefs.edit()
            .putFloat(KEY_PROT, log.proteinG)
            .putFloat(KEY_CARBS, log.carbsG)
            .putFloat(KEY_FAT, log.fatG)
            .putString(KEY_LOG_DATE, today())
            .apply()
    }

    fun completedWorkoutId(): String? {
        val today = today()
        return if (prefs.getString(KEY_WORKOUT_DATE, "") == today) {
            prefs.getString(KEY_WORKOUT_ID, null)
        } else null
    }

    fun completeWorkout(id: String) {
        prefs.edit()
            .putString(KEY_WORKOUT_ID, id)
            .putString(KEY_WORKOUT_DATE, today())
            .apply()
    }

    fun clearSetup() {
        prefs.edit().putBoolean(KEY_SET, false).apply()
    }

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    companion object {
        private const val PREFS = "BodyData"
        private const val KEY_MALE = "isMale"
        private const val KEY_WEIGHT = "Weight"
        private const val KEY_HEIGHT = "Height"
        private const val KEY_ACTIVE = "Active"
        private const val KEY_SLEEP = "Sleep"
        private const val KEY_AGE = "Age"
        private const val KEY_SET = "Set"
        private const val KEY_BODY_FAT = "BodyFat"
        private const val KEY_PROT = "logged_protein"
        private const val KEY_CARBS = "logged_carbs"
        private const val KEY_FAT = "logged_fat"
        private const val KEY_LOG_DATE = "log_date"
        private const val KEY_WORKOUT_ID = "workout_id"
        private const val KEY_WORKOUT_DATE = "workout_date"
    }
}
