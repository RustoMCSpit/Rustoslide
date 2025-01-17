package ltd.ucode.slide.data.common.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ltd.ucode.slide.data.common.entity.Comment

@Dao
interface CommentDao {
    @Query("SELECT * FROM _comment " +
            "WHERE rowid = :rowId ")
    fun get(rowId: Long): Comment?

    @Query("SELECT * FROM _comment " +
            "WHERE rowid = :rowid ")
    suspend fun query(rowid: Long): Comment?

    @Query("SELECT * FROM _comment AS c " +
            "INNER JOIN _site AS s ON s.rowid = c.site_rowid " +
            "WHERE c.rowid = :commentId AND s.name LIKE :instanceName ")
    fun get(commentId: Int, instanceName: String): List<Comment>

    @Query("SELECT * FROM _comment AS c " +
            "INNER JOIN _site AS s ON s.rowid = c.site_rowid " +
            "WHERE c.rowid = :commentId AND s.name LIKE :instanceName ")
    suspend fun query(commentId: Int, instanceName: String): List<Comment>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(comment: Comment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(comment: Comment)

    @Insert
    suspend fun addAll(comments: List<Comment>)

    @Update
    suspend fun update(comment: Comment)

    @Delete
    suspend fun delete(comment: Comment)
}
