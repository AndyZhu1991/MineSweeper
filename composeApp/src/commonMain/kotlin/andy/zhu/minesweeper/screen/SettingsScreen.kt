package andy.zhu.minesweeper.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import andy.zhu.minesweeper.navigation.SettingsScreenComponent
import kotlin.math.max

@Composable
fun SettingsScreen(component: SettingsScreenComponent) {
    Scaffold(
        topBar = { SimpleTopAppBar("Settings",0.5f, onBack = component.onClose) },
    ) { paddingValues ->
        val showActionToggle by component.showActionToggle.collectAsState()
        val defaultAction by component.defaultAction.collectAsState()
        ProvidePreferenceTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                SwitchPreference(
                    value = showActionToggle,
                    onValueChange = { component.onShowActionToggleChanged(it) },
                    title = { Text("Show Action Toggle") },
                    modifier = Modifier.padding(top = 16.dp),
                    summary = { Text("Show action toggle button on the right-bottom of the screen") }
                )

                ListPreference(
                    value = defaultAction,
                    onValueChange = { component.onDefaultActionChanged(it) },
                    values = SettingsScreenComponent.DefaultAction.entries,
                    title = { Text("Default Action") },
                    summary = {
                        when (defaultAction) {
                            SettingsScreenComponent.DefaultAction.Flag -> Text("Flag")
                            SettingsScreenComponent.DefaultAction.Dig -> Text("Dig")
                        }
                    }
                )
            }
        }
    }
}

// Copied code from: https://github.com/zhanghai/ComposePreference

@Stable
class PreferenceTheme(
    val categoryPadding: PaddingValues,
    val categoryColor: Color,
    val categoryTextStyle: TextStyle,
    val padding: PaddingValues,
    val horizontalSpacing: Dp,
    val verticalSpacing: Dp,
    val disabledOpacity: Float,
    val iconContainerMinWidth: Dp,
    val iconColor: Color,
    val titleColor: Color,
    val titleTextStyle: TextStyle,
    val summaryColor: Color,
    val summaryTextStyle: TextStyle,
    val dividerHeight: Dp
)

@Composable
fun preferenceTheme(
    categoryPadding: PaddingValues =
        PaddingValues(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 8.dp),
    categoryColor: Color = MaterialTheme.colorScheme.secondary,
    categoryTextStyle: TextStyle = MaterialTheme.typography.labelLarge,
    padding: PaddingValues = PaddingValues(16.dp),
    horizontalSpacing: Dp = 16.dp,
    verticalSpacing: Dp = 16.dp,
    disabledOpacity: Float = 0.38f,
    iconContainerMinWidth: Dp = 56.dp,
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    titleTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    summaryColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    summaryTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    dividerHeight: Dp = 32.dp
) =
    PreferenceTheme(
        categoryPadding = categoryPadding,
        categoryColor = categoryColor,
        categoryTextStyle = categoryTextStyle,
        padding = padding,
        horizontalSpacing = horizontalSpacing,
        verticalSpacing = verticalSpacing,
        disabledOpacity = disabledOpacity,
        iconContainerMinWidth = iconContainerMinWidth,
        iconColor = iconColor,
        titleColor = titleColor,
        titleTextStyle = titleTextStyle,
        summaryColor = summaryColor,
        summaryTextStyle = summaryTextStyle,
        dividerHeight = dividerHeight
    )

internal fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}

val LocalPreferenceTheme =
    compositionLocalOf<PreferenceTheme> { noLocalProvidedFor("LocalPreferenceTheme") }

@Composable
fun ProvidePreferenceTheme(
    theme: PreferenceTheme = preferenceTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalPreferenceTheme provides theme, content = content)
}


@Composable
fun BasicPreference(
    textContainer: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconContainer: @Composable () -> Unit = {},
    widgetContainer: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier =
        modifier.then(
            if (onClick != null) {
                Modifier.clickable(enabled, onClick = onClick)
            } else {
                Modifier
            }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconContainer()
        Box(modifier = Modifier.weight(1f)) { textContainer() }
        widgetContainer()
    }
}

