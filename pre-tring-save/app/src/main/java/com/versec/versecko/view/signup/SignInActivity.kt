package com.versec.versecko.view.signup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.versec.versecko.R
import com.versec.versecko.databinding.ActivitySignInBinding
import com.versec.versecko.databinding.ItemAlertSignincodeBinding
import com.versec.versecko.util.Response
import com.versec.versecko.util.WindowEventManager
import com.versec.versecko.view.MainScreenActivity
import com.versec.versecko.viewmodel.SignInViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class SignInActivity
    (


): AppCompatActivity()
{

    private val signInViewModel : SignInViewModel by viewModel<SignInViewModel>()
    private lateinit var binding : ActivitySignInBinding
    private lateinit var verifyCode : String
    private lateinit var  id : String
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken

    private lateinit var phoneNumber : String


    private lateinit var editText : EditText
    private lateinit var pinView : EditText
    private lateinit var textTimer : TextView
    private lateinit var textNotice : TextView
    private lateinit var buttonGetCode : AppCompatButton
    private lateinit var buttonVerify : AppCompatButton
    private lateinit var buttonReGetCode : AppCompatButton

    private lateinit var timer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val mAuth : FirebaseAuth by inject()

        verifyCode = "not-yet"

        editText = findViewById(R.id.editPhoneNumber)
        pinView = findViewById(R.id.pinviewSmsCode)
        textTimer = findViewById(R.id.textTimer)
        textNotice = findViewById(R.id.textNotice)
        buttonGetCode = findViewById(R.id.buttonGetCode)
        buttonVerify = findViewById(R.id.buttonVerifyCode)
        buttonReGetCode = findViewById(R.id.buttonReGetCode)

        buttonGetCode.isClickable = false

        signInViewModel.setDistance(30)
        signInViewModel.setGender("both")
        signInViewModel.setAgeRange(20,80)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (text != null) {
                    if (text.length>8 && text.length<12) {

                        buttonGetCode.setBackgroundResource(R.drawable.button_corner_16_blue_gradient)
                        buttonGetCode.isClickable = true

                    } else
                    {
                        buttonGetCode.setBackgroundResource(R.drawable.button_corner_16_gray_light)
                        buttonGetCode.isClickable = false
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }


        })


        pinView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (text != null) {

                    /**
                    if (text.equals(verifyCode)) {


                        textNotice.text = "올바른 코드입니다!"
                        textNotice.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.blue_azure))
                        buttonVerify.setBackgroundResource(R.drawable.button_corner_16_blue_gradient)
                        buttonVerify.isClickable = true

                    }
                    else {
                        textNotice.text = "잘못된 코드입니다!"
                        textNotice.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.red))
                        buttonVerify.setBackgroundResource(R.drawable.button_corner_16_gray_light)
                        buttonVerify.isClickable = false
                    } **/

                    if (text.length == 6) {

                        buttonVerify.setBackgroundResource(R.drawable.button_corner_16_blue_gradient)
                        buttonVerify.isClickable = true

                    } else {

                        buttonVerify.setBackgroundResource(R.drawable.button_corner_16_gray_light)
                        buttonVerify.isClickable = false
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                Log.d("sms-auto", "smsCode: "+ credential.smsCode)

            }

            override fun onVerificationFailed(exception: FirebaseException) {

            }


            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken

            ) {
                hide()


                startTimer()

                id = verificationId
                resendingToken = token

                Log.d("sms-test", "onCodeSent")




                textNotice.text = "인증 코드를 입력해주세요."
                textNotice.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.blue_azure))


            }

        }

        buttonGetCode.setOnClickListener {

            show()

            phoneNumber = editText.text.toString()

            //TimeUnit
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+82"+phoneNumber) // Phone number to verify
                .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)

            pinView.visibility=View.VISIBLE
            textNotice.visibility =View.VISIBLE
            textNotice.text = "잠시만 기다려주세요..."
            textNotice.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.blue_azure))

            buttonGetCode.visibility = View.INVISIBLE
            buttonVerify.visibility = View.VISIBLE
            buttonVerify.setBackgroundResource(R.drawable.button_corner_16_gray_light)
            buttonVerify.isClickable = false

            buttonReGetCode.visibility = View.VISIBLE
            buttonReGetCode.isClickable = true

        }

        buttonVerify.setOnClickListener {

            show()

            Log.d("sms-test", "onClick")

            Log.d("sms-test", "id: "+id)

            verifyCode = pinView.text.toString()

            Log.d("sms-test", "code: "+verifyCode)


            val credential = PhoneAuthProvider.getCredential(id, verifyCode)



            lifecycleScope.launch {

                val signInResult = signInViewModel.signIn(credential)


                when(signInResult) {


                    is Response.Exist -> {


                        val user = signInViewModel.getOwnUser()

                        when(user) {
                            is Response.Success -> {

                                if (user.data != null) {

                                    hide()
                                    signInViewModel.insertUser_Local(user.data)
                                    startActivity(Intent(this@SignInActivity, MainScreenActivity::class.java))

                                } else {
                                    show()
                                }
                            }
                            is Response.Error -> {
                                show()
                            }
                            else -> {

                            }
                        }


                    }
                    is Response.No -> {

                        hide()

                        startActivity(Intent(this@SignInActivity, FillUserInfoActivity::class.java).putExtra("phoneNumber", phoneNumber))
                    }
                    is Response.Error -> {

                        hide()

                        val builder = AlertDialog.Builder(this@SignInActivity).create()

                        val view = layoutInflater.inflate(R.layout.item_alert_signincode, null)
                        builder.setView(view)
                        builder.show()

                        ItemAlertSignincodeBinding.bind(view).buttonConfirm.setOnClickListener { builder.dismiss() }
                    }
                    else -> {

                    }
                }
            }

        }


        buttonReGetCode.setOnClickListener {

            timer.cancel()

            show()

            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+82"+phoneNumber) // Phone number to verify
                .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .setForceResendingToken(resendingToken)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)

            buttonReGetCode.visibility = View.GONE

            textNotice.text = "잠시만 기다려주세요..."
            textNotice.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.blue_azure))

            buttonGetCode.visibility = View.INVISIBLE
            buttonVerify.setBackgroundResource(R.drawable.button_corner_16_gray_light)
            buttonVerify.isClickable = false
        }











    }

    private fun startTimer () {

        textTimer.visibility = View.VISIBLE

        timer = object : CountDownTimer(120000,1000) {
            override fun onTick(millis: Long) {

                textTimer.setText(SimpleDateFormat("mm:ss").format(Date(millis)))
                textTimer.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.blue_azure))
            }

            override fun onFinish() {
                textTimer.text = "인증 코드가 만료되었습니다."
                textTimer.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.red))

                buttonVerify.setBackgroundResource(R.drawable.button_corner_16_gray_light)
                buttonVerify.isClickable = false


            }


        }

        timer.start()
    }

    private fun show() {
        binding.progressBar.show()
        WindowEventManager.blockUserInteraction(this)
    }

    private fun hide () {
        binding.progressBar.hide()
        WindowEventManager.openUserInteraction(this)
    }

    override fun onBackPressed() {

    }
}