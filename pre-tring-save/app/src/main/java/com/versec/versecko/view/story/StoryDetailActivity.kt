package com.versec.versecko.view.story

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.versec.versecko.AppContext
import com.versec.versecko.R
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.databinding.ActivityStoryDetailBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.profile.adapter.ViewPagerAdapter
import com.versec.versecko.viewmodel.StoryDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStoryDetailBinding
    private lateinit var story : StoryEntity
    private lateinit var adapter : ViewPagerAdapter

    private val viewModel : StoryDetailViewModel by viewModel<StoryDetailViewModel>()


    companion object {
        private const val STORY_DETAIL = 255
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        story = intent.getSerializableExtra("story") as StoryEntity




        setButton()

        init(story)

        lifecycleScope.launch(Dispatchers.IO) {

            val getOwnStoryResponse = viewModel.getOwnStory(story.uid)

            when(getOwnStoryResponse) {

                is Response.Success -> {

                    withContext(Dispatchers.Main) {
                        init(getOwnStoryResponse.data)
                    }

                }
                is Response.Error -> {

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@StoryDetailActivity, "인터넷 연결 오류로 인해, 해당 게시글이 업데이트되지 않았습니다.", Toast.LENGTH_SHORT).show()
                    }

                }
                else -> {

                }
            }
        }

        binding.buttonLike.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {

                if (!story.likes.contains(AppContext.uid)) {

                    val likeResponse =
                        viewModel.likeStory(story.uid)

                    when(likeResponse) {

                        is Response.Success -> {

                            story = likeResponse.data

                            withContext(Dispatchers.Main) {

                                binding.buttonLike.setBackgroundResource(R.drawable.icon_heart_story)

                                binding.textLikeCount.visibility = View.VISIBLE
                                binding.textLikes.visibility = View.VISIBLE

                                binding.textLikeCount.setText(story.likes.size.toString())
                                binding.textLikes.setText(resources.getString(R.string.HOWMANYLIKE_MORETHAN0))
                            }


                        }
                        is Response.Error -> {

                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@StoryDetailActivity, "인터넷 연결 오류로 인해, \n 해당 작업이 수행되지 않았습니다", Toast.LENGTH_SHORT).show()
                            }

                        }
                        else -> {

                        }

                    }

                } else {

                    val cancelLikeResponse =
                        viewModel.cancelLike(story.uid)

                    when(cancelLikeResponse) {

                        is Response.Success -> {

                            withContext(Dispatchers.Main) {

                                binding.buttonLike.setBackgroundResource(R.drawable.icon_heart_empty_story)

                                story = cancelLikeResponse.data

                                if (story.likes.size == 0) {

                                    binding.textLikeCount.visibility = View.GONE
                                    binding.textLikes.visibility = View.GONE

                                } else {

                                    binding.textLikeCount.visibility = View.VISIBLE
                                    binding.textLikes.visibility = View.VISIBLE
                                    binding.textLikeCount.setText(story.likes.size.toString())
                                    binding.textLikes.setText(resources.getString(R.string.HOWMANYLIKE_MORETHAN0))

                                }

                            }

                        }
                        is Response.Error -> {

                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@StoryDetailActivity, "인터넷 연결 오류로 인해, \n 해당 작업이 수행되지 않았습니다", Toast.LENGTH_SHORT).show()

                            }
                        }
                        else -> {

                        }

                    }

                }



            }
        }


        binding.buttonMore.setOnClickListener {

            val builder = AlertDialog.Builder(this)

            builder.setItems(R.array.story_more, DialogInterface.OnClickListener { dialogInterface, index ->

                when(index) {
                    0 ->  deleteStory()
                }

            } ).create().show()
        }

    }

    private fun init (story : StoryEntity ) {


        val uriList = mutableListOf<String>()

        story.uriMap.forEach { entry ->  uriList.add(entry.key.toInt(), entry.value) }

        adapter = ViewPagerAdapter(uriList)

        binding.viewpagerStoryImage.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpagerStoryImage) { tab, position ->

        }.attach()

        binding.textNickName.setText(story.userNickName)
        binding.textStoryContents.setText(story.contents)
        binding.textStoryLocaton.setText(story.subLocation+", "+story.mainLocation)

        binding.textLikes.setText(resources.getString(R.string.HOWMANYLIKE_MORETHAN0))

        if (story.likes.size > 0 ){
            binding.textLikes.visibility = View.VISIBLE
            binding.textLikeCount.visibility = View.VISIBLE
            binding.textLikeCount.setText(story.likes.size.toString())

            if (story.likes.contains(AppContext.uid))
                binding.buttonLike.setBackgroundResource(R.drawable.icon_heart_story)
            else
                binding.buttonLike.setBackgroundResource(R.drawable.icon_heart_empty_story)

        } else  {

            binding.textLikes.visibility = View.GONE
            binding.textLikeCount.visibility = View.GONE
            binding.buttonLike.setBackgroundResource(R.drawable.icon_heart_empty_story)
        }

        binding.textTime.setText( SimpleDateFormat("yyyy년 MM월 dd일").format(Date(story.timestamp)) )


    }

    private fun deleteStory () {

        lifecycleScope.launch(Dispatchers.IO) {


            val deleteStory =
                viewModel.deleteStory(story.uid, story.uriMap.size)

            when (deleteStory) {

                is Response.Success -> {

                    setResult(STORY_DETAIL)
                    finish()
                }
                is Response.Error -> {

                }
                else -> {

                }
            }

        }

    }

    private fun setButton () {

        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonMore.setOnClickListener {


        }
    }

}