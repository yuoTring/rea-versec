package com.versec.versecko.viewmodel

import androidx.lifecycle.ViewModel
import com.versec.versecko.data.repository.UserRepository

class FilterViewModel (

    private val repository: UserRepository

        ) : ViewModel() {




    private fun _setGenderValue (gender : String) {
        repository.setGenderValue(gender)
    }

    fun setGender (gender: String) {
        _setGenderValue(gender)
    }

    private fun _setAgeRange (min : Int, max : Int) {
        repository.setAgeRange(min, max)
    }

    fun setAgeRange (min: Int, max: Int) {
        _setAgeRange(min, max)
    }

    private fun _setDistance (distance : Int) {
        repository.setDistance(distance)
    }

    fun setDistance (distance: Int) {
        _setDistance(distance)
    }






    private fun _getGenderValue () : String? {
        return repository.getGenderValue()
    }

    fun getGender () : String? {
        return _getGenderValue()
    }

    private fun _getAgeRange () : List<Int>? {
        return repository.getAgeRange()
    }

    fun getAgeRange () : List<Int>? {
        return _getAgeRange()
    }

    private fun _getDistance () : Int? {
        return repository.getDistance()
    }

    fun getDistance () : Int? {
        return _getDistance()
    }


}