package com.versec.versecko.view.story

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.AppContext
import com.versec.versecko.R
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityStoryUploadBinding
import com.versec.versecko.util.Response
import com.versec.versecko.util.WindowEventManager
import com.versec.versecko.view.ChoosePlaceActivity
import com.versec.versecko.view.signup.adapter.ImageAdapter
import com.versec.versecko.view.signup.adapter.TagAdapter
import com.versec.versecko.viewmodel.StoryUploadViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.time.LocalDateTime

class StoryUploadActivity : AppCompatActivity() {

    private val viewModel : StoryUploadViewModel by viewModel<StoryUploadViewModel>()

    lateinit var binding : ActivityStoryUploadBinding

    private lateinit var images : MutableList<Uri>
    private lateinit var imageAdapter : ImageAdapter


    private lateinit var mainPlace : String
    private lateinit var subPlace : String
    private lateinit var places : MutableList<String>

    private lateinit var tagAdapter : TagAdapter

    private var imageCount = 0

    private lateinit var ownUser : UserEntity

    private var imageAddedOrNot : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityStoryUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        ownUser = intent.getSerializableExtra("user") as UserEntity


        images = mutableListOf (

            Uri.parse("---"),
            Uri.parse("---"),
            Uri.parse("---"),
            Uri.parse("---"),
            Uri.parse("---"),
            Uri.parse("---")

        )



        binding.recyclerChosenImages.layoutManager =
            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)

        val resourceId = resources.getIdentifier("button_add", "drawable", packageName)

        imageAdapter = ImageAdapter(images, resourceId, false) { item, position ->

            if (images.get(position).toString().equals("---")) {

                startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), IMAGE)

            } else {

                val builder = AlertDialog.Builder(this)

                builder.setItems(arrayOf("삭제"), DialogInterface.OnClickListener { dialogInterface, index ->

                    images.set(position, Uri.parse("---"))
                    imageCount = imageCount - 1

                    images.removeAt(position)
                    images.add(Uri.parse("---"))

                    imageAdapter.updateList(images)
                    imageAdapter.notifyDataSetChanged()

                })

                builder.create().show()

            }

        }

        binding.recyclerChosenImages.adapter = imageAdapter

        places = mutableListOf("+")

        tagAdapter = TagAdapter(places) { place ->

            if (places.get(0).equals(place)) {
                startActivityForResult(Intent(this, ChoosePlaceActivity::class.java).putExtra("requestCode", STORY_UPLOAD), STORY_UPLOAD)
            }
        }


        binding.recyclerPlace.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL,false)
        binding.recyclerPlace.adapter = tagAdapter


        binding.editContents.doAfterTextChanged { text ->


            var textCount = "0/200"

            if (text.toString().length > 10 ) {
                binding.editContents.isEnabled = false
            } else {

                binding.editContents.isEnabled = true

                textCount =
                    text.toString().length.toString() + "/200"
                binding.textCheckLength.setText(textCount)
            }

        }






        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonComplete.setOnClickListener {

            show()
            imageAddedOrNot = false

            val uriMap = mutableMapOf<String, String>()

            images.forEachIndexed { index, uri ->

                if (!uri.toString().equals("---")) {

                    imageAddedOrNot = true
                    uriMap.put(index.toString(), uri.toString())

                } else {
                    uriMap.put(index.toString(), NULL)
                }

            }


            if (imageAddedOrNot) {

                if (places.size > 2) {

                    if (binding.editContents.text.length > 0) {

                        val story = StoryEntity(
                            ownUser.uid,
                            ownUser.uid,
                            ownUser.nickName,
                            binding.editContents.text.toString(),
                            uriMap,
                            places.get(1),
                            places.get(2),
                            mutableListOf(),
                            System.currentTimeMillis()
                        )

                        lifecycleScope.launch(Dispatchers.IO) {

                            val uploadResponse = viewModel.uploadStory(story)

                            when(uploadResponse) {

                                is Response.Success -> {


                                    withContext(Dispatchers.Main) {
                                        hide()
                                    }

                                    setResult(COMPLETE, intent)
                                    finish()
                                }
                                is Response.Error -> {

                                    withContext(Dispatchers.Main) {
                                        hide()

                                        Toast.makeText(this@StoryUploadActivity, "인터넷 연결 오류로 인해 스토리가 업로드되지 않았습니다.", Toast.LENGTH_SHORT).show()

                                        Log.d("story-check", uploadResponse.errorMessage)
                                    }

                                }
                                else -> {

                                }
                            }
                        }



                    } else {

                        hide()
                        Toast.makeText(this, "모든 정보를 입력하고 완료 버튼을 눌러주세요!", Toast.LENGTH_SHORT).show()

                    }

                } else {

                    hide()
                    Toast.makeText(this, "모든 정보를 입력하고 완료 버튼을 눌러주세요!", Toast.LENGTH_SHORT).show()

                }

            } else {

                hide()
                Toast.makeText(this, "모든 정보를 입력하고 완료 버튼을 눌러주세요!", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun show () {

        binding.progressBar.show()
        WindowEventManager.blockUserInteraction(this)
    }

    private fun hide () {

        binding.progressBar.hide()
        WindowEventManager.openUserInteraction(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE && resultCode == RESULT_OK && data != null) {


            val uri = data.data

            Log.d("ucrop-check", uri.toString())

            val uCrop = uri?.let {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    UCrop.of(
                        it,
                        Uri.fromFile(
                            File(
                                cacheDir,
                                "story__"+
                                        LocalDateTime.now().toString()
                                        +".png"

                            )
                                    )
                            )
                        .withAspectRatio(1.0F, 1.0F)
                        .start(this)
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {

            val uri = UCrop.getOutput(data)

            if (uri != null ) {

                images.set(imageCount, uri)
                imageCount = imageCount+1

                imageAdapter.updateList(images)
                imageAdapter.notifyDataSetChanged()

                Log.d("ucrop-check!", uri.toString())

            }
        } else if (requestCode ==  STORY_UPLOAD && resultCode == 300 && data != null) {


            places.clear()
            places.add("다시 설정")
            places.addAll(data.getStringArrayListExtra("story")!!)

            tagAdapter.updateTagList(places)
            tagAdapter.notifyDataSetChanged()

            mainPlace = places.get(1)
            subPlace = places.get(2)

        } else {

            Log.d("ucrop-check", "else")

        }


    }

    companion object {

        const val IMAGE = 100
        const val PLACE = 503
        const val COMPLETE = 5000
        const val NULL = "null"

        private const val STORY_UPLOAD = 250

    }
}