@Stable
private class CopiedPaddingValues(
    private val start: Dp,
    private val top: Dp,
    private val end: Dp,
    private val bottom: Dp,
    private val paddingValues: PaddingValues
) : PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
        (if (layoutDirection == LayoutDirection.Ltr) start else end).takeIf { it != Dp.Unspecified }
            ?: paddingValues.calculateLeftPadding(layoutDirection)

    override fun calculateTopPadding(): Dp =
        top.takeIf { it != Dp.Unspecified } ?: paddingValues.calculateTopPadding()

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
        (if (layoutDirection == LayoutDirection.Ltr) end else start).takeIf { it != Dp.Unspecified }
            ?: paddingValues.calculateRightPadding(layoutDirection)

    override fun calculateBottomPadding(): Dp =
        bottom.takeIf { it != Dp.Unspecified } ?: paddingValues.calculateBottomPadding()

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is CopiedPaddingValues) {
            return false
        }
        return start == other.start &&
                top == other.top &&
                end == other.end &&
                bottom == other.bottom &&
                paddingValues == other.paddingValues
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + top.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + bottom.hashCode()
        result = 31 * result + paddingValues.hashCode()
        return result
    }

    override fun toString(): String {
        return "Copied($start, $top, $end, $bottom, $paddingValues)"
    }
}

@Composable
internal fun PaddingValues.copy(
    horizontal: Dp = Dp.Unspecified,
    vertical: Dp = Dp.Unspecified
): PaddingValues = copy(start = horizontal, top = vertical, end = horizontal, bottom = vertical)

@Composable
internal fun PaddingValues.copy(
    start: Dp = Dp.Unspecified,
    top: Dp = Dp.Unspecified,
    end: Dp = Dp.Unspecified,
    bottom: Dp = Dp.Unspecified
): PaddingValues = CopiedPaddingValues(start, top, end, bottom, this)

@Composable
fun Preference(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    widgetContainer: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    BasicPreference(
        textContainer = {
            val theme = LocalPreferenceTheme.current
            Column(
                modifier =
                Modifier.padding(
                    theme.padding.copy(
                        start = if (icon != null) 0.dp else Dp.Unspecified,
                        end = if (widgetContainer != null) 0.dp else Dp.Unspecified,
                    )
                )
            ) {
                PreferenceDefaults.TitleContainer(title = title, enabled = enabled)
                PreferenceDefaults.SummaryContainer(summary = summary, enabled = enabled)
            }
        },
        modifier = modifier,
        enabled = enabled,
        iconContainer = { PreferenceDefaults.IconContainer(icon = icon, enabled = enabled) },
        widgetContainer = { widgetContainer?.invoke() },
        onClick = onClick
    )
}

