package andy.zhu.minesweeper.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
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

fun Matrix.transformed(block: Matrix.() -> Unit): Matrix {
    return Matrix(values.copyOf()).apply(block)
}

fun Matrix.copy(): Matrix {
    return Matrix(values.copyOf())
}

fun Matrix.inverted(): Matrix {
    return transformed { invert() }
}

fun Matrix.scaleX(): Float {
    return this.values[Matrix.ScaleX]
}

fun Matrix.scaleY(): Float {
    return this.values[Matrix.ScaleY]
}

fun Matrix.translateX(): Float {
    return this.values[Matrix.TranslateX]
}

fun Matrix.translateY(): Float {
    return this.values[Matrix.TranslateY]
}

fun Matrix.scale(scaleX: Float, scaleY: Float, pivotX: Float, pivotY: Float) {
    val deltaX = (pivotX - translateX()) / this.scaleX()
    val deltaY = (pivotY - translateY()) / this.scaleY()
    translate(deltaX, deltaY)
    scale(scaleX, scaleY)
    translate(-deltaX, -deltaY)
}

fun Matrix.postTranslate(translateX: Float, translateY: Float) {
    translate(translateX / scaleX(), translateY / scaleY())
}

fun Color.transform(to: Color, fraction: Float): Color {
    return Color(
        red = this.red + (to.red - this.red) * fraction,
        green = this.green + (to.green - this.green) * fraction,
        blue = this.blue + (to.blue - this.blue) * fraction,
        alpha = this.alpha + (to.alpha - this.alpha) * fraction,
    )
}