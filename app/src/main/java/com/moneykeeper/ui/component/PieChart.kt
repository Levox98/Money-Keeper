package com.moneykeeper.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.moneykeeper.domain.model.Category
import java.math.BigDecimal

@Composable
fun PieChart(
    data: List<Pair<Category, BigDecimal>>,
    total: BigDecimal,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0),
        Color(0xFF673AB7), Color(0xFF3F51B5), Color(0xFF2196F3),
        Color(0xFF03A9F4), Color(0xFF00BCD4), Color(0xFF009688),
        Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
        Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800),
        Color(0xFFFF5722)
    )

    Canvas(modifier = modifier) {
        var startAngle = -90f
        data.forEachIndexed { index, pair ->
            val sweepAngle = (pair.second.toDouble() / total.toDouble() * 360f).toFloat()
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
        }
    }
}
