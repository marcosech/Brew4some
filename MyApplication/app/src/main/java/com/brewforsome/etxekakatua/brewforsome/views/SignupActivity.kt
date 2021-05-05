package com.brewforsome.etxekakatua.brewforsome.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.brewforsome.etxekakatua.brewforsome.R
import com.brewforsome.etxekakatua.brewforsome.Utils.Application
import com.brewforsome.etxekakatua.brewforsome.Utils.Utils
import com.brewforsome.etxekakatua.brewforsome.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.HashMap


class SignupActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    lateinit var binding:ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySignupBinding.inflate(layoutInflater).apply {
            setContentView(root)
            binding = this
            initializeUi(this)
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mAuth = FirebaseAuth.getInstance()
        initializeFirebase()
    }

    private fun initializeUi(binding: ActivitySignupBinding) {
        binding.signSignupBtn.setOnClickListener { signUpClick() }
        binding.loginClickBtn.setOnClickListener { loginClick() }
    }

    fun signUpClick() {
        signupUser(binding.signUserTxt.text.toString(), binding.signEmailTxt.text.toString(), binding.signPassTxt.text.toString(), binding.signPhoneTxt.text.toString())
    }

    private fun signupUser(name: String, email: String, password: String, phone: String) {
        if (Utils.isEmpty(name) || Utils.isEmpty(email) || Utils.isEmpty(password) || Utils.isEmpty(phone)) {
            Utils.createToast(this, getString(R.string.emptyFields))
        }
        if (!Utils.checkEmailFormat(email)) {
            Utils.createToast(this, getString(R.string.emailErrorFormat))
        } else if (!Utils.isPassValid(password)) {
            Utils.createToast(this, getString(R.string.passErrorFormat))
        } else if (!Utils.isPhoneValid(phone)) {
            Utils.createToast(this, getString(R.string.passPhoneFormat))
        } else {
            mAuth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@SignupActivity) { task ->
                        if (!task.isSuccessful) {
                            if (task.exception is FirebaseAuthUserCollisionException) {
                                Utils.createToast(this@SignupActivity, "User with this email already exist.")
                            } else {
                                Utils.createToast(this@SignupActivity, getString(R.string.errorSignup))
                            }
                        } else {
                            Utils.createToast(this@SignupActivity, getString(R.string.successSignup))
                            setUserProfile(name, email, phone)
                        }
                    }
        }
    }

    //Add user datas
    fun setUserProfile(name: String, email: String, phone: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = mAuth!!.currentUser!!.uid
            val currentUser = FirebaseDatabase.getInstance().reference.child("Users").child(userId)
            lateinit var newUser: HashMap<String, String>
            newUser["userName"] = name
            newUser["userEmail"] = Application.email
            newUser["userPhone"] = Application.phone
            currentUser.setValue(newUser)


        }
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    private fun loginClick() {
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    private fun initializeFirebase() {
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                Log.d("Usuario logueado", "onAuthStateChanged:signed_in:" + user.uid)
            } else {
                Log.d("usuario sin loguear", "onAuthStateChanged:signed_out")
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    public override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }
}