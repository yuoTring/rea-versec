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

    private var requestCode = 0
    // requestCode = 3

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_style)

        binding = ActivityChooseStyleBinding.inflate(layoutInflater)

        view = binding.root

        setContentView(view)

        val intent = intent

        requestCode = intent.getIntExtra("requestCode",0)

        setClickable(false)

        styleList = resources.getStringArray(R.array.style).toMutableList()
        chosenList = mutableListOf()

        val layoutManager = GridLayoutManager(this, 3)

        adapter = StyleAdapter(this, styleList, chosenList) {

            val style = it!!.substringBefore("__")

            if (chosenList.size > 4) {

                if (!chosenList.contains(style))
                    Toast.makeText(this, "최대 5가지 스타일을 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                else
                    chosenList.remove(style)

            } else {

                if (!chosenList.contains(style))
                    chosenList.add(style)
                else
                    chosenList.remove(style)

                if (chosenList.size>0)
                    setClickable(true)
                else
                    setClickable(false)
            }


            adapter.updateChosenList(chosenList)
            adapter.notifyDataSetChanged()


        }





        binding.recyclerTemplate.layoutManager = layoutManager
        binding.recyclerTemplate.adapter = adapter

        binding.buttonBack.setOnClickListener {
            if (requestCode == AGAIN_STYLE) {

                setResult(CANCEL)
                finish()

            } else {
                finish()
            }
        }

        binding.buttonSet.setOnClickListener {
            val intent = Intent()

            if (chosenList.size>0 && requestCode == 3) {

                intent.putStringArrayListExtra("style", ArrayList(chosenList))
                setResult(300, intent)
                finish()

            } else if (chosenList.size>0 && requestCode == DISCOVERY_STYLE) {

                intent.putStringArrayListExtra("condition", ArrayList(chosenList))
                setResult(300, intent)
                finish()

            } else if (chosenList.size>0 && requestCode == AGAIN_STYLE) {

                intent.putStringArrayListExtra("condition", ArrayList(chosenList))
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

    companion object {

        const val DISCOVERY_STYLE = 500
        const val AGAIN_STYLE = 1000
        private const val CANCEL = -1000

    }
}