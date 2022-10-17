package com.versec.versecko.view.matching

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.AppContext
import com.versec.versecko.FilterActivity
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.FragmentMatchingBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.matching.adapter.CardStackAdapter
import com.versec.versecko.viewmodel.MainViewModel
import com.versec.versecko.viewmodel.MatchingViewModel
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList
import kotlin.properties.Delegates

class MatchingFragment : Fragment(), CardStackListener {

    private lateinit var binding : FragmentMatchingBinding
    private val viewModel : MatchingViewModel by viewModel<MatchingViewModel>()
    private val mainViewModel : MainViewModel by viewModel<MainViewModel>()

    private lateinit var cardStackLayoutManager: CardStackLayoutManager
    private lateinit var cardStackAdapter: CardStackAdapter

    private lateinit var ownUser : UserEntity
    private lateinit var otherUserList : MutableList<UserEntity>

    private var currentPosition = 0
    private var dragged = false
    private var hidden = false
    private var whichFrag = 0

    private lateinit var response: Response<MutableList<UserEntity>>

    private val conditionList : MutableList<String> = mutableListOf()

    private lateinit var genderFilter : String
    private var minAge by Delegates.notNull<Int>()
    private var maxAge by Delegates.notNull<Int>()
    private var radius by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        arguments?.let {
            whichFrag= it.getInt("which")


            conditionList.addAll(it.getStringArrayList("condition")!!.toMutableList())
        }



