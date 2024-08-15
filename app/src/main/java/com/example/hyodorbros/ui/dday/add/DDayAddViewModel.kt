package com.example.hyodorbros.ui.dday.add

import androidx.lifecycle.MutableLiveData
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.DDayRepository
import com.example.hyodorbros.ext.currentTimeString
import com.example.hyodorbros.ext.ioScope
import com.example.hyodorbros.room.entity.DDayEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class DDayAddViewModel @Inject constructor(private val dDayRepository: DDayRepository) :
    BaseViewModel() {

    val inputDDayLiveData = MutableLiveData(currentTimeString())
    val inputTitleLiveData = MutableLiveData("")
    val inputContentLiveData = MutableLiveData("")
    val isCheckedAddNotiLiveData = MutableLiveData(false)

    fun add() {
        hideInputTextField()
        ioScope {
            val checkTitle = async { checkInputTitle() }.await()

            if (checkTitle) {

                val entity = DDayEntity(
                    date = inputDDayLiveData.value.orEmpty(),
                    title = inputTitleLiveData.value.orEmpty(),
                    content = inputContentLiveData.value.orEmpty(),
                    isNoti = isCheckedAddNotiLiveData.value!!
                )

                if (dDayRepository.insertDDay(entity)) {
                    viewStateChanged(DDayAddViewState.RouteMain)
                }
            }
        }
    }

    fun showCalendar() {
        hideInputTextField()
        viewStateChanged(DDayAddViewState.ShowCalendar)
    }

    private fun hideInputTextField() {
        viewStateChanged(DDayAddViewState.HideInputTextFiled)
    }


    private fun checkInputTitle(): Boolean {
        return if (inputTitleLiveData.value.isNullOrEmpty()) {
            viewStateChanged(DDayAddViewState.Error("디데이 제목은 꼭 입력하셔야 합니다."))
            false
        } else {
            true
        }
    }
}