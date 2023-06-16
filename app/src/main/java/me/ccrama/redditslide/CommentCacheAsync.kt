package me.ccrama.redditslide

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.fasterxml.jackson.databind.JsonNode
import ltd.ucode.slide.App
import ltd.ucode.slide.Authentication
import ltd.ucode.slide.R
import ltd.ucode.slide.SettingValues
import ltd.ucode.slide.SettingValues.getCommentSorting
import me.ccrama.redditslide.util.GifUtils
import me.ccrama.redditslide.util.LogUtil
import me.ccrama.redditslide.util.PhotoLoader
import net.dean.jraw.http.NetworkException
import net.dean.jraw.http.SubmissionRequest
import net.dean.jraw.models.CommentSort
import net.dean.jraw.models.Submission
import net.dean.jraw.models.meta.SubmissionSerializer
import net.dean.jraw.paginators.SubredditPaginator
import net.dean.jraw.util.JrawUtils

/**
 * Created by carlo_000 on 4/18/2016.
 */
class CommentCacheAsync : AsyncTask<Any?, Any?, Any?> {
    var alreadyReceived: List<Submission>? = null
    var mNotifyManager: NotificationManager? = null

    constructor(
        submissions: List<Submission>?, c: Context, subreddit: String,
        otherChoices: BooleanArray
    ) {
        alreadyReceived = submissions
        context = c
        subs = arrayOf(subreddit)
        this.otherChoices = otherChoices
    }

    constructor(
        submissions: List<Submission>?, mContext: Activity, baseSub: String,
        alternateSubName: String?
    ) : this(submissions, mContext, baseSub, booleanArrayOf(true, true)) {
    }

    constructor(c: Context, subreddits: Array<String>) {
        context = c
        subs = subreddits
    }

