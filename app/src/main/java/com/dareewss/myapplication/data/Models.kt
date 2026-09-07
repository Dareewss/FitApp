package com.dareewss.myapplication.data

enum class ActivityLevel(val multiplier: Float, val title: String, val detail: String) {
    SEDENTARY(1.2f, "Sedentary", "Little or no exercise"),
    LIGHT(1.375f, "Light", "1–3 days / week"),
    MODERATE(1.55f, "Moderate", "3–5 days / week"),
    ACTIVE(1.725f, "Active", "6–7 days / week"),
    VERY_ACTIVE(1.9f, "Very Active", "Athlete / physical job");

    companion object {
        fun fromMultiplier(value: Float): ActivityLevel =
            entries.minBy { kotlin.math.abs(it.multiplier - value) }
    }
}

data class UserProfile(
    val isMale: Boolean,
    val age: Int,
    val weightKg: Float,
    val heightCm: Float,
    val sleepHours: Int,
    val activity: ActivityLevel,
    val bodyFatPercent: Float? = null
)

data class MacroLog(
    val proteinG: Float = 0f,
    val carbsG: Float = 0f,
    val fatG: Float = 0f
) {
    val kcal: Int get() = kotlin.math.round(proteinG * 4 + carbsG * 4 + fatG * 9).toInt()
}

data class MacroTargets(
    val kcal: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatG: Int
)

enum class BmrMethod(val label: String) {
    KATCH_MCARDLE("KATCH-MCARDLE"),
    MIFFLIN_ST_JEOR("MIFFLIN-ST JEOR")
}

data class BodyMetrics(
    val bmi: Float,
    val bmiLabel: String,
    val bmr: Int,
    val tdee: Int,
    val method: BmrMethod,
    val macros: MacroTargets,
    val sleepStatus: String
)

fun UserProfile.computeMetrics(): BodyMetrics {
    val heightM = heightCm / 100f
    val bmi = if (heightM > 0f) weightKg / (heightM * heightM) else 0f
    val bmiLabel = when {
        bmi < 18.5f -> "Underweight"
        bmi < 25f -> "Normal weight"
        bmi < 30f -> "Overweight"
        else -> "Obese"
    }

    val fat = bodyFatPercent
    val (bmr, method) = if (fat != null && fat in 3f..60f) {
        val lbm = weightKg * (1f - fat / 100f)
        Pair(kotlin.math.round(370 + 21.6 * lbm).toInt(), BmrMethod.KATCH_MCARDLE)
    } else {
        val base = if (isMale) {
            10 * weightKg + 6.25 * heightCm - 5 * age + 5
        } else {
            10 * weightKg + 6.25 * heightCm - 5 * age - 161
        }
        Pair(kotlin.math.round(base).toInt(), BmrMethod.MIFFLIN_ST_JEOR)
    }

    val tdee = kotlin.math.round(bmr * activity.multiplier).toInt()
    val macros = MacroTargets(
        kcal = tdee,
        proteinG = kotlin.math.round(tdee * 0.30 / 4.0).toInt(),
        carbsG = kotlin.math.round(tdee * 0.40 / 4.0).toInt(),
        fatG = kotlin.math.round(tdee * 0.30 / 9.0).toInt()
    )
    val sleepStatus = when (sleepHours) {
        in 7..9 -> "OPTIMAL"
        in 6..6, in 10..10 -> "FAIR"
        else -> "LOW"
    }
    return BodyMetrics(bmi, bmiLabel, bmr, tdee, method, macros, sleepStatus)
}

data class WorkoutPlan(
    val id: String,
    val title: String,
    val level: String,
    val duration: String,
    val focus: String,
    val exercises: List<String>
)

object WorkoutLibrary {
    val plans = listOf(
        WorkoutPlan(
            id = "full_body",
            title = "Full Body Prime",
            level = "Foundation",
            duration = "40 min",
            focus = "Strength",
            exercises = listOf(
                "Goblet squat — 3 × 10",
                "Push-up or bench press — 3 × 8",
                "Bent-over row — 3 × 10",
                "Romanian deadlift — 3 × 8",
                "Plank — 3 × 40s"
            )
        ),
        WorkoutPlan(
            id = "upper",
            title = "Upper Engine",
            level = "Build",
            duration = "45 min",
            focus = "Push / Pull",
            exercises = listOf(
                "Overhead press — 4 × 6",
                "Pull-up or lat pulldown — 4 × 8",
                "Incline press — 3 × 10",
                "Seated row — 3 × 10",
                "Face pulls — 3 × 15"
            )
        ),
        WorkoutPlan(
            id = "lower",
            title = "Lower Drive",
            level = "Build",
            duration = "45 min",
            focus = "Legs",
            exercises = listOf(
                "Back squat — 4 × 6",
                "Walking lunge — 3 × 10/leg",
                "Hip hinge / RDL — 3 × 8",
                "Leg press or split squat — 3 × 10",
                "Calf raise — 3 × 15"
            )
        ),
        WorkoutPlan(
            id = "recovery",
            title = "Recovery Flow",
            level = "Restore",
            duration = "25 min",
            focus = "Mobility",
            exercises = listOf(
                "Worlds greatest stretch — 6/side",
                "Cat-cow — 10 breaths",
                "90/90 hip switch — 8/side",
                "Thoracic openers — 8/side",
                "Nasal walk — 5 min"
            )
        )
    )
}
