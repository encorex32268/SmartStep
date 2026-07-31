package com.lihan.smartstep.dashboard.data

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.GoogleGenerativeAIException
import com.google.ai.client.generativeai.type.InvalidAPIKeyException
import com.google.ai.client.generativeai.type.PromptBlockedException
import com.google.ai.client.generativeai.type.QuotaExceededException
import com.google.ai.client.generativeai.type.ResponseStoppedException
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.lihan.smartstep.BuildConfig
import com.lihan.smartstep.dashboard.domain.AICoach
import com.lihan.smartstep.dashboard.domain.AICoachConfig


private const val apiKey: String = BuildConfig.GEMINI_API_KEY
private const val modelName: String = "gemini-flash-latest"

class GeminiAICoach: AICoach {

    // 基礎模型參數設定 (Generation Config)
    private val config: GenerationConfig = generationConfig {
        temperature = 0.7f // 保持適度創意與親切感
        topK = 40          // 控制詞彙選擇範圍，使回答穩定流暢
    }

    // 主模型
    private val primaryModel: GenerativeModel by lazy {
        GenerativeModel(
            modelName = modelName,
            apiKey = apiKey,
            generationConfig = config,
            systemInstruction = content { text(AICoachConfig.SYSTEM_INSTRUCTION) }
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
        caloriesBurned: Int
    ): String {
        val contextData = """
            【使用者當前活動數據】
            - 當前步數：$currentSteps 步
            - 目標步數：$stepGoal 步
            - 運動時間：$spentTimeMinutes 分鐘
            - 步行距離：$distanceKm 公里
            - 消耗熱量：$caloriesBurned 大卡
            
            使用者訊息：$userMessage
        """.trimIndent()

        return safeGenerateContent { model ->
            model.generateContent(contextData)
        }
    }

    /**
     * 安全發送請求並處理配額、API Key 無效等例外狀況，具備備用模型自動切換與友善錯誤訊息機制
     */
    private suspend fun safeGenerateContent(
        action: suspend (GenerativeModel) -> GenerateContentResponse
    ): String {
        // 先嘗試主模型
        try {
            val response = action(primaryModel)
            val text = getResponseText(response)
            if (text != null) return text
        } catch (e: Exception) {
            return handleException(e)
        }
        return "教練暫時無法提供回應，請稍後再試。"
    }

    private fun getResponseText(response: GenerateContentResponse): String? {
        return try {
            response.text?.takeIf { it.isNotBlank() }
        } catch (_: Exception) {
            null
        }
    }

    private fun handleException(e: Throwable): String {
        val message = e.localizedMessage.orEmpty()
        val isQuotaError = e is QuotaExceededException ||
                message.contains("Quota", ignoreCase = true) ||
                message.contains("429", ignoreCase = true) ||
                message.contains("RESOURCE_EXHAUSTED", ignoreCase = true)

        if (isQuotaError) {
            return "目前 AI 服務請求額度已滿 (Quota Exceeded)，請稍等約 15 秒後再試！"
        }

        return when (e) {
            is InvalidAPIKeyException -> "API Key 設定無效或未提供，請檢查您的 Gemini API Key。"
            is PromptBlockedException -> "訊息內容觸發安全過濾機制，請調整提問方式重試。"
            is ResponseStoppedException -> "回應產生中斷，請稍後重試。"
            is GoogleGenerativeAIException -> {
                if (message.contains("Unexpected Response", ignoreCase = true)) {
                    "AI 服務回應異常，請稍後再試。"
                } else if (message.isNotBlank()) {
                    "AI 服務發生異常：$message"
                } else {
                    "AI 服務暫時無法提供回應，請稍後重試。"
                }
            }
            else -> "網路或連線發生問題，請確認網路連線後重試。"
        }
    }
}