    var subs: Array<String>
    var context: Context
    var mBuilder: NotificationCompat.Builder? = null
    var otherChoices: BooleanArray = emptyArray<Boolean>().toBooleanArray()
    public override fun doInBackground(params: Array<Any?>): Void? {
        if (Authentication.isLoggedIn && Authentication.me == null || Authentication.reddit == null) {
            if (Authentication.reddit == null) {
                Authentication(context)
            }
            if (Authentication.reddit != null) {
                try {
                    Authentication.me = Authentication.reddit!!.me()
                    Authentication.mod = Authentication.me!!.isMod()
                    SettingValues.authentication.edit()
                        .putBoolean(App.SHARED_PREF_IS_MOD, Authentication.mod)
                        .apply()
                    val name = Authentication.me!!.getFullName()
                    Authentication.name = name
                    LogUtil.v("AUTHENTICATED")
                    if (Authentication.reddit!!.isAuthenticated) {
                        val accounts = SettingValues.authentication.getStringSet("accounts", HashSet())
                        if (accounts!!.contains(name)) { //convert to new system
                            accounts.remove(name)
                            accounts.add(name + ":" + Authentication.refresh)
                            SettingValues.authentication.edit().putStringSet("accounts", accounts)
                                .apply() //force commit
                        }
                        Authentication.isLoggedIn = true
                        App.notFirst = true
                    }
                } catch (e: Exception) {
                    Authentication(context)
                }
            }
        }
        val multiNameToSubsMap = UserSubscriptions.getMultiNameToSubs(true)
        if (Authentication.reddit == null) App.authentication = Authentication(context)
        val success = ArrayList<String?>()
        for (fSub in subs) {
            val sub: String?
            val sortType = getCommentSorting(fSub)
            sub = if (multiNameToSubsMap.containsKey(fSub)) {
                multiNameToSubsMap[fSub]
            } else {
                fSub
            }
            if (!sub!!.isEmpty()) {
                if (sub != SAVED_SUBMISSIONS) {
                    mNotifyManager =
                        ContextCompat.getSystemService(context, NotificationManager::class.java)
                    mBuilder = NotificationCompat.Builder(context, App.CHANNEL_COMMENT_CACHE)
                    mBuilder!!.setOngoing(true)
                    mBuilder!!.setContentTitle(
                        context.getString(
                            R.string.offline_caching_title,
                            if (sub.equals(
                                    "frontpage",
                                    ignoreCase = true
                                )
                            ) fSub else if (fSub.contains("/m/")) fSub else "/r/$fSub"
                        )
                    )
                        .setSmallIcon(R.drawable.ic_save)
                }
                val submissions: MutableList<Submission> = ArrayList()
                val newFullnames = ArrayList<String>()
                var count = 0
                if (alreadyReceived != null) {
                    submissions.addAll(alreadyReceived!!)
                } else {
                    var p: SubredditPaginator
                    p = if (fSub.equals("frontpage", ignoreCase = true)) {
                        SubredditPaginator(Authentication.reddit)
                    } else {
                        SubredditPaginator(Authentication.reddit, sub)
                    }
                    p.setLimit(Constants.PAGINATOR_POST_LIMIT)
                    try {
                        submissions.addAll(p.next())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                val commentDepth = SettingValues.commentDepth ?: 5
                val commentCount = SettingValues.commentCount ?: 50
                Log.v("CommentCacheAsync", "comment count $commentCount")
                val random = (Math.random() * 100).toInt()
                for (s in submissions) {
                    try {
                        val n = getSubmission(
                            SubmissionRequest.Builder(s.id).limit(commentCount)
                                .depth(commentDepth)
                                .sort(sortType)
                                .build()
                        )
                        val s2 = SubmissionSerializer.withComments(n, CommentSort.CONFIDENCE)
                        OfflineSubreddit.writeSubmission(n, s2, context)
                        newFullnames.add(s2.fullName)
                        if (!SettingValues.noImages) PhotoLoader.loadPhoto(context, s)
                        when (ContentType.getContentType(s)) {
                            ContentType.Type.VREDDIT_DIRECT, ContentType.Type.VREDDIT_REDIRECT, ContentType.Type.GIF -> if (otherChoices[0]) {
                                if (context is Activity) {
                                    (context as Activity).runOnUiThread {
                                        GifUtils.cacheSaveGif(
                                            Uri.parse(GifUtils.AsyncLoadGif.formatUrl(s.url)),
                                            context as Activity,
                                            s.subredditName,
                                            null,
                                            false
                                        )
                                    }
                                }
                            }

                            ContentType.Type.ALBUM -> if (otherChoices[1]) //todo this AlbumUtils.saveAlbumToCache(context, s.getUrl());
                            {
                                break
                            }

                            else -> {}
                        }
                    } catch (ignored: Exception) {
                    }
                    count = count + 1
                    if (mBuilder != null) {
                        mBuilder!!.setProgress(submissions.size, count, false)
                        mNotifyManager!!.notify(random, mBuilder!!.build())
                    }
                }
                OfflineSubreddit.newSubreddit(sub).writeToMemory(newFullnames)
                if (mBuilder != null) {
                    mNotifyManager!!.cancel(random)
                }
                if (!submissions.isEmpty()) success.add(sub)
            }
        }
        if (mBuilder != null) {
            mBuilder!!.setContentText(context.getString(R.string.offline_caching_complete)) // Removes the progress bar
                .setSubText(success.size.toString() + " subreddits cached").setProgress(0, 0, false)
            mBuilder!!.setOngoing(false)
            mNotifyManager!!.notify(2001, mBuilder!!.build())
        }
        return null
    }

    @Throws(NetworkException::class)
    fun getSubmission(request: SubmissionRequest): JsonNode? {
        val args: MutableMap<String, String> = HashMap()
        if (request.depth != null) args["depth"] = Integer.toString(request.depth)
        if (request.context != null) {
            args["context"] = Integer.toString(request.context)
        }
        if (request.limit != null) args["limit"] = Integer.toString(request.limit)
        if (request.focus != null && !JrawUtils.isFullname(request.focus)) {
            args["comment"] = request.focus
        }
        var sort = request.sort
        if (sort == null) // Reddit sorts by confidence by default
        {
            sort = CommentSort.CONFIDENCE
        }
        args["sort"] = sort.name.lowercase()
        return try {
            val response = Authentication.reddit!!.execute(
                Authentication.reddit!!.request()
                    .path(String.format("/comments/%s", request.id))
                    .query(args)
                    .build()
            )
            response.json
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val SAVED_SUBMISSIONS = "read later"
    }
}
