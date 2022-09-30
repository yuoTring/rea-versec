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
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.FragmentMatchingBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.matching.adapter.CardStackAdapter
import com.versec.versecko.viewmodel.MainViewModel
import com.versec.versecko.viewmodel.MatchingViewModel
import com.yuyakaido.android.cardstackview.*
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
            conditionList.addAll(it.getStringArrayList("codition")!!.toMutableList())
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
            Log.d("user-get", ownUser.toString())
        })

        //should move it to anther one
        viewModel.setGender("both")
        viewModel.setAgeRange(20,80)
        viewModel.setDistance(30)

        genderFilter = viewModel.getGender()!!
        minAge = viewModel.getAgeRange()!!.get(0)
        maxAge = viewModel.getAgeRange()!!.get(1)
        radius = viewModel.getDistance()!!*1000

        lifecycleScope.launch {

            binding.progressBarUser.show()

            val response =
                viewModel.getUsersWithGeoHash(AppContext.latitude, AppContext.longitude, radius, genderFilter, minAge, maxAge)
            when(response) {


                is Response.Success -> {
                    binding.progressBarUser.hide()

                    if (!otherUserList.isEmpty())
                        otherUserList.removeAll(otherUserList)

                    otherUserList.addAll(response.data)

                    cardStackAdapter.updateUserList(otherUserList)
                    cardStackAdapter.notifyDataSetChanged()
                }
                is Response.Error -> {
                    Log.d("TAG-LIFE", response.errorMessage)
                }
                else -> {
                    Log.d("TAG-LIFE", "else")

                }
            }
        }



        binding.buttonLike.setOnClickListener { like(otherUserList.get(currentPosition), ownUser) }
        binding.buttonSkip.setOnClickListener { skip(otherUserList.get(currentPosition)) }



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


        const val MAIN = 1000
        const val PLACE = 1001
        const val STYLE = 1002

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

    override fun onCardAppeared(view: View?, position: Int) {  initInfo() }

    override fun onCardDisappeared(view: View?, position: Int) {

        if (currentPosition<otherUserList.size - 1)
            currentPosition++
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

                lifecycleScope.launch {

                    binding.progressBarUser.show()

                    val response =
                        viewModel.getUsersWithGeoHash(AppContext.latitude, AppContext.longitude, radius, genderFilter, minAge, maxAge)
                    when(response) {


                        is Response.Success -> {
                            binding.progressBarUser.hide()

                            if (!otherUserList.isEmpty())
                                otherUserList.removeAll(otherUserList)

                            otherUserList.addAll(response.data)

                            cardStackAdapter.updateUserList(otherUserList)
                            cardStackAdapter.notifyDataSetChanged()
                        }
                        is Response.Error -> {
                            Log.d("TAG-LIFE", response.errorMessage)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 1) {
            binding.cardUserList.swipe()
        }

    }



}