package com.versec.versecko.view.story

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.AppContext
import com.versec.versecko.R
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.databinding.FragmentStoryFeedBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.ChoosePlaceActivity
import com.versec.versecko.view.story.adapter.StoryFeedAdapter
import com.versec.versecko.viewmodel.StoryFeedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoryFeedFragment : Fragment() {

    private lateinit var binding : FragmentStoryFeedBinding
    private val viewModel : StoryFeedViewModel by viewModel<StoryFeedViewModel>()

    private val stories : MutableList<StoryEntity?> = mutableListOf()

    private lateinit var adapter: StoryFeedAdapter

    private var lastItemTimestamp : Long = 0

    private var selectedPlace : String? = null
    private var selectedDepth = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        lastItemTimestamp = System.currentTimeMillis()
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentStoryFeedBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.textChoosePlace.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), ChoosePlaceActivity::class.java).putExtra("requestCode", STORY), STORY)
        }

        adapter = StoryFeedAdapter(requireActivity(), stories) { position, type, view ->


            if (type == TYPE_EXIST_LIKE) {


                val story =
                    stories.get(position)!!


                val buttonLike = view as AppCompatButton

                lifecycleScope.launch(Dispatchers.IO) {

                    if (story.likes.contains(AppContext.uid)) {

                        val cancelLikeResponse =
                            viewModel.cancelLike(story.uid)


                        when(cancelLikeResponse) {

                            is Response.Success -> {

                                val story = cancelLikeResponse.data

                                stories.set(position, story)

                                withContext(Dispatchers.Main) {

                                    buttonLike.setBackgroundResource(R.drawable.icon_heart_empty_story)
                                    adapter.changeStoris(stories)
                                    adapter.notifyDataSetChanged()
                                }

                            }
                            is Response.No -> {

                                withContext(Dispatchers.Main) {
                                    Toast.makeText(requireActivity(), "게시글이 삭제되어 해당 작업이 수행되지 않았습니다", Toast.LENGTH_SHORT).show()
                                }
                            }
                            is Response.Error -> {

                                withContext(Dispatchers.Main) {
                                    Toast.makeText(requireActivity(), "인터넷 연결 오류로 인해, 해당 작업이 수행되지 않았습니다", Toast.LENGTH_SHORT).show()
                                }

                            }
                            else -> {

                            }
                        }

                    } else {



                        val likeResponse =
                            viewModel.likeStory(story.uid)


                        when(likeResponse) {

                            is Response.Success -> {

                                val story = likeResponse.data

                                stories.set(position,story)

                                withContext(Dispatchers.Main) {

                                    buttonLike.setBackgroundResource(R.drawable.icon_heart)
                                    adapter.changeStoris(stories)
                                    adapter.notifyDataSetChanged()
                                }

                            }
                            is Response.No -> {

                                withContext(Dispatchers.Main) {
                                    Toast.makeText(requireActivity(), "게시글이 삭제되어 해당 작업이 수행되지 않았습니다", Toast.LENGTH_SHORT).show()
                                }
                            }
                            is Response.Error -> {

                                withContext(Dispatchers.Main) {
                                    Toast.makeText(requireActivity(), "인터넷 연결 오류로 인해, 해당 작업이 수행되지 않았습니다", Toast.LENGTH_SHORT).show()
                                }

                            }
                            else -> {

                            }
                        }

                    }
                }





            } else if (type == TYPE_LOADING_SEEMORE) {


                val textSeeMore = view as TextView

                val jobLoading = lifecycleScope.launch(Dispatchers.Main) {

                    while (true) {

                        textSeeMore.setText(".")
                        delay(500)
                        textSeeMore.setText("..")
                        delay(500)
                        textSeeMore.setText("...")
                    }
                }

                lifecycleScope.launch(Dispatchers.IO) {

                    val fetchedAdditionalStoriesResponse = viewModel.getStories(

                        startTime = lastItemTimestamp,
                        place = selectedPlace,
                        depth = selectedDepth

                    )

                    when(fetchedAdditionalStoriesResponse) {

                        is Response.Success -> {

                            jobLoading.cancel()



                            // no more additional stories in firestore
                            if (fetchedAdditionalStoriesResponse.data.size == 0 ) {

                                withContext(Dispatchers.Main) {
                                    textSeeMore.setText(R.string.text_nomorestory)

                                    delay(3000)
                                    textSeeMore.setText("더보기")
                                }

                            } else {


                                stories.forEach { story ->

                                    if (fetchedAdditionalStoriesResponse.data.contains(story))
                                        fetchedAdditionalStoriesResponse.data.remove(story)

                                }

                                stories.remove(null)
                                stories.addAll(fetchedAdditionalStoriesResponse.data)
                                setLastItemTimestamp(stories)
                                stories.add(null)

                                adapter.changeStoris(stories)

                                withContext(Dispatchers.Main) {

                                    adapter.notifyDataSetChanged()
                                    textSeeMore.setText("더보기")

                                }

                            }






                        }
                        is Response.Error -> {

                        }
                        else -> {

                        }


                    }
                }








            } else if (type == TYPE_REPORT) {

                val builder = AlertDialog.Builder(requireActivity())

                builder.setItems(R.array.story_report, DialogInterface.OnClickListener { dialogInterface, index ->

                    when(index) {
                        0 -> {

                            lifecycleScope.launch(Dispatchers.IO) {


                                val reportStoryResponse = viewModel.reportStory(stories.get(position)!!.uid)

                                when(reportStoryResponse) {

                                    is Response.Success -> {

                                        withContext(Dispatchers.Main) {

                                            stories.remove(stories.get(position))

                                            if (stories.size == 1) {

                                                stories.clear()
                                                binding.layout.visibility = View.VISIBLE

                                            }

                                            adapter.changeStoris(stories)
                                            adapter.notifyDataSetChanged()

                                            Toast.makeText(requireActivity(), "신고가 완료되었습니다!", Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                    is Response.Error -> {

                                        withContext(Dispatchers.Main) {

                                            Toast.makeText(requireActivity(), "인터넷 연결 오류로 인해, 해당 작업이 수행되지 않았습니다", Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                    else -> {

                                    }
                                }

                            }


                        }
                    }

                } ).create().show()

            }

        }

        binding.recyclerStoryFeed.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        binding.recyclerStoryFeed.adapter = adapter


        getStories(System.currentTimeMillis(), null, ONE_DEPTH)


    }

    private fun getStories (timestamp : Long, place : String?, depth : Int) {


        binding.progressBar.show()
        binding.layout.visibility = View.GONE



        lifecycleScope.launch(Dispatchers.IO) {

            val fetchStoriesResponse =
                viewModel.getStories(timestamp, place, depth)

            when(fetchStoriesResponse) {

                is Response.Success -> {



                    stories.clear()


                    if (fetchStoriesResponse.data.size > 0) {

                        stories.addAll(fetchStoriesResponse.data)
                        setLastItemTimestamp(stories)
                        stories.add(null)

                        adapter.changeStoris(stories)

                        withContext(Dispatchers.Main) {

                            binding.layout.visibility = View.INVISIBLE
                        }

                    } else {

                        withContext(Dispatchers.Main) {
                            binding.layout.visibility = View.VISIBLE
                        }

                    }

                    withContext(Dispatchers.Main) {

                        binding.progressBar.hide()
                        adapter.notifyDataSetChanged()
                    }


                }
                is Response.Error -> {

                    Log.d("story-confirm", fetchStoriesResponse.errorMessage)

                }
                else -> {

                }
            }

        }
    }

    private fun setLastItemTimestamp (stories : MutableList<StoryEntity?>) {

        lastItemTimestamp = stories.get(stories.size-1)!!.timestamp
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        lastItemTimestamp = System.currentTimeMillis()

        if (requestCode == STORY && data != null ) {


            selectedPlace =
                data.getStringArrayListExtra("story")?.get(1).toString()

            Log.d("result-code", resultCode.toString())

            if (resultCode == 350) {

                selectedDepth = ONE_DEPTH

                selectedPlace =
                    selectedPlace!!.substringBefore(" 전체")

                getStories(

                    System.currentTimeMillis(),
                    selectedPlace,
                    selectedDepth

                )

            } else if (resultCode == 300) {

                selectedDepth = TWO_DEPTH

                getStories(

                    System.currentTimeMillis(),
                    selectedPlace,
                    selectedDepth
                )
            }

            binding.textChoosePlace.setText(selectedPlace+" "+resources.getString(R.string.emoji_bottomtriangle))
            binding.textChoosePlace.setTextColor(resources.getColor(R.color.charcoal))



        }
    }

    companion object {

        const val STORY = 503
        const val ONE_DEPTH = 1
        const val TWO_DEPTH = 2

        private const val TYPE_LOADING_SEEMORE = 40000

        private const val TYPE_EXIST_LIKE = 40020
        private const val TYPE_REPORT = 40030

        @JvmStatic
        fun newInstance() =
            StoryFeedFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}