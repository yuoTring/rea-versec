package com.versec.versecko.view

import android.content.Intent
import com.versec.versecko.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.versec.versecko.databinding.ActivityChooseStyleBinding
import com.versec.versecko.view.signup.adapter.StyleAdapter

class ChooseStyleActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityChooseStyleBinding
    private lateinit var view : View
    private lateinit var adapter : StyleAdapter

    private lateinit var styleList : MutableList<String>
    private lateinit var chosenList : MutableList<String>

    // requestCode = 3

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_style)

        binding = ActivityChooseStyleBinding.inflate(layoutInflater)

        view = binding.root

        setContentView(view)

        setClickable(false)

        styleList = resources.getStringArray(R.array.style).toMutableList()
        chosenList = mutableListOf()

        val layoutManager = GridLayoutManager(this, 3)

        adapter = StyleAdapter(this, styleList, chosenList) {

            if (chosenList.size > 4) {

                if (!chosenList.contains(it))
                    Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()
                else
                    chosenList.remove(it)

            } else {

                if (!chosenList.contains(it))
                    chosenList.add(it!!)
                else
                    chosenList.remove(it)
            }


            adapter.updateChosenList(chosenList)
            adapter.notifyDataSetChanged()


        }





        binding.recyclerTemplate.layoutManager = layoutManager
        binding.recyclerTemplate.adapter = adapter

        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonSet.setOnClickListener {
            val intent = Intent()

            if (chosenList.size>0) {
                intent.putStringArrayListExtra("style", ArrayList(chosenList))
                setResult(300, intent)
                finish()
            }

        }

    }

    private fun setClickable (clickable : Boolean) {
        if (clickable) {
            binding.buttonSet.setBackgroundResource(R.color.blue)
            binding.buttonSet.isClickable = true
        } else {
            binding.buttonSet.setBackgroundResource(R.color.gray_superlight)
            binding.buttonSet.isClickable = false
        }
    }
}