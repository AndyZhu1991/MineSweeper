package andy.zhu.minesweeper

import kotlinx.coroutines.*

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameInstance(
    val gameConfig: GameConfig,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val hasMine = createInitialMines(gameConfig)
    private val mineCount = calcMineCounts()
    private val status = Array<GridStatus>(gameConfig.mapSize()) { GridStatus.HIDDEN }
    private val _mapUIFlow: MutableStateFlow<MineMapUI> = MutableStateFlow(buildMapUI())
    val mapUIFlow: StateFlow<MineMapUI> = _mapUIFlow

    private var timerJob: Job? = null
    private val timeSeconds = MutableStateFlow<Int>(0)
    val timeString: StateFlow<String> = timeSeconds.map(coroutineScope, ::toTimeString)

    private val openedCount = MutableStateFlow(0)
    private val flaggedCount = MutableStateFlow(0)

    private val minesRemaining = flaggedCount.map(coroutineScope) {
        val remaining = gameConfig.mineCount - it
        return@map if (remaining >= 0) remaining else 0
    }

    val minesRemainingText = minesRemaining.map(coroutineScope) { "💣$it" }

    val gameWin = openedCount.map(coroutineScope) { it == gameConfig.mapSize() - gameConfig.mineCount }
    val gameOver = MutableStateFlow(false)

    private val gameEnd = gameWin.combine(coroutineScope, gameOver) { succeed, failed ->
        succeed || failed
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
        coroutineScope.cancel()
    }

    fun onMineTap(position: Position) {
        if (!position.isValid() or gameEnd.value) {
            return
        }
        if (status[position] == GridStatus.HIDDEN) {
            if (openedCount.value == 0) {
                startTimer()
            }
            openGrids(listOf(position))
        }
    }

    fun onMineLongTap(position: Position) {
        if (!position.isValid() or gameEnd.value) {
            return
        }
        switchStatus(position)
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

    private fun indexToPosition(index: Int): Position {
        return Position(index % gameConfig.width, index / gameConfig.width)
    }

    private fun createInitialMines(gameConfig: GameConfig): BooleanArray {
        val mines = BooleanArray(gameConfig.mapSize())
        (0 until gameConfig.mapSize())
                .shuffled()
                .take(gameConfig.mineCount)
                .forEach { mines[it] = true }
        return mines
    }
    
    private fun calcMineCounts(): IntArray {
        val mineCount = IntArray(gameConfig.mapSize())
        positions()
            .filterNot { hasMine[it] }
            .forEach {
                mineCount[it] = calcMineCount(it)
            }
        return mineCount
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
    
    private fun buildMapUI(): MineMapUI {
        val mineUIItems: List<MineItemUI> = MutableList(gameConfig.mapSize()) { index ->
            when(status[index]) {
                GridStatus.HIDDEN -> MineItemUI.Hidden
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
    
    private fun updateMapUI() {
        _mapUIFlow.value = buildMapUI()
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
            if (flagCount == mineCount[position] && hiddens.isNotEmpty()) {
                openGrids(hiddens)
            }
        }
    }

    private fun openGrids(positions: List<Position>): List<Position> {
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

    private fun startTimer() {
        if (timerJob == null) {
            timerJob = coroutineScope.launch {
                while (true) {
                    delay(1000)
                    timeSeconds.value += 1
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }
    
    enum class GridStatus {
        HIDDEN, OPENED, FLAGGED, UNCERTAIN
    }
    
    sealed class MineItemUI {
        object Hidden: MineItemUI()
        object Flagged: MineItemUI()
        object Uncertain: MineItemUI()
        object OpenedBoom: MineItemUI()
        object MineView: MineItemUI()
        class Opened(val num: Int): MineItemUI()
    }
    
    class MineMapUI(
        items: List<MineItemUI>,
        gameConfig: GameConfig,
    ) {
        val width = gameConfig.width
        val height = gameConfig.height

        val enumeratedItems: List<Triple<Int, Int, MineItemUI>> = items.mapIndexed { index, item->
            val y = index / gameConfig.width
            val x = index % gameConfig.width
            Triple(y, x, item)
        }
    }

    inner class Position(val x: Int, val y: Int) {
        fun isValid(): Boolean {
            return x >= 0 && x < gameConfig.width && y >= 0 && y < gameConfig.height
        }

        fun flattenedIndex(): Int {
            return y * gameConfig.width + x
        }
    }

    companion object {
        private fun toTimeString(timeSeconds: Int): String {
            val minute = timeSeconds / 60
            val second = timeSeconds % 60
            return if (second < 10) {
                "$minute:0$second"
            } else {
                "$minute:$second"
            }
        }
    }
}