internal object PreferenceDefaults {
    @Composable
    fun IconContainer(
        icon: @Composable (() -> Unit)?,
        enabled: Boolean,
        excludedEndPadding: Dp = 0.dp
    ) {
        if (icon != null) {
            val theme = LocalPreferenceTheme.current
            Box(
                modifier =
                Modifier.widthIn(min = theme.iconContainerMinWidth - excludedEndPadding)
                    .padding(theme.padding.copy(end = 0.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides
                            theme.iconColor.let {
                                if (enabled) it else it.copy(alpha = theme.disabledOpacity)
                            },
                    content = icon
                )
            }
        }
    }

    @Composable
    fun TitleContainer(title: @Composable () -> Unit, enabled: Boolean) {
        val theme = LocalPreferenceTheme.current
        CompositionLocalProvider(
            LocalContentColor provides
                    theme.titleColor.let { if (enabled) it else it.copy(alpha = theme.disabledOpacity) }
        ) {
            ProvideTextStyle(value = theme.titleTextStyle, content = title)
        }
    }

    @Composable
    fun SummaryContainer(summary: (@Composable () -> Unit)?, enabled: Boolean) {
        if (summary != null) {
            val theme = LocalPreferenceTheme.current
            CompositionLocalProvider(
                LocalContentColor provides
                        theme.summaryColor.let {
                            if (enabled) it else it.copy(alpha = theme.disabledOpacity)
                        }
            ) {
                ProvideTextStyle(value = theme.summaryTextStyle, content = summary)
            }
        }
    }
}

@Composable
fun SwitchPreference(
    state: MutableState<Boolean>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null
) {
    var value by state
    SwitchPreference(
        value = value,
        onValueChange = { value = it },
        title = title,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = summary
    )
}

@Composable
fun SwitchPreference(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null
) {
    Preference(
        title = title,
        modifier = modifier.toggleable(value, enabled, Role.Switch, onValueChange),
        enabled = enabled,
        icon = icon,
        summary = summary,
        widgetContainer = {
            val theme = LocalPreferenceTheme.current
            Switch(
                checked = value,
                onCheckedChange = null,
                modifier = Modifier.padding(theme.padding.copy(start = theme.horizontalSpacing)),
                enabled = enabled
            )
        }
    )
}

enum class ListPreferenceType {
    ALERT_DIALOG,
    DROPDOWN_MENU
}

internal object ListPreferenceDefaults {
    fun <T> item(
        type: ListPreferenceType,
        valueToText: (T) -> AnnotatedString
    ): @Composable (value: T, currentValue: T, onClick: () -> Unit) -> Unit =
        when (type) {
            ListPreferenceType.ALERT_DIALOG -> {
                { value, currentValue, onClick ->
                    DialogItem(value, currentValue, valueToText, onClick)
                }
            }
            ListPreferenceType.DROPDOWN_MENU -> {
                { value, currentValue, onClick ->
                    DropdownMenuItem(value, currentValue, valueToText, onClick)
                }
            }
        }

    @Composable
    private fun <T> DialogItem(
        value: T,
        currentValue: T,
        valueToText: (T) -> AnnotatedString,
        onClick: () -> Unit
    ) {
        val selected = value == currentValue
        Row(
            modifier =
            Modifier.fillMaxWidth()
                .heightIn(min = 48.dp)
                .selectable(selected, true, Role.RadioButton, onClick)
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = selected, onClick = null)
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = valueToText(value),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    @Composable
    private fun <T> DropdownMenuItem(
        value: T,
        currentValue: T,
        valueToText: (T) -> AnnotatedString,
        onClick: () -> Unit
    ) {
        DropdownMenuItem(
            text = { Text(text = valueToText(value)) },
            onClick = onClick,
            modifier =
            Modifier.background(
                if (value == currentValue) MaterialTheme.colorScheme.secondaryContainer
                else Color.Transparent
            ),
            colors = MenuDefaults.itemColors()
        )
    }
}

@Composable
fun <T> ListPreference(
    value: T,
    onValueChange: (T) -> Unit,
    values: List<T>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    type: ListPreferenceType = ListPreferenceType.ALERT_DIALOG,
    valueToText: (T) -> AnnotatedString = { AnnotatedString(it.toString()) },
    item: @Composable (value: T, currentValue: T, onClick: () -> Unit) -> Unit =
        ListPreferenceDefaults.item(type, valueToText)
) {
    var openSelector by rememberSaveable { mutableStateOf(false) }
    // Put DropdownMenu before Preference so that it can anchor to the right position.
    if (openSelector) {
        when (type) {
            ListPreferenceType.ALERT_DIALOG -> {
                PreferenceAlertDialog(
                    onDismissRequest = { openSelector = false },
                    title = title,
                    buttons = {
                        TextButton(onClick = { openSelector = false }) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    val lazyListState = rememberLazyListState()
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().verticalScrollIndicators(lazyListState),
                        state = lazyListState
                    ) {
                        items(values) { itemValue ->
                            item(itemValue, value) {
                                onValueChange(itemValue)
                                openSelector = false
                            }
                        }
                    }
                }
            }
            ListPreferenceType.DROPDOWN_MENU -> {
                val theme = LocalPreferenceTheme.current
                Box(
                    modifier = Modifier.fillMaxWidth().padding(theme.padding.copy(vertical = 0.dp))
                ) {
                    DropdownMenu(
                        expanded = openSelector,
                        onDismissRequest = { openSelector = false }
                    ) {
                        for (itemValue in values) {
                            item(itemValue, value) {
                                onValueChange(itemValue)
                                openSelector = false
                            }
                        }
                    }
                }
            }
        }
    }
    Preference(
        title = title,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = summary
    ) {
        openSelector = true
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun PreferenceAlertDialog(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    buttons: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AlertDialog(onDismissRequest = onDismissRequest, modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier =
                    Modifier.fillMaxWidth()
                        .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 16.dp)
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides AlertDialogDefaults.titleContentColor
                    ) {
                        ProvideTextStyle(MaterialTheme.typography.headlineSmall, title)
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().weight(1f, fill = false)) { content() }
                Box(
                    modifier =
                    Modifier.fillMaxWidth()
                        .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    AlertDialogFlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 12.dp) {
                        CompositionLocalProvider(
                            LocalMinimumInteractiveComponentEnforcement provides false,
                            content = buttons
                        )
                    }
                }
            }
        }
    }
}

