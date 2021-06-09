/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ipvc.estg.plantme.ui.camera

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraListViewModel : ViewModel() {

    private val _recognitionList = MutableLiveData<List<Recognition>>()
    val recognitionList: LiveData<List<Recognition>> = _recognitionList

    fun updateData(recognitions: List<Recognition>){
        _recognitionList.postValue(recognitions)
    }

}

data class Recognition(val label:String, val confidence:Float) {

    // Retorno da string que apresenta a label e a probabilidade
    override fun toString():String{
        return "$labelString / $probabilityString"
    }

    @SuppressLint("DefaultLocale")
    fun String.capitalizeWords(): String =
            split(" ").joinToString(" ") { it.toLowerCase().capitalize() }

    // Texto de output, apresentando a probabilidade
    val probabilityString = String.format("%.1f%%", confidence * 100.0f)
    val labelString = processarTextoLabel(label)

    fun processarTextoLabel(labelOriginal: String) : String {
        var str: String = labelOriginal.replace('_', ' ')
        str = str.capitalizeWords()
        return str
    }

}