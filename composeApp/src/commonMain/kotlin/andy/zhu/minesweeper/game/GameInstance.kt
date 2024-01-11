package andy.zhu.minesweeper.game

import androidx.compose.ui.unit.IntOffset
import andy.zhu.minesweeper.extensions.combine
import andy.zhu.minesweeper.extensions.map
import andy.zhu.minesweeper.settings.RankItem
import andy.zhu.minesweeper.settings.getRank
import andy.zhu.minesweeper.settings.saveRank
import getPlatform
import kotlinx.coroutines.*

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock

class GameInstance(
    val gameConfig: GameConfig,
    private val coroutineScope: CoroutineScope,
) {
    private val hasMine = BooleanArray(gameConfig.mapSize())
    private val mineCount = IntArray(gameConfig.mapSize())
    private val status = Array(gameConfig.mapSize()) { GridStatus.HIDDEN }
    private val _mapUIFlow: MutableStateFlow<MineMapUI> = MutableStateFlow(buildMapUI())
    val mapUIFlow: StateFlow<MineMapUI> = _mapUIFlow

    private var hoverPosition: Position? = null

    private var timerJob: Job? = null
    private val timeMillis = MutableStateFlow(0)
    val timeString: StateFlow<String> = timeMillis.map(coroutineScope, Companion::toTimeString)

    private val openedCount = MutableStateFlow(0)
    private val flaggedCount = MutableStateFlow(0)

    private val minesRemaining = flaggedCount.map(coroutineScope) {
        val remaining = gameConfig.mineCount - it
        return@map if (remaining >= 0) remaining else 0
    }

    val minesRemainingText = minesRemaining.map(coroutineScope) { it.toString() }

    val showFab = getPlatform().isMobile
    private val _flagWhenTap = MutableStateFlow(getPlatform().isMobile)  // Flag the grid when tap
    val flagWhenTap: StateFlow<Boolean> = _flagWhenTap

    val gameStarted = openedCount.map(coroutineScope) { it > 0 }

    val gameOver = MutableStateFlow(false)
    val gameWinInfo: StateFlow<GameWinInfo?> = openedCount.map(coroutineScope) {
        if (it == gameConfig.mapSize() - gameConfig.mineCount) {
            buildWinInfo()
        } else {
            null
        }
    }

    val gameEnd = gameWinInfo.combine(coroutineScope, gameOver) { winInfo, failed ->
        winInfo != null || failed
    }

    init {
        coroutineScope.launch {
            gameEnd.collect {
                if (it) stopTimer()
            }
        }
    }

    fun onResume() {
        if (timerJob == null && openedCount.value > 0) {
            startTimer()
        }
    }

    fun onPause() {
        stopTimer()
    }

    fun onDestroy() {
        stopTimer()
    }

    fun onMineTap(position: Position) {
        if (!position.isValid() or gameEnd.value) {
            return
        }

        when (status[position]) {
            GridStatus.HIDDEN -> {
                if (openedCount.value != 0 && flagWhenTap.value) {
                    switchStatus(position)
                } else {
                    openGrids(listOf(position))
                }
            }
            GridStatus.FLAGGED, GridStatus.UNCERTAIN -> {
                if (flagWhenTap.value) {
                    switchStatus(position)
                }
            }
            GridStatus.OPENED -> {
                if (getPlatform().isMobile) {
                    tryOpenNeighbours(position)
                }
            }
        }
    }

    fun onMineLongTap(position: Position) {
        if (!position.isValid() or gameEnd.value) {
            return
        }
        if (flagWhenTap.value) {
            openGrids(listOf(position))
        } else {
            switchStatus(position)
        }
    }

    fun onMineRightClick(position: Position) {
        if (!position.isValid() or gameEnd.value) {
            return
        }
        switchStatus(position)
    }

    fun onMineMiddleClick(position: Position) {
        if (!position.isValid() or gameEnd.value) {
            return
        }
        tryOpenNeighbours(position)
    }

    fun onMineHover(ppos: Position?) {
        val position = if (ppos == null || !ppos.isValid() || status[ppos] != GridStatus.HIDDEN) {
            null
        } else {
            ppos
        }
        if (this.hoverPosition != position) {
            this.hoverPosition = position
            updateMapUI()
        }
    }

    fun switchTapAction() {
        _flagWhenTap.value = !_flagWhenTap.value
    }

    private fun indexToPosition(index: Int): Position {
        return Position(index % gameConfig.width, index / gameConfig.width)
    }

    private fun initMinesArray(firstHit: Position) {
        val noMineIndices = (neighbours(firstHit) + listOf(firstHit)).map(Position::flattenedIndex)
        (0 until gameConfig.mapSize())
                .shuffled()
                .filterNot(noMineIndices::contains)
                .take(gameConfig.mineCount)
                .forEach { hasMine[it] = true }
    }
    
    private fun initCountArray() {
        positions()
            .filterNot { hasMine[it] }
            .forEach {
                mineCount[it] = calcMineCount(it)
            }
    }
    
    private fun neighbours(position: Position): List<Position> {
        val x = position.x
        val y = position.y
        return listOf(
            Position(x = x - 1, y = y - 1),
            Position(x = x    , y = y - 1),
            Position(x = x + 1, y = y - 1),
            Position(x = x - 1, y = y    ),
            Position(x = x + 1, y = y    ),
            Position(x = x - 1, y = y + 1),
            Position(x = x    , y = y + 1),
            Position(x = x + 1, y = y + 1),
            )
            .filter(Position::isValid)
    }
    
    private fun calcMineCount(position: Position): Int {
        return neighbours(position).count { hasMine[it] }
    }
    
    private fun buildMapUI(animations: List<Pair<MineItemUI.Animation, Position>> = emptyList()): MineMapUI {
        val mineUIItems: List<MineItemUI> = MutableList(gameConfig.mapSize()) { index ->
            animations.firstOrNull { it.second.flattenedIndex() == index }?.let {
                return@MutableList it.first
            }
            when(status[index]) {
                GridStatus.HIDDEN -> {
                    if (hoverPosition?.flattenedIndex() == index) {
                        MineItemUI.HiddenHover
                    } else {
                        MineItemUI.Hidden
                    }
                }
                GridStatus.FLAGGED -> MineItemUI.Flagged
                GridStatus.UNCERTAIN -> MineItemUI.Uncertain
                GridStatus.OPENED -> {
                    if (hasMine[index]) {
                        MineItemUI.OpenedBoom
                    } else {
                        MineItemUI.Opened(mineCount[index])
                    }
                }
            }
        }
        return MineMapUI(mineUIItems, gameConfig)
    }
    
    private fun updateMapUI(animations: List<Pair<MineItemUI.Animation, Position>> = emptyList()) {
        _mapUIFlow.value = buildMapUI(animations)
    }
    
    private fun positions(): Sequence<Position> {
        return (0 until gameConfig.mapSize())
            .asSequence()
            .map(::indexToPosition)
    }

    private operator fun BooleanArray.get(position: Position): Boolean {
        return this[position.flattenedIndex()]
    }

    private operator fun IntArray.get(position: Position): Int {
        return this[position.flattenedIndex()]
    }

    private operator fun IntArray.set(position: Position, count: Int) {
        this[position.flattenedIndex()] = count
    }

    private operator fun Array<GridStatus>.get(position: Position): GridStatus {
        return this[position.flattenedIndex()]
    }

    private operator fun Array<GridStatus>.set(position: Position, status: GridStatus) {
        this[position.flattenedIndex()] = status
    }
    
    private fun switchStatus(position: Position) {
        val oldStatus = status[position]
        val newStatus = when(oldStatus) {
            GridStatus.HIDDEN -> GridStatus.FLAGGED
            GridStatus.OPENED -> GridStatus.OPENED
            GridStatus.FLAGGED -> GridStatus.HIDDEN
            GridStatus.UNCERTAIN -> GridStatus.HIDDEN
        }
        status[position] = newStatus
        updateMapUI()
        if (oldStatus == GridStatus.FLAGGED) {
            flaggedCount.value--
        } else if (newStatus == GridStatus.FLAGGED) {
            flaggedCount.value++
        }
    }
    
    private fun tryOpenNeighbours(position: Position) {
        if (status[position] == GridStatus.OPENED && mineCount[position] > 0) {
            val neighbours = neighbours(position)
            val flagCount = neighbours.count { status[it] == GridStatus.FLAGGED }
            val hiddens = neighbours.filter { status[it] == GridStatus.HIDDEN }
            if (hiddens.isNotEmpty()) {
                if (flagCount == mineCount[position]) {
                    openGrids(hiddens)
                } else if (flagCount < mineCount[position]) {
                    updateMapUI(hiddens.map { MineItemUI.BlinkAnimation to it })
                }
            }
        }
    }

    private fun openGrids(positions: List<Position>): List<Position> {
        if (openedCount.value == 0) {
            startTimer()
            initMinesArray(positions[0])
            initCountArray()
        }
        val queue = positions.filter { status[it] == GridStatus.HIDDEN }.toMutableList()
        var index = 0
        while (index < queue.size) {
            val position = queue[index]
            if (status[position] == GridStatus.HIDDEN) {
                status[position] = GridStatus.OPENED
                if (hasMine[position]) {
                    gameOver.value = true
                } else {
                    openedCount.value++
                    if (mineCount[position] == 0) {
                        queue.addAll(neighbours(position).filter { status[it] == GridStatus.HIDDEN })
                    }
                }
            }
            index++
        }
        updateMapUI()
        return queue
    }

    fun flagAllCanBeFlagged() {
        positions()
            .filter { status[it] == GridStatus.OPENED }
            .filter { mineCount[it] > 0 }
            .flatMap {
                val neighbours = neighbours(it)
                val notOpened = neighbours.count { status[it] == GridStatus.HIDDEN || status[it] == GridStatus.FLAGGED }
                if (mineCount[it] == notOpened) {
                    neighbours.filter { status[it] == GridStatus.HIDDEN }
                } else {
                    emptyList()
                }
            }
            .toSet()
            .forEach { switchStatus(it) }
    }

    private fun startTimer() {
        if (timerJob == null) {
            timerJob = coroutineScope.launch {
                while (true) {
                    delay(1000)
                    timeMillis.value += 1000
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun buildWinInfo(): GameWinInfo {
        if (gameConfig.level == GameConfig.Level.Custom) {
            return GameWinInfo(emptyList(), -1)
        }

        val rank: List<RankItem> = getRank(gameConfig)
        val currentRankItem = RankItem(
            Clock.System.now().toEpochMilliseconds(),
            timeMillis.value.toLong(),
        )
        val newRank = (listOf(currentRankItem) + rank)
            .sortedBy { it.costTimeMillis }
            .take(RECORD_KEEP_COUNT)
        val currentRank = newRank.indexOf(currentRankItem)
        saveRank(gameConfig, newRank)
        return GameWinInfo(newRank, currentRank)
    }

    fun save(): GameSave {
        return GameSave.fromGameInstance(gameConfig, hasMine, status, timeMillis.value)
    }
    
    enum class GridStatus {
        HIDDEN, OPENED, FLAGGED, UNCERTAIN
    }
    
    sealed class MineItemUI {
        object Hidden: MineItemUI()
        object HiddenHover: MineItemUI()
        object Flagged: MineItemUI()
        object Uncertain: MineItemUI()
        object OpenedBoom: MineItemUI()
        object MineView: MineItemUI()
        class Opened(val num: Int): MineItemUI()

        sealed class Animation(val targetUI: MineItemUI): MineItemUI()
        object BlinkAnimation: Animation(Hidden)
    }
    
    class MineMapUI(
        val items: List<MineItemUI>,
        gameConfig: GameConfig,
    ) {
        val width = gameConfig.width
        val height = gameConfig.height

        val hasAnimation = items.any { it is MineItemUI.Animation }

        fun getItemUI(y: Int, x: Int): MineItemUI {
            return items[width * y + x]
        }
    }

    inner class Position(val x: Int, val y: Int) {
        fun isValid(): Boolean {
            return x >= 0 && x < gameConfig.width && y >= 0 && y < gameConfig.height
        }

        fun flattenedIndex(): Int {
            return y * gameConfig.width + x
        }

        fun toOffset(): IntOffset {
            return IntOffset(x, y)
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Position) return false
            return this.x == other.x && this.y == other.y
        }

        override fun hashCode(): Int {
            return flattenedIndex()
        }
    }

    class GameWinInfo(
        val records: List<RankItem>,
        val yourRank: Int,
    )

    companion object {
        private const val RECORD_KEEP_COUNT = 5

        private fun toTimeString(timeMillis: Int): String {
            val timeSeconds = timeMillis / 1000
            val minute = timeSeconds / 60
            val second = timeSeconds % 60
            return if (second < 10) {
                "$minute:0$second"
            } else {
                "$minute:$second"
            }
        }

        fun fromGameSave(
            gameSave: GameSave,
            coroutineScope: CoroutineScope
        ): GameInstance = GameInstance(gameSave.gameConfig, coroutineScope).apply {
            hasMine.fill(false)
            gameSave.mineIndices.forEach { hasMine[it] = true }
            mineCount.fill(0)
            initCountArray()
            status.fill(GridStatus.HIDDEN)
            gameSave.openedIndices.forEach { status[it] = GridStatus.OPENED }
            gameSave.flaggedIndices.forEach { status[it] = GridStatus.FLAGGED }
            timeMillis.value = gameSave.timeMillis
            openedCount.value = gameSave.openedIndices.size
            flaggedCount.value = gameSave.flaggedIndices.size
            updateMapUI()
            startTimer()
        }
    }
}