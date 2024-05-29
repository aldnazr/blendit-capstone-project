package com.android.blendit.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.blendit.ui.analysis.AnalysisViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return when {
//            modelClass.isAssignableFrom(AnalysisViewModel::class.java) -> {
//                AnalysisViewModel(pref) as T
//            }
//
//            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//        }
//    }
}