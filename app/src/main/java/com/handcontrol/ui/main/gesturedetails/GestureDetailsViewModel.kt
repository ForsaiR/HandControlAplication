package com.handcontrol.ui.main.gesturedetails

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.handcontrol.api.Api
import com.handcontrol.model.Action
import com.handcontrol.model.Gesture
import com.handcontrol.repository.GestureRepository
import com.handcontrol.server.protobuf.Gestures
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Time
import java.sql.Timestamp
import java.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.seconds


class GestureDetailsViewModel(var item: Gesture?) : ViewModel() {
    val errorConnection = MutableLiveData(false)

    val isInfinity = MutableLiveData(item?.isInfinityRepeat ?: false)
    val repeatCount = MutableLiveData(item?.repeatCount?.toString() ?: "")

    var playedPosition: Int? = null
    var repeatCountInt = item?.repeatCount ?: 1

    private val infinityObserver =
        Observer<Boolean> { v ->
            if (v) repeatCount.value = "∞" else repeatCount.value = repeatCountInt.toString()
        }

    private val repeatObserver =
        Observer<String> { v ->
            if (isInfinity.value != true) repeatCountInt = v.toIntOrNull() ?: 0
        }

    init {
        isInfinity.observeForever(infinityObserver)
        repeatCount.observeForever(repeatObserver)
        if (item == null) {
            item = Gesture(null, "", false, true, null, mutableListOf())
        }
        GestureRepository.initGesture(item)
    }

    val id = item?.id
    val name = MutableLiveData(item?.name ?: "")
    val actions = MutableLiveData(item?.actions ?: mutableListOf())

    fun saveGesture() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val gest = Gestures.Gesture.newBuilder()
                    .setName(name.value)
                    .setLastTimeSync(System.currentTimeMillis()/1000)
                    .setIterable(isInfinity.value!!)
                    .addAllActions(item?.actions?.map { it.getProtoModel() })

                if (id != null) {
                    gest.id = id
                }

                if (repeatCount.value.toString() != "∞") {
                    gest.repetitions = repeatCount.value!!.toInt()
                }

                Api.getApiHandler().saveGesture( Gestures.SaveGesture.newBuilder()
                    .setGesture(gest.build())
                    .setTimeSync(System.currentTimeMillis()/1000)
                    .build()
                )
                errorConnection.postValue(false)
            } catch (e: StatusRuntimeException) {
                e.printStackTrace()
                errorConnection.postValue(true)
            }
        }
    }

    fun setPositions(action: Action) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Api.getApiHandler().setPositions(Gestures.SetPositions.newBuilder()
                    .setThumbFingerPosition(action.thumbFinger)
                    .setLittleFingerPosition(action.littleFinger)
                    .setMiddleFingerPosition(action.middleFinger)
                    .setPointerFingerPosition(action.pointerFinger)
                    .setRingFingerPosition(action.ringFinger)
                    .build())
                errorConnection.postValue(false)
            } catch (e: StatusRuntimeException) {
                e.printStackTrace()
                errorConnection.postValue(true)
            }
        }
    }

    fun deleteAction(action: Action) {
        actions.value!!.remove(action)
        GestureRepository.deleteAction(action)
    }

    override fun onCleared() {
        isInfinity.removeObserver(infinityObserver)
        repeatCount.removeObserver(repeatObserver)
        super.onCleared()
    }
}

class GestureDetailsViewModelFactory(
    private val gesture: Gesture?
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GestureDetailsViewModel(gesture) as T
    }
}
