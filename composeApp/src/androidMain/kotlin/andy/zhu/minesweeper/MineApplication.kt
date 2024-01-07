package andy.zhu.minesweeper

import android.app.Application

class MineApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MineApplication
            private set
    }
}