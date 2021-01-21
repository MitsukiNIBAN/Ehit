package com.mitsuki.ehit.core.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.mitsuki.ehit.being.db.RoomData
import com.mitsuki.ehit.core.model.entity.QuickSearch
import com.mitsuki.ehit.core.model.entity.SearchHistory
import com.mitsuki.ehit.core.model.repository.RemoteRepository
import com.mitsuki.ehit.core.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SearchViewModel @ViewModelInject constructor(@RemoteRepository var repository: Repository) :
    ViewModel() {

    @Suppress("PrivatePropertyName")
    private val HISTORY_COUNT = 3


    suspend fun searchHistory(): Flow<List<SearchHistory>> =
        withContext(Dispatchers.IO) { RoomData.searchDao.queryHistory(HISTORY_COUNT) }

    suspend fun quickSearch(): Flow<List<QuickSearch>> =
        withContext(Dispatchers.IO) { RoomData.searchDao.queryQuick() }

    suspend fun saveSearch(text: String) = withContext(Dispatchers.IO) {
        RoomData.searchDao.insertHistory(SearchHistory(text, System.currentTimeMillis()))
    }


}