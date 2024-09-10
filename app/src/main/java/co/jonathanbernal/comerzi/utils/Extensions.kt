package co.jonathanbernal.comerzi.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.text.NumberFormat
import java.util.Locale

fun <R> Flow<R>.toStateFlow(coroutineScope: CoroutineScope, initialValue: R) =
    stateIn(coroutineScope, SharingStarted.Lazily, initialValue)

fun String?.orEmpty(): String = this ?: ""

fun Int?.orZero(): Int = this ?: 0

fun formatPrice(price: Double): String {
    val format = NumberFormat.getNumberInstance(Locale.GERMANY) // Utiliza el formato con punto del mil
    return format.format(price)
}