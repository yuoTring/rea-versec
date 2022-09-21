package com.versec.versecko.view.matching

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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.AppContext
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.FragmentMatchingBinding
import com.versec.versecko.view.matching.adapter.CardStackAdapter
import com.versec.versecko.viewmodel.MainViewModel
import com.versec.versecko.viewmodel.MatchingViewModel
import com.yuyakaido.android.cardstackview.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchingFragment : Fragment(), CardStackListener {

    private lateinit var binding : FragmentMatchingBinding
    private val viewModel : MatchingViewModel by viewModel<MatchingViewModel>()
    private val mainViewModel : MainViewModel by viewModel<MainViewModel>()

    private lateinit var cardStackLayoutManager: CardStackLayoutManager
    private lateinit var cardStackAdapter: CardStackAdapter

    private lateinit var ownUser : UserEntity
    private lateinit var otherUserList : MutableList<UserEntity>
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {}

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
        cardStackAdapter = CardStackAdapter(requireActivity(), otherUserList)

        cardStackViewInit()

        mainViewModel.userLocal.observe(requireActivity(), Observer {
            ownUser = it
        })







        val radius = 2250
            //1750
            //1500

        viewModel.getUsersWithGeoHash(37.39373713, 126.963231, radius)
            .observe(viewLifecycleOwner, Observer {
                    users ->

                Log.d("user-getwithhash", "size -> "+ users.size)

                if (!otherUserList.isEmpty())
                otherUserList.removeAll(otherUserList)

                otherUserList.addAll(users)

                cardStackAdapter.updateUserList(users)
                cardStackAdapter.notifyDataSetChanged()

            })



        binding.buttonLike.setOnClickListener {

            viewModel.likeUser(otherUserList.get(currentPosition), ownUser)

            Toast.makeText(requireActivity(), "@@@", Toast.LENGTH_SHORT).show()

            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Slow.duration)
                //.setInterpolator(AccelerateInterpolator())
                .build()

            cardStackLayoutManager.setSwipeAnimationSetting(setting)

            binding.cardUserList.swipe()

        }

        binding.buttonSkip.setOnClickListener {

            viewModel.skipUser(otherUserList.get(currentPosition), ownUser)

            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Slow.duration)
                //.setInterpolator(AccelerateInterpolator())
                .build()


            cardStackLayoutManager.setSwipeAnimationSetting(setting)
            binding.cardUserList.swipe()
        }

    }

    companion object
    {

        @JvmStatic
        fun newInstance() =
            MatchingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }



    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {   initInfo() }

    override fun onCardDisappeared(view: View?, position: Int) { currentPosition++ }

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


        binding.textTripWish.setText(otherUserList.get(currentPosition).tripWish.get(0)+", "+ otherUserList.get(currentPosition).tripWish.get(1))
        binding.textTripStyle.setText("#"+otherUserList.get(currentPosition).tripStyle.get(0)+", #"+otherUserList.get(currentPosition).tripStyle.get(1))




    }
}