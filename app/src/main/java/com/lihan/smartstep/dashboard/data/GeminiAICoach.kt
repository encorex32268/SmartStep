package com.lihan.smartstep.dashboard.data

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.RequestOptions
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.lihan.smartstep.BuildConfig
import com.lihan.smartstep.dashboard.domain.AICoach
import com.lihan.smartstep.dashboard.domain.AICoachConfig


private const val apiKey: String = BuildConfig.GEMINI_API_KEY
private const val modelName: String = "gemini-flash-lite-latest"

class GeminiAICoach: AICoach {

    // 基礎模型參數設定 (Generation Config)
    private val config: GenerationConfig = generationConfig {
        temperature = 0.5f // 保持適度創意與親切感
        topK = 30          // 控制詞彙選擇範圍，使回答穩定流暢
    }

    // 主模型
    private val cloudModel: GenerativeModel by lazy {
        GenerativeModel(
            modelName = modelName,
            apiKey = apiKey,
            generationConfig = config,
            systemInstruction = content { text(AICoachConfig.SYSTEM_INSTRUCTION) },
            requestOptions = RequestOptions(apiVersion = "v1beta")
        )
    }


    /**
     * 向活動教練發送提示詞並獲取回應
     */
    override suspend fun generateCoachResponse(userMessage: String): String {
        return safeGenerateContent { model ->
            model.generateContent(userMessage)
        }
    }

    /**
     * 結合使用者當前活動數據向活動教練發送請求
     */
    override suspend fun generateCoachResponseWithStepData(
        userMessage: String,
        currentSteps: Int,
        stepGoal: Int,
        spentTimeMinutes: Int,
        distanceKm: Double,
        caloriesBurned: Int,
        otherRule: String,
    ): String {
//        - 運動時間：$spentTimeMinutes 分鐘
        val contextData = """
            【使用者當前活動數據】
            - 當前步數：$currentSteps 步
            - 目標步數：$stepGoal 步
           
            - 步行距離：$distanceKm 公里
            - 消耗熱量：$caloriesBurned 大卡
            
            使用者訊息：$userMessage
            
            $otherRule
            
        """.trimIndent()

        return safeGenerateContent { model ->
            model.generateContent(contextData)
        }
    }

    private suspend fun safeGenerateContent(
        action: suspend (GenerativeModel) -> GenerateContentResponse
    ): String {
        // 先嘗試主模型
        try {
            val response = action(cloudModel)
            val text = getResponseText(response)
            if (text != null) return text
        } catch (e: Exception) {
            return e.localizedMessage?:""
        }
        return "Something happened ! Please try again."
    }

    private fun getResponseText(response: GenerateContentResponse): String? {
        return try {
            response.text?.takeIf { it.isNotBlank() }
        } catch (_: Exception) {
            null
        }
    }
}