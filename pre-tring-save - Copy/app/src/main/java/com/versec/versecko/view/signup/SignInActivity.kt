package com.versec.versecko.view.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.versec.versecko.R
import com.versec.versecko.util.Results
import com.versec.versecko.viewmodel.SignInViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit


class SignInActivity
    (


): AppCompatActivity()
{

    private val signInViewModel : SignInViewModel by viewModel<SignInViewModel>()
    private lateinit var verifyCode : String
    private lateinit var  id : String


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
            }


            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                id = verificationId



                textNotice.text = "Type in code"
                textNotice.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.blue_azure))

            }

        }

        buttonGetCode.setOnClickListener {

            //TimeUnit
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+1"+editText.text.toString()) // Phone number to verify
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

            val credential = PhoneAuthProvider.getCredential(id, verifyCode)
            signInViewModel.signIn(credential)

        }

        buttonReGetCode.setOnClickListener {

        }




        val signInRequestObserver = Observer<Results<Int>> { result ->


            if (result.equals(1)) {

                Toast.makeText(this, "signIn -success", Toast.LENGTH_SHORT).show()

            }
            else {

                Toast.makeText(this, "signIn - fail", Toast.LENGTH_SHORT).show()

            }

        }

        signInViewModel._signInRequest.observe(this, signInRequestObserver)




    }
}