// Copied from androidx.compose.material3.AlertDialogFlowRow .
@Composable
internal fun AlertDialogFlowRow(
    mainAxisSpacing: Dp,
    crossAxisSpacing: Dp,
    content: @Composable () -> Unit
) {
    Layout(content) { measurables, constraints ->
        val sequences = mutableListOf<List<Placeable>>()
        val crossAxisSizes = mutableListOf<Int>()
        val crossAxisPositions = mutableListOf<Int>()

        var mainAxisSpace = 0
        var crossAxisSpace = 0

        val currentSequence = mutableListOf<Placeable>()
        var currentMainAxisSize = 0
        var currentCrossAxisSize = 0

        // Return whether the placeable can be added to the current sequence.
        fun canAddToCurrentSequence(placeable: Placeable) =
            currentSequence.isEmpty() ||
                    currentMainAxisSize + mainAxisSpacing.roundToPx() + placeable.width <=
                    constraints.maxWidth

        // Store current sequence information and start a new sequence.
        fun startNewSequence() {
            if (sequences.isNotEmpty()) {
                crossAxisSpace += crossAxisSpacing.roundToPx()
            }
            sequences += currentSequence.toList()
            crossAxisSizes += currentCrossAxisSize
            crossAxisPositions += crossAxisSpace

            crossAxisSpace += currentCrossAxisSize
            mainAxisSpace = max(mainAxisSpace, currentMainAxisSize)

            currentSequence.clear()
            currentMainAxisSize = 0
            currentCrossAxisSize = 0
        }

        for (measurable in measurables) {
            // Ask the child for its preferred size.
            val placeable = measurable.measure(constraints)

            // Start a new sequence if there is not enough space.
            if (!canAddToCurrentSequence(placeable)) startNewSequence()

            // Add the child to the current sequence.
            if (currentSequence.isNotEmpty()) {
                currentMainAxisSize += mainAxisSpacing.roundToPx()
            }
            currentSequence.add(placeable)
            currentMainAxisSize += placeable.width
            currentCrossAxisSize = max(currentCrossAxisSize, placeable.height)
        }

        if (currentSequence.isNotEmpty()) startNewSequence()

        val mainAxisLayoutSize = max(mainAxisSpace, constraints.minWidth)

        val crossAxisLayoutSize = max(crossAxisSpace, constraints.minHeight)

        val layoutWidth = mainAxisLayoutSize

        val layoutHeight = crossAxisLayoutSize

        layout(layoutWidth, layoutHeight) {
            sequences.forEachIndexed { i, placeables ->
                val childrenMainAxisSizes =
                    IntArray(placeables.size) { j ->
                        placeables[j].width +
                                if (j < placeables.lastIndex) mainAxisSpacing.roundToPx() else 0
                    }
                val arrangement = Arrangement.End
                val mainAxisPositions = IntArray(childrenMainAxisSizes.size) { 0 }
                with(arrangement) {
                    arrange(
                        mainAxisLayoutSize,
                        childrenMainAxisSizes,
                        layoutDirection,
                        mainAxisPositions
                    )
                }
                placeables.forEachIndexed { j, placeable ->
                    placeable.place(x = mainAxisPositions[j], y = crossAxisPositions[i])
                }
            }
        }
    }
}

