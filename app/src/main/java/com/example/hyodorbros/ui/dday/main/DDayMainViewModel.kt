package com.example.hyodorbros.ui.dday.main

import androidx.lifecycle.viewModelScope
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.DDayRepository
import com.example.hyodorbros.ext.ioScope
import com.example.hyodorbros.room.entity.DDayEntity
import com.example.hyodorbros.ui.home.HomeViewState
import com.example.hyodorbros.util.Result
import com.example.hyodorbros.util.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DDayMainViewModel @Inject constructor(private val dDayRepository: DDayRepository) :
    BaseViewModel() {


    private val getDDayEntityListStream: Flow<Result<List<DDayEntity>>> =
        dDayRepository.dDayEntityList.asResult()


    private val getTotalDDayEntityCountStream: Flow<Result<Int>> =
        dDayRepository.totalDDayCount.asResult()

    val uiStateStream: StateFlow<DDayMainUiStream> =
        getDDayEntityListStream.zip(getTotalDDayEntityCountStream) { entityListResult, totalCountResult ->
            val totalCount = if (totalCountResult is Result.Success) {
                totalCountResult.data
            } else {
                0
            }

            val entityList = if (entityListResult is Result.Success) {
                entityListResult.data
            } else {
                emptyList()
            }
            DDayMainUiStream(totalCount, entityList)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DDayMainUiStream()
        )

    fun routeAdd() {
        viewStateChanged(DDayMainViewState.RouteAdd)
    }

    fun delete(item: DDayEntity) {
        ioScope {
            dDayRepository.deleteDDay(item)
        }
    }

    fun update(item: DDayEntity) {
        viewStateChanged(DDayMainViewState.RouteUpdate(item))
    }

    fun toggleNotification(item : DDayEntity){
        ioScope {
            val toggleItem = item.copy(isNoti = !item.isNoti)

            if(dDayRepository.updateDDay(toggleItem)){
                if(toggleItem.isNoti){
                    viewStateChanged(DDayMainViewState.ShowNotification(toggleItem))
                }else{
                    viewStateChanged(DDayMainViewState.HideNotification(toggleItem))
                }
            }
        }
    }

}

data class DDayMainUiStream(
    val totalCount: Int? = null,
    val list: List<DDayEntity>? = null
)
