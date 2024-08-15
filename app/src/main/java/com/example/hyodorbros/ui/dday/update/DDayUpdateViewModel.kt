package com.example.hyodorbros.ui.dday.update

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.DDayRepository
import com.example.hyodorbros.ext.ioScope
import com.example.hyodorbros.ext.uiScope
import com.example.hyodorbros.room.entity.DDayEntity
import com.example.hyodorbros.ui.dday.add.DDayAddViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class DDayUpdateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dDayRepository: DDayRepository
) :
    BaseViewModel() {

    val inputDDayLiveData = MutableLiveData("")
    val inputTitleLiveData = MutableLiveData("")
    val inputContentLiveData = MutableLiveData("")
    val isCheckedAddNotiLiveData = MutableLiveData(false)


    private val getEntityUid = savedStateHandle.get<Int>(KEY_ITEM)

    init {
        showGetEntity()
    }

    private fun showGetEntity() {
        getEntityUid?.let { uid ->
            ioScope {
                val dDayEntity = dDayRepository.getEntity(uid)
                dDayEntity?.let { entity ->
                    uiScope {
                        inputDDayLiveData.value = entity.date
                        inputTitleLiveData.value = entity.title
                        inputContentLiveData.value = entity.content
                        isCheckedAddNotiLiveData.value = entity.isNoti
                    }
                }
            }
        }
    }

    fun update() {
        hideInputTextField()
        ioScope {
            val checkTitle = async { checkInputTitle() }.await()

            if (checkTitle) {
                val entity = DDayEntity(
                    uid = getEntityUid!!,
                    date = inputDDayLiveData.value.orEmpty(),
                    title = inputTitleLiveData.value.orEmpty(),
                    content = inputContentLiveData.value.orEmpty(),
                    isNoti = isCheckedAddNotiLiveData.value!!
                )

                if (dDayRepository.updateDDay(entity)) {

                    if(entity.isNoti){
                        viewStateChanged(DDayUpdateViewState.ShowNotification(entity))
                    } else{
                        viewStateChanged(DDayUpdateViewState.HideNotification(entity))
                    }

                    viewStateChanged(DDayUpdateViewState.RouteMain)
                }
            }
        }
    }

    fun showCalendar() {
        hideInputTextField()
        viewStateChanged(DDayUpdateViewState.ShowCalendar)
    }

    private fun hideInputTextField() {
        viewStateChanged(DDayUpdateViewState.HideInputTextFiled)
    }

    private fun checkInputTitle(): Boolean {
        return if (inputTitleLiveData.value.isNullOrEmpty()) {
            viewStateChanged(DDayUpdateViewState.Error("디데이 제목은 꼭 입력하셔야 합니다."))
            false
        } else {
            true
        }
    }

    companion object {
        const val KEY_ITEM = "key_item"
    }
}