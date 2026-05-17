package com.moneykeeper.domain.ai

import com.moneykeeper.domain.model.FinancialTransaction
import com.moneykeeper.domain.model.TransactionType
import com.google.mlkit.genai.prompt.Generation
import java.math.BigDecimal

class FinanceAIAnalyzer {

    private val generativeModel = Generation.getClient()

    suspend fun analyzeSpending(transactions: List<FinancialTransaction>): String {
        val promptText = buildPrompt(transactions)
        
        return try {
            val response = generativeModel.generateContent(promptText)
            response.candidates.firstOrNull()?.text ?: "Не удалось получить рекомендации"
        } catch (e: Exception) {
            "Ошибка при анализе данных: ${e.localizedMessage}"
        }
    }

    private fun buildPrompt(transactions: List<FinancialTransaction>): String {
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
        
        val categoryTotals = expenses.groupBy { it.category.name }
            .mapValues { (_, txs) -> 
                txs.fold(BigDecimal.ZERO) { acc, tx -> acc.add(tx.amount) }
            }
            .toList()
            .sortedByDescending { it.second }

        val formattedData = categoryTotals.joinToString("\n") { (category, total) ->
            "- $category: $total"
        }

        return """
            Проанализируй следующие расходы пользователя и дай финансовые рекомендации.
            Данные по категориям (сумма):
            $formattedData
            
            Требования к ответу:
            1. Определи основные тренды (куда уходит больше всего денег).
            2. Дай 2-3 практических совета, как оптимизировать расходы в самых крупных категориях.
            3. Напиши одну вдохновляющую рекомендацию по финансовой грамотности.
            Ответ должен быть на русском языке, кратким и полезным.
        """.trimIndent()
    }
}