        currentPosition = 0
    }

    override fun onCreateView (

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View?
    {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_matching,container,false)
        val view = binding.root


        noResultViewOn(false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        otherUserList = mutableListOf()


        cardStackLayoutManager = CardStackLayoutManager(activity, this)
        cardStackAdapter = CardStackAdapter(otherUserList) {

            val intent = Intent(requireContext(), UserProfileActivity::class.java)
            intent.putExtra("otherUser", otherUserList.get(currentPosition))
            startActivityForResult(intent, 0)

        }

        cardStackViewInit()

        mainViewModel.userLocal.observe(requireActivity(), Observer {
            ownUser = it
        })

        genderFilter = viewModel.getGender()!!
        minAge = viewModel.getAgeRange()!!.get(0)
        maxAge = viewModel.getAgeRange()!!.get(1)
        radius = viewModel.getDistance()!!*1000

        lifecycleScope.launch {

            binding.progressBarUser.show()

            //val response =
                //viewModel.getUsersWithGeoHash(AppContext.latitude, AppContext.longitude, radius, genderFilter, minAge, maxAge)

            if (whichFrag == MAIN) {
                response =
                    viewModel.getUsersWithGeoHash(AppContext.latitude, AppContext.longitude, radius, genderFilter, minAge, maxAge)
            } else if (whichFrag == RESIDENCE) {
                response =
                    viewModel.getUsersWithPlaces(conditionList, genderFilter, minAge, maxAge)
            } else if (whichFrag == TRIP) {
                response =
                    viewModel.getUsersWithPlaces(conditionList, genderFilter, minAge, maxAge)
            }

            when(response) {


                is Response.Success -> {
                    binding.progressBarUser.hide()

                    if (!otherUserList.isEmpty())
                        otherUserList.removeAll(otherUserList)

                    otherUserList.addAll((response as Response.Success<MutableList<UserEntity>>).data)

                    if (otherUserList.contains(ownUser))
                        otherUserList.remove(ownUser)


                    if (otherUserList.size == 0)
                        noResultViewOn(true)
                    else
                        noResultViewOn(false)

                    Log.d("fragment-state", "size: " +otherUserList.size)

                    cardStackAdapter.updateUserList(otherUserList)
                    cardStackAdapter.notifyDataSetChanged()
                }
                is Response.Error -> {
                    Log.d("TAG-LIFE", (response as Response.Error).errorMessage)
                }
                else -> {
                    Log.d("TAG-LIFE", "else")

                }
            }
        }



        binding.buttonLike.setOnClickListener { like(otherUserList.get(currentPosition), ownUser) }
        binding.buttonSkip.setOnClickListener { skip(otherUserList.get(currentPosition)) }

        binding.buttonSetSearchCondition.setOnClickListener { requireActivity().startActivity(Intent(requireActivity(), FilterActivity::class.java)) }



    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (hidden)
            this.hidden = true
        else
            this.hidden = false

    }

    override fun onStart() {
        super.onStart()
        getUsersAgain()
    }

    companion object
    {
        val MALE = "male"
        val FEMALE = "female"
        val BOTH = "both"


        const val MAIN = 10000
        const val STYLE = 10001
        const val RESIDENCE = 10002
        const val TRIP = 10003

        @JvmStatic
        fun newInstance(whichFrag : Int, list : ArrayList<String>) =
            MatchingFragment().apply {
                arguments = Bundle().apply {
                    putInt("which", whichFrag)
                    putStringArrayList("condition", list)
                }
            }
    }



    override fun onCardDragging(direction: Direction?, ratio: Float) {

        if (!dragged && direction == Direction.Left && ratio > 0.45) {
            like(otherUserList.get(currentPosition), ownUser)
            dragged = true
        } else if (!dragged && direction == Direction.Right && ratio > 0.45) {
            skip(otherUserList.get(currentPosition))
            dragged = true
        } else {

        }
    }

    override fun onCardSwiped(direction: Direction?) {
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) { initInfo() }

    override fun onCardDisappeared(view: View?, position: Int) {

        if (currentPosition<otherUserList.size - 1)
            currentPosition++

        lifecycleScope.launch(Dispatchers.Main) {

            delay(950)

            if (currentPosition == otherUserList.size - 1)
                noResultViewOn(true)
            else
                noResultViewOn(false)
        }
    }

    private fun cardStackViewInit () {

        cardStackLayoutManager.setStackFrom(StackFrom.None)
        cardStackLayoutManager.setVisibleCount(2)
        cardStackLayoutManager.setTranslationInterval(7.998f)
        cardStackLayoutManager.setScaleInterval(0.94989f)
        cardStackLayoutManager.setSwipeThreshold(0.29998f)
        cardStackLayoutManager.setMaxDegree(19.998f)
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL)
        cardStackLayoutManager.setCanScrollHorizontal(true)
        cardStackLayoutManager.setCanScrollVertical(false)
        cardStackLayoutManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        cardStackLayoutManager.setOverlayInterpolator(LinearInterpolator())

        binding.cardUserList.layoutManager = cardStackLayoutManager
        binding.cardUserList.adapter = cardStackAdapter
        //cardStackView.layoutManager = cardStackLayoutManager
        //cardStackView.adapter = cardStackAdapter
        binding.cardUserList.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }



    }

    private fun initInfo () {
        dragged = false
        if (otherUserList.get(currentPosition).tripWish.size>1)
            binding.textTripWish.setText(otherUserList.get(currentPosition).tripWish.get(0)+", "+ otherUserList.get(currentPosition).tripWish.get(1))
        else
            binding.textTripWish.setText(otherUserList.get(currentPosition).tripWish.get(0))

        if (otherUserList.get(currentPosition).tripStyle.size>1)
            binding.textTripStyle.setText("#"+otherUserList.get(currentPosition).tripStyle.get(0)+", #"+otherUserList.get(currentPosition).tripStyle.get(1))
        else
            binding.textTripStyle.setText("#"+otherUserList.get(currentPosition).tripStyle.get(0))
    }

    private fun like (otherUser : UserEntity, ownUser : UserEntity) {

        Log.d("user-like", otherUser.toString())

        lifecycleScope.launch {

            when(viewModel.likeUser(otherUser, ownUser)) {
                is Response.Success -> {
                    val setting = SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Slow.duration)
                        .build()

                    cardStackLayoutManager.setSwipeAnimationSetting(setting)
                    binding.cardUserList.swipe()
                }
                is Response.Error -> {

                }
                else -> {

                }
            }

        }
    }

    private fun skip (otherUser: UserEntity) {

        viewModel.skipUser(otherUser)

        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(Duration.Slow.duration)
            .build()

        cardStackLayoutManager.setSwipeAnimationSetting(setting)
        binding.cardUserList.swipe()

    }

    private fun getUsersAgain () {


        if (!hidden) {


            val newRadius = viewModel.getDistance()!!*1000
            if (
                radius != newRadius ||
                !genderFilter.equals(viewModel.getGender()) ||
                minAge != viewModel.getAgeRange()!!.get(0) ||
                maxAge != viewModel.getAgeRange()!!.get(1)
            ) {


                radius = newRadius
                genderFilter = viewModel.getGender()!!
                minAge = viewModel.getAgeRange()!!.get(0)
                maxAge = viewModel.getAgeRange()!!.get(1)

                Log.d("fragment-state", "fr: " +whichFrag +" - "+hidden)



                Log.d("fragment-state", "wh: " +radius)
                Log.d("fragment-state", "wh: " +genderFilter)
                Log.d("fragment-state", "wh: " +minAge)
                Log.d("fragment-state", "wh: " +maxAge)


                lifecycleScope.launch {

                    binding.progressBarUser.show()

                    if (whichFrag == MAIN) {
                        response =
                            viewModel.getUsersWithGeoHash(AppContext.latitude, AppContext.longitude, radius, genderFilter, minAge, maxAge)
                    } else if (whichFrag == RESIDENCE) {
                        response =
                            viewModel.getUsersWithPlaces(conditionList, genderFilter, minAge, maxAge)
                    } else if (whichFrag == TRIP) {
                        response =
                            viewModel.getUsersWithPlaces(conditionList, genderFilter, minAge, maxAge)
                    }

                    when(response) {


                        is Response.Success -> {
                            binding.progressBarUser.hide()

                            if (!otherUserList.isEmpty())
                                otherUserList.removeAll(otherUserList)

                            otherUserList.addAll((response as Response.Success<MutableList<UserEntity>>).data)

                            Log.d("fragment-state", "size: " +otherUserList.size)

                            if (otherUserList.size == 0)
                                noResultViewOn(true)
                            else
                                noResultViewOn(false)

                            cardStackAdapter.updateUserList(otherUserList)
                            cardStackAdapter.notifyDataSetChanged()
                        }
                        is Response.Error -> {
                            Log.d("TAG-LIFE", (response as Response.Error).errorMessage)
                        }
                        else -> {
                            Log.d("TAG-LIFE", "else")

                        }
                    }

                    radius = newRadius
                    genderFilter = viewModel.getGender().toString()
                    minAge = viewModel.getAgeRange()!!.get(0)
                    maxAge = viewModel.getAgeRange()!!.get(1)
                }

            }

        }

    }

    private fun noResultViewOn (on : Boolean) {

        if (on) {

            binding.containerUserOn.visibility = View.INVISIBLE
            binding.containerNoResult.visibility = View.VISIBLE

        } else {

            binding.containerUserOn.visibility = View.VISIBLE
            binding.containerNoResult.visibility = View.INVISIBLE

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 1) {
            binding.cardUserList.swipe()
        }

    }



}