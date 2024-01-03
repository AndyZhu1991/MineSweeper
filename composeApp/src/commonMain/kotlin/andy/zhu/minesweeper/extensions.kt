package andy.zhu.minesweeper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

fun <T, M> StateFlow<T>.map(
    coroutineScope : CoroutineScope,
    mapper : (value : T) -> M
) : StateFlow<M> = map { mapper(it) }.stateIn(
    coroutineScope,
    SharingStarted.Eagerly,
    mapper(value)
)

fun <T1, T2, M> StateFlow<T1>.combine(
    coroutineScope : CoroutineScope,
    other : StateFlow<T2>,
    mapper : (value1 : T1, value2 : T2) -> M
) : StateFlow<M> {
    return combine(other) { v1, v2 ->
        mapper(v1, v2)
    }.stateIn(
        coroutineScope,
        SharingStarted.Eagerly,
        mapper(value, other.value)
    )
}