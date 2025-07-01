package andy.zhu.minesweeper.database

import andy.zhu.minesweeper.Database
import createSqlDriver
import org.koin.dsl.module

val dbModule = module {
    single {
        createSqlDriver()
    }
    single {
        Database(get())
    }
}
