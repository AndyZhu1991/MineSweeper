package andy.zhu.minesweeper

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameInstance(
    val gameConfig: GameConfig,
) {
    private val hasMine = createInitialMines(gameConfig)
    private val mineCount = calcMineCounts()
    private val status = Array<GridStatus>(gameConfig.mapSize()) { GridStatus.HIDDEN }
    private val _mapUIFlow: MutableStateFlow<MineMapUI> = MutableStateFlow(buildMapUI())
    val mapUIFlow: StateFlow<MineMapUI> = _mapUIFlow
    
//    init {
//        positions().forEach { (y, x) ->
//            if ((x+y) % 2 == 0) {
//                status[y * gameConfig.width + x] = GridStatus.OPENED
//            }
//        }
//        updateMapUI()
//    }
    
    private fun to1dIndex(y: Int, x: Int) = y * gameConfig.width + x
    
    private fun to2dIndex(index: Int): Pair<Int, Int> {
        return index / gameConfig.width to index % gameConfig.width
    }
    
    private fun isValidPosition(y: Int, x: Int): Boolean {
        return y >= 0 && y < gameConfig.height && x >= 0 && x < gameConfig.width
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
            .filterNot { hasMine(it.first, it.second) }
            .forEach { (y, x) ->
                mineCount[to1dIndex(y, x)] = calcMineCount(y, x)
            }
        return mineCount
    }
    
    private fun neighbours(y: Int, x: Int): List<Pair<Int, Int>> {
        return listOf(
            y - 1 to x - 1,
            y - 1 to x    ,
            y - 1 to x + 1,
            y     to x - 1,
            y     to x + 1,
            y + 1 to x - 1,
            y + 1 to x    ,
            y + 1 to x + 1,
            )
            .filter { isValidPosition(it.first, it.second) }
    }
    
    private fun calcMineCount(y: Int, x: Int): Int {
        return neighbours(y, x).count { hasMine(it.first, it.second) }
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
    
    fun positions(): Sequence<Pair<Int, Int>> {
        return (0 until gameConfig.mapSize())
            .asSequence()
            .map(::to2dIndex)
    }
    
    fun hasMine(y: Int, x: Int): Boolean {
        return hasMine[to1dIndex(y, x)]
    }
    
    fun mineCount(y: Int, x: Int): Int {
        return mineCount[to1dIndex(y, x)]
    }
    
    fun status(y: Int, x: Int): GridStatus {
        return status[to1dIndex(y, x)]
    }
    
    private fun setStatus(y: Int, x: Int, status: GridStatus) {
        this.status[to1dIndex(y, x)] = status
    }
    
    fun onMineTap(y: Int, x: Int) {
        if (!isValidPosition(y, x)) {
            return
        }
        if (status(y, x) == GridStatus.HIDDEN) {
            openGrid(y, x)
            updateMapUI()
        }
    }
    
    fun onMineLongTap(y: Int, x: Int) {
        switchStatus(y, x)
    }
    
    fun onMineRightClick(y: Int, x: Int) {
        switchStatus(y, x)
    }
    
    fun onMineMiddleClick(y: Int, x: Int) {
        tryOpenNeighbours(y, x)
    }
    
    private fun switchStatus(y: Int, x: Int) {
        val newStatus = when(status(y, x)) {
            GridStatus.HIDDEN -> GridStatus.FLAGGED
            GridStatus.OPENED -> GridStatus.OPENED
            GridStatus.FLAGGED -> GridStatus.UNCERTAIN
            GridStatus.UNCERTAIN -> GridStatus.HIDDEN
        }
        setStatus(y, x, newStatus)
        updateMapUI()
    }
    
    private fun tryOpenNeighbours(y: Int, x: Int) {
        if (status(y, x) == GridStatus.OPENED && mineCount(y, x) > 0) {
            val neighbours = neighbours(y, x)
            val flagCount = neighbours.count { status(it.first, it.second) == GridStatus.FLAGGED }
            val hiddens = neighbours.filter { status(it.first, it.second) == GridStatus.HIDDEN }
            if (flagCount == mineCount(y, x) && hiddens.isNotEmpty()) {
                hiddens.forEach { (y, x) ->
                    openGrid(y, x)
                }
                updateMapUI()
            }
        }
    }
    
    private fun openGrid(y: Int, x: Int) {
        setStatus(y, x, GridStatus.OPENED)
        if (hasMine(y, x)) {
            // Game Over
        } else {
            if (mineCount(y, x) == 0) {
                neighbours(y, x)
                    .filter { status(it.first, it.second) == GridStatus.HIDDEN }
                    .forEach { (y, x) ->
                        openGrid(y, x)
                    }
            }
        }
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
            val x = index % gameConfig.height
            Triple(y, x, item)
        }
    }
}