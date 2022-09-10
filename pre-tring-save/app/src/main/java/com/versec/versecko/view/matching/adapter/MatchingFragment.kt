import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.versec.versecko.AppContext
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.FragmentMatchingBinding
import com.versec.versecko.view.matching.adapter.CardStackAdapter
import com.versec.versecko.viewmodel.MatchingViewModel
import com.yuyakaido.android.cardstackview.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchingFragment : Fragment(), CardStackListener {

    private lateinit var binding : FragmentMatchingBinding
    private val viewModel : MatchingViewModel by viewModel<MatchingViewModel>()

    private lateinit var cardStackLayoutManager: CardStackLayoutManager
    private lateinit var cardStackAdapter: CardStackAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {}
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
        //inflater.inflate(R.layout.fragment_matching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val testUserList = mutableListOf<UserEntity>()

        for (uid in 100..102) {

            testUserList.add(
                UserEntity(

                    uid = "testuid_____"+uid,
                    nickName = "aeaaaaaa"+uid,
                    gender ="female",
                    age = 22+uid/1000,
                    birth ="19990901",
                    mainResidence= "Seoul",
                    subResidence = "Seoul",
                    tripWish = mutableListOf("!!!","!!?"),
                    tripStyle = mutableListOf("!!!","!!?"),
                    selfIntroduction = "hi~~~",
                    uriMap = mutableMapOf(
                        "0" to "https://firebasestorage.googleapis.com/v0/b/tring-2c450.appspot.com/o/image%2FprofileImages%2Ftest!!!!!%2Ftest!!!!!_0?alt=media&token=fd73fe45-808f-4540-b3ce-0ba5020a1c25",
                        "1" to "https://firebasestorage.googleapis.com/v0/b/tring-2c450.appspot.com/o/image%2FprofileImages%2Ftest!!!!!%2Ftest!!!!!_1?alt=media&token=237b6947-9fd9-4892-83e2-2bedfee38169",
                        "2" to "https://firebasestorage.googleapis.com/v0/b/tring-2c450.appspot.com/o/image%2FprofileImages%2Ftest!!!!!%2Ftest!!!!!_0?alt=media&token=fd73fe45-808f-4540-b3ce-0ba5020a1c25" ),
                    geohash = "none",
                    latitude = 37.0,
                    longitude = 127.0,
                    mannerScore = 4.5,
                    premiumOrNot = false,
                    knock = 0,
                    loungeStatus = 0
                )
            )
        }

        cardStackLayoutManager = CardStackLayoutManager(activity, this)
        cardStackAdapter = CardStackAdapter(testUserList)

        cardStackViewInit()






        /**
        viewModel.getUsersWithGeoHash(37.39373713728228, 126.96323115734985, 2000)
        .observe(viewLifecycleOwner, Observer {

        fetchedUserList ->

        Log.d("-----", "size: "+ fetchedUserList.size)

        cardStackAdapter.updateUserList(fetchedUserList)
        cardStackAdapter.notifyDataSetChanged()

        })**/



        val radius = 4500

        viewModel.getUsersWithGeoHash(37.39373713, 126.963231, radius)
            .observe(viewLifecycleOwner, Observer {
                    users ->

                Log.d("user-getwithhash", "size -> "+ users.size)
            })



        binding.buttonLike.setOnClickListener {

            //viewModel.likeUser(???)

        }

        binding.buttonSkip.setOnClickListener {

        }

    }

    companion object
    {

        // TODO: Rename and change types and number of parameters
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

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardDisappeared(view: View?, position: Int) {
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
        cardStackLayoutManager.setSwipeableMethod(SwipeableMethod.Manual)
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
}