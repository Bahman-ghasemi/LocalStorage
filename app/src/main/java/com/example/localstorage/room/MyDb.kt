package com.example.localstorage.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [User::class, School::class],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(2, 3, spec = MyDb.migration2To3::class),
        AutoMigration(3, 4, spec = MyDb.migration3To4::class)
    ]
)
abstract class MyDb : RoomDatabase() {
    abstract val dao: UserDao

    @RenameColumn(tableName = "User", fromColumnName = "added", toColumnName = "dataAdded")
    class migration2To3() : AutoMigrationSpec

    @RenameTable(fromTableName = "User", toTableName = "tbl_users")
    class migration3To4():AutoMigrationSpec

    companion object{
        object migtation4to5:Migration(startVersion = 4, endVersion = 5){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE School (name TEXT PRIMARY KEY NOT NULL)")
            }

        }
    }
}