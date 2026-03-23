package com.lihan.smartstep.coach.data

import com.google.genai.Client
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.lihan.smartstep.BuildConfig
import com.lihan.smartstep.coach.domain.AIGenerator
import com.lihan.smartstep.core.domain.AppDataStore
import com.lihan.smartstep.stepcount.domain.repository.SmartStepRepository
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber

class GeminiAIGenerator(
    private val smartStepRepository: SmartStepRepository,
    private val appDataStore: AppDataStore
): AIGenerator {

    private val client: Client by lazy {
        Client.builder()
            .apiKey(BuildConfig.GOOGLE_API_KEY)
            .build()
    }

    override suspend fun sendSuggestion(text: String): String? {
        return withContext(Dispatchers.IO){
            try {
                val usersHeight = appDataStore.getHeight().first()
                val usersWeight = appDataStore.getWeight().first()
                val usersGender = appDataStore.getGender().first()
                val todayGoal = appDataStore.getTotalStep().first()
                val nowStep = appDataStore.getTodaySteps().first()
                val recentlyActivities = smartStepRepository
                    .getWeekDailySteps(DateTimeUtils.getTodayEpochMilli())
                    .first()


                val referenceData = """
                    User's info:
                    Height: $usersHeight cm.
                    Weight: $usersWeight kg.
                    Gender: ${usersGender.name}.
                    -----
                    TodayActivity:
                    NowStep: $nowStep / TodayGoal: $todayGoal
                    -----
                    RecentlyActivities:
                    $recentlyActivities
                """.trimIndent()

                val role = Content
                    .builder()
                    .role(
                        """
                        You are a professional walking coach。    
                        ---
                        Rules:
                        1.Strictly prohibit listing the user's personal parameters (such as height, weight, or BMI) in your response unless explicitly asked by the user.
                        2.Provide direct, actionable suggestions instead of restating or summarizing the reference data.
                        3.Maintain a concise and motivating tone.
                        4.Current User Data: $referenceData .
                        5.Don't show  current User Data to user
                    """.trimIndent()
                    )
                    .build()

                val config = GenerateContentConfig.builder()
                    .maxOutputTokens(700)
                    .systemInstruction(role)
                    .build()


                val response =  client.models.generateContent(
                    "gemini-3-flash-preview",
                    "Context: \n $referenceData \n\n  Task: Based on the context, answer: $text",
                    config
                )
                response.text()
            }catch (e: Exception){
                ensureActive()
                e.printStackTrace()
                Timber.d("AI Generate error ${e.message}")
                null
            }

        }

    }
}