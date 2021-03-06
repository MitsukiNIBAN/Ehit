package com.mitsuki.ehit.core.model.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mitsuki.armory.httprookie.HttpRookie
import com.mitsuki.armory.httprookie.request.urlParams
import com.mitsuki.armory.httprookie.response.Response
import com.mitsuki.ehit.being.extend.debug
import com.mitsuki.ehit.being.network.Url
import com.mitsuki.ehit.const.RequestKey
import com.mitsuki.ehit.core.crutch.PageIn
import com.mitsuki.ehit.core.model.convert.GalleryListConvert
import com.mitsuki.ehit.core.model.entity.Gallery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GalleryListSource constructor(private val pageIn: PageIn) :
    PagingSource<Int, Gallery>() {

    private val mConvert by lazy { GalleryListConvert() }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gallery> {
        val page = params.key ?: 0
        return try {
            // 如果成功加载，那么返回一个LoadResult.Page,如果失败就返回一个Error
            // Page里传进列表数据，以及上一页和下一页的页数,具体的是否最后一页或者其他逻辑就自行判断
            // 需要注意的是，如果是第一页，prevKey就传null，如果是最后一页那么nextKey也传null
            // 其他情况prevKey就是page-1，nextKey就是page+1
            withContext(Dispatchers.IO) {
                val data: Response<ArrayList<Gallery>> =
                    HttpRookie
                        .get<ArrayList<Gallery>>(pageIn.targetUrl) {
                            convert = mConvert
                            if (page != 0) urlParams(RequestKey.PAGE_LIST to page.toString())
                            pageIn.searchKey?.addParams(this)
                        }
                        .execute()
                when (data) {
                    is Response.Success<ArrayList<Gallery>> -> {
                        val list: ArrayList<Gallery> = data.requireBody()
                        LoadResult.Page(
                            data = list,
                            prevKey = if (page <= 0) null else page - 1,
                            nextKey = if (list.size > 0) page + 1 else null
                        )
                    }
                    is Response.Fail<*> -> throw data.throwable
                }
            }
        } catch (inner: Throwable) {
            // 捕获异常，返回一个Error
            LoadResult.Error(inner)
        }
    }

    override val jumpingSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, Gallery>): Int = pageIn.targetPage
}