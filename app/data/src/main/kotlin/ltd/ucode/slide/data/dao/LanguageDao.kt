package ltd.ucode.slide.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ltd.ucode.slide.data.entity.Language

@Dao
interface LanguageDao {
    @Query("SELECT * FROM languages " +
            "WHERE rowid = :rowId ")
    fun get(rowId: Int): Language

    @Query("SELECT * FROM languages " +
            "WHERE rowid = :rowid ")
    suspend fun query(rowid: Int): Language

    @Query("SELECT * FROM language_images AS li " +
            "INNER JOIN languages AS l ON l.rowid = li.language_rowid " +
            "INNER JOIN sites AS s ON s.rowid = li.site_rowid " +
            "WHERE li.language_id = :languageId AND s.rowid = :siteRowId ")
    fun flow(languageId: Int, siteRowId: Int): Flow<Language>

    @Query("SELECT l.rowid FROM language_images AS li " +
            "INNER JOIN languages AS l ON l.rowid = li.language_rowid " +
            "INNER JOIN sites AS s ON s.rowid = li.site_rowid " +
            "WHERE li.language_id = :languageId AND s.name LIKE :siteName ")
    fun get(languageId: Int, siteName: String): Int

    @Query("SELECT * FROM language_images AS li " +
            "INNER JOIN languages AS l ON l.rowid = li.language_rowid " +
            "INNER JOIN sites AS s ON s.rowid = li.site_rowid " +
            "WHERE li.language_id = :languageId AND s.name LIKE :siteName ")
    suspend fun query(languageId: Int, siteName: String): List<Language>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(language: Language)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(language: Language)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ensureAll(languages: List<Language>)

    @Upsert
    suspend fun upsert(language: Language, image: Language.Image)

    @Update
    suspend fun update(language: Language)

    @Delete
    suspend fun delete(language: Language)
}