internal fun Modifier.verticalScrollIndicators(
    scrollableState: ScrollableState,
    reverseScrolling: Boolean = false,
    drawTopIndicator: Boolean = true,
    drawBottomIndicator: Boolean = true
): Modifier =
    scrollIndicators(
        scrollableState,
        Orientation.Vertical,
        reverseScrolling,
        drawTopIndicator,
        drawBottomIndicator
    )

internal fun Modifier.horizontalScrollIndicators(
    scrollableState: ScrollableState,
    reverseScrolling: Boolean = false,
    drawStartIndicator: Boolean = true,
    drawEndIndicator: Boolean = true
): Modifier =
    scrollIndicators(
        scrollableState,
        Orientation.Horizontal,
        reverseScrolling,
        drawStartIndicator,
        drawEndIndicator
    )

private fun Modifier.scrollIndicators(
    scrollableState: ScrollableState,
    orientation: Orientation,
    reverseScrolling: Boolean = false,
    drawTopStartIndicator: Boolean = true,
    drawBottomEndIndicator: Boolean = true
): Modifier =
    composed(
        debugInspectorInfo {
            name = "scrollIndicators"
            properties["orientation"] = orientation
            properties["reverseScrolling"] = reverseScrolling
            properties["scrollableState"] = scrollableState
            properties["drawTopStartIndicator"] = drawTopStartIndicator
            properties["drawBottomEndIndicator"] = drawBottomEndIndicator
        }
    ) {
        val layoutDirection = LocalLayoutDirection.current
        val reverseDirection =
            ScrollableDefaults.reverseDirection(layoutDirection, orientation, reverseScrolling)
        val drawTopLeftIndicator: Boolean
        val drawBottomRightIndicator: Boolean
        when (orientation) {
            Orientation.Vertical -> {
                drawTopLeftIndicator = drawTopStartIndicator
                drawBottomRightIndicator = drawBottomEndIndicator
            }
            Orientation.Horizontal -> {
                when (layoutDirection) {
                    LayoutDirection.Ltr -> {
                        drawTopLeftIndicator = drawTopStartIndicator
                        drawBottomRightIndicator = drawBottomEndIndicator
                    }
                    LayoutDirection.Rtl -> {
                        drawTopLeftIndicator = drawBottomEndIndicator
                        drawBottomRightIndicator = drawTopStartIndicator
                    }
                }
            }
        }
        val color = LocalContentColor.current.copy(alpha = ScrollIndicatorOpacity)
        val thickness = with(LocalDensity.current) { ScrollIndicatorThickness.toPx() }
        drawWithContent {
            drawContent()
            val indicatorSize =
                when (orientation) {
                    Orientation.Vertical -> Size(size.width, thickness)
                    Orientation.Horizontal -> Size(thickness, size.height)
                }
            if (drawTopLeftIndicator) {
                val canScrollUpLeft =
                    if (reverseDirection) {
                        scrollableState.canScrollBackward
                    } else {
                        scrollableState.canScrollForward
                    }
                if (canScrollUpLeft) {
                    drawRect(color = color, size = indicatorSize)
                }
            }
            if (drawBottomRightIndicator) {
                val canScrollDownRight =
                    if (reverseDirection) {
                        scrollableState.canScrollForward
                    } else {
                        scrollableState.canScrollBackward
                    }
                val topLeft =
                    when (orientation) {
                        Orientation.Vertical -> Offset(0f, size.height - thickness)
                        Orientation.Horizontal -> Offset(size.width - thickness, 0f)
                    }
                if (canScrollDownRight) {
                    drawRect(color = color, topLeft = topLeft, size = indicatorSize)
                }
            }
        }
    }

private const val ScrollIndicatorOpacity = 0.12f
private val ScrollIndicatorThickness = 1.dp
