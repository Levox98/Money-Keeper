package com.moneykeeper.domain.usecase

import com.moneykeeper.domain.repository.FundsRepository
import com.moneykeeper.domain.ai.FinanceAIAnalyzer
import kotlinx.coroutines.flow.first

class AnalyzeFinancesUseCase(
    private val repository: FundsRepository,
    private val analyzer: FinanceAIAnalyzer
) {
    suspend fun execute(): Result<String> {
        return try {
            val transactions = repository.getAllTransactions().first()
            if (transactions.isEmpty()) {
                return Result.failure(Exception("Нет транзакций для анализа. Добавьте свои первые расходы!"))
            }
            val report = analyzer.analyzeSpending(transactions)
            Result.success(report)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
