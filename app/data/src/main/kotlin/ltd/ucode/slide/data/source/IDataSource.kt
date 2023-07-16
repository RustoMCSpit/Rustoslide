package ltd.ucode.slide.data.source

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ltd.ucode.slide.data.auth.Credential
import ltd.ucode.slide.data.entity.Post
import ltd.ucode.slide.data.entity.Site
import ltd.ucode.slide.data.value.Feed
import ltd.ucode.slide.data.value.Period
import ltd.ucode.slide.data.value.Sorting

interface IDataSource {
    suspend fun login(username: String, domain: String)
    suspend fun login(username: String, domain: String, credential: Credential)

    fun getSite(rowId: Int): Flow<Site>
    fun getSite(domain: String): Flow<Site>

    fun getSites(): Flow<List<Site>>
    fun getSites(software: String): Flow<List<Site>>

    fun getPost(rowId: Int): Flow<Post>
    fun getPost(domain: String, key: Int): Flow<Post>

    fun getPosts(domain: String, feed: Feed, period: Period, order: Sorting): Flow<PagingData<Post>>
}