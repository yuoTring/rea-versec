package com.versec.versecko.view.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.versec.versecko.R
import com.versec.versecko.util.Results
import com.versec.versecko.view.SplashActivity
import com.versec.versecko.viewmodel.SignInViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class SignInActivity
    (


): AppCompatActivity()
{

    private val signInViewModel : SignInViewModel by viewModel<SignInViewModel>()
    private lateinit var verifyCode : String
    private lateinit var  id : String

    private lateinit var phoneNumber : String


    private lateinit var editText : EditText
    private lateinit var pinView : EditText
    private lateinit var textNotice : TextView
    private lateinit var buttonGetCode : AppCompatButton
    private lateinit var buttonVerify : AppCompatButton
    private lateinit var buttonReGetCode : AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val mAuth : FirebaseAuth by inject()

        verifyCode = "not-yet"

        editText = findViewById(R.id.editPhoneNumber)
        pinView = findViewById(R.id.pinviewSmsCode)
        textNotice = findViewById(R.id.textNotice)
        buttonGetCode = findViewById(R.id.buttonGetCode)
        buttonVerify = findViewById(R.id.buttonVerifyCode)
        buttonReGetCode = findViewById(R.id.buttonReGetCode)


        pinView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (text != null) {
                    if (text.equals(verifyCode)) {

                        textNotice.text = "Correct Code!"
                        textNotice.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.blue_azure))
                        buttonVerify.setBackgroundResource(R.drawable.button_corner_16_blue_gradient)
                        buttonVerify.isClickable = true

                    }
                    else {
                        textNotice.text = "Incorrect Code!"
                        textNotice.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.red))
                        buttonVerify.setBackgroundResource(R.drawable.button_corner_16_gray_light)
                        buttonVerify.isClickable = true
                        //buttonVerify.isClickable = false
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {


                verifyCode = credential.smsCode.toString()
                Log.d("sms-auto", "smsCode: "+ credential.smsCode)

            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Log.d("sms-test", "onVerificationFailed: "+ exception.toString())

            }


            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                id = verificationId

                Log.d("sms-test", "onCodeSent")




                textNotice.text = "Type in code"
                textNotice.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.blue_azure))

            }

        }

        buttonGetCode.setOnClickListener {

            phoneNumber = editText.text.toString()

            //TimeUnit
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+1"+phoneNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)

            pinView.visibility=View.VISIBLE
            textNotice.visibility =View.VISIBLE
            textNotice.text = "Plz waiting..."
            textNotice.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.black))

            buttonGetCode.visibility = View.INVISIBLE
            buttonVerify.visibility = View.VISIBLE
            buttonVerify.setBackgroundResource(R.drawable.button_corner_16_gray_light)
            buttonVerify.isClickable = false

        }

        buttonVerify.setOnClickListener {

            Log.d("sms-test", "onClick")

            Log.d("sms-test", "id: "+id)

            verifyCode = pinView.text.toString()

            Log.d("sms-test", "code: "+verifyCode)


            val credential = PhoneAuthProvider.getCredential(id, verifyCode)
            signInViewModel.signIn(credential).observe(this, Observer {
                signInResult ->

                if (signInResult.equals(Results.Exist(2))) {
                    Log.d("sms-test", signInResult.toString())

                    startActivity(Intent(this, SplashActivity::class.java))

                }
                else if (signInResult.equals(Results.No(3))) {
                    Log.d("sms-test", signInResult.toString())

                    startActivity(Intent(this, FillUserInfoActivity::class.java).putExtra("phoneNumber", phoneNumber))
                }
                else if (signInResult.equals(Results.Success(1))) {
                    Log.d("sms-test", signInResult.toString())

                }
                else {
                    Log.d("sms-test", signInResult.toString())

                }



            })

            //val signInResult = Results<Int> = async

        }

        buttonReGetCode.setOnClickListener {

            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+1"+phoneNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)

        }








    }
}