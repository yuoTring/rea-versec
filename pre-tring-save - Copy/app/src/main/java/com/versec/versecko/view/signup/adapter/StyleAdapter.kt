package com.versec.versecko.view.signup.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.databinding.ItemRecyclerStyleBinding
import com.versec.versecko.databinding.ItemRecyclerStyleContentsBinding

class StyleAdapter (

    private val activity : Activity,
    private val styleList: MutableList<String>,
    private var chosenList: MutableList<String>,

    private val onClick: (String?) -> Unit


) : RecyclerView.Adapter<StyleAdapter.ViewHolder>()
{
    private lateinit var adapter: StyleContentsAdapter

    inner class ViewHolder (
        val binding : ItemRecyclerStyleBinding,
        //val styleContentsAdapter: StyleContentsAdapter

    ) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind (style : String?, onClick: (String?) -> Unit) {
            onClick(style)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemRecyclerStyleBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        holder.binding.recyclerStyleContent
        holder.binding.textStyleTitle.setText(styleList.get(position))

        //val context = AppContext.getInstance()

        //val resourceId = context.resources.getIdentifier("", "array",context.packageName )
        val resourceId = activity.resources.getIdentifier(styleList.get(position), "array", activity.packageName)
        val styleContentsList = activity.resources.getStringArray(resourceId).toMutableList()

        //val adapter
        adapter = StyleContentsAdapter(styleContentsList, chosenList) {

            //Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            holder.bind(it, onClick)
            adapter.updateStyleChosenList(chosenList)
            adapter.notifyDataSetChanged()

        }


        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        holder.binding.recyclerStyleContent.layoutManager = layoutManager
        holder.binding.recyclerStyleContent.adapter = adapter

    }

    override fun getItemCount(): Int {

        return styleList.size
    }

    fun updateChosenList ( chosenList: MutableList<String>) {
        this.chosenList = chosenList

    }
















    inner class StyleContentsAdapter (

        private val styleContentsList : MutableList<String>,
        private var chosenStyleContentsList: MutableList<String>,
        private val onStyleContentsClick : (String?) -> Unit

    ) : RecyclerView.Adapter<StyleContentsAdapter.ContentsViewHolder>() {

        inner class ContentsViewHolder (val binding: ItemRecyclerStyleContentsBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind (styleContents : String?, onClick: (String?) -> Unit) {
                binding.root.setOnClickListener {
                    onClick(styleContents)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsViewHolder {

            val binding = ItemRecyclerStyleContentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return ContentsViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ContentsViewHolder, position: Int) {

            //val context = AppContext.getInstance()
            val resourceId = activity.resources.getIdentifier(styleContentsList.get(position), "drawable", activity.packageName)
            holder.binding.imageStyle.setImageDrawable(ContextCompat.getDrawable(activity, resourceId))
            holder.binding.textStyle.setText(styleContentsList.get(position))

            if (chosenStyleContentsList.contains(styleContentsList.get(position)))
                holder.binding.textStyle.setText("chosen!!!")

            holder.bind(styleContentsList.get(position), onStyleContentsClick)


        }

        override fun getItemCount(): Int { return styleContentsList.size }

        fun updateStyleChosenList ( chosenStyleContentsList: MutableList<String>) {
            this.chosenStyleContentsList = chosenStyleContentsList
        }

    }
}