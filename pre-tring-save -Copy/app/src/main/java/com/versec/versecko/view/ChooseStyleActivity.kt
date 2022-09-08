package com.versec.versecko.view

import android.content.Intent
import com.versec.versecko.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        styleList = resources.getStringArray(R.array.style_main).toMutableList()
        chosenList = mutableListOf()

        val layoutManager = LinearLayoutManager(this)

        adapter = StyleAdapter(this, styleList, chosenList) {

            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()

            if (!chosenList.contains(it))
                chosenList.add(it!!)
            else
                chosenList.remove(it)
            //adapter.StyleContentsAdapter().updateStyleChosenList(chosenList)
            //adapter.StyleContentsAdapter()

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
            }
            else
            {
                Toast.makeText(this, "Please select at least one style to proceed!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}