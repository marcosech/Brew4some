package com.brewforsome.etxekakatua.brewforsome.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brewforsome.etxekakatua.brewforsome.R
import com.brewforsome.etxekakatua.brewforsome.Utils.Application
import com.brewforsome.etxekakatua.brewforsome.Utils.PreferencesManager
import com.brewforsome.etxekakatua.brewforsome.Utils.Utils
import com.brewforsome.etxekakatua.brewforsome.databinding.ActivityLoginBinding
import com.brewforsome.etxekakatua.brewforsome.model.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    var databaseReference: DatabaseReference? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var preferencesManager: PreferencesManager? = null
    var i: Intent? = null
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityLoginBinding.inflate(layoutInflater).apply {
            setContentView(root)
            binding = this
            initializeUi()
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mAuth = FirebaseAuth.getInstance()
        preferencesManager = PreferencesManager(this)
        i = intent
        initializeFirebase()
    }

    private fun initializeUi() {
        binding.loginSignupBtn.setOnClickListener { signupClick() }
        binding.forgotBtn.setOnClickListener { forgotClick() }
        binding.loginBtn.setOnClickListener { loginClick() }
    }

    private fun initializeFirebase() {
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                Log.d("Entra", "onAuthStateChanged:signed_in:" + user.uid)
            } else {
                Log.d("Sale", "onAuthStateChanged:signed_out")
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

    fun signupClick() {
        val i = Intent(this, SignupActivity::class.java)
        startActivity(i)
    }

    private fun forgotClick() {
        val i = Intent(this, ForgotActivity::class.java)
        startActivity(i)
    }

    private fun loginClick() {
        Utils.showProgress(this)
        loginUser(binding.loginUserTxt.text.toString(), binding.loginPassTxt.text.toString())
    }

    private fun loginUser(email: String?, password: String?) {
        if (!Utils.checkEmailFormat(email)) {
            Utils.createToast(this, getString(R.string.emailErrorFormat))
            Utils.hideProgress()
        } else if (!Utils.isPassValid(password)) {
            Utils.createToast(this, getString(R.string.passErrorFormat))
            Utils.hideProgress()
        } else {
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            // Sign in success, update UI with the signed-in user's information
                            Utils.createToast(this@LoginActivity, getString(R.string.successLogin))
                            mAuth = FirebaseAuth.getInstance()
                            databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(mAuth!!.currentUser!!.uid)
                            databaseReference!!.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val userdto = dataSnapshot.getValue(UserDto::class.java)
                                    //                                    userDataName = dataSnapshot.child("userName").getValue().toString();
//                                    userDataPhone = dataSnapshot.child("userPhone").getValue().toString();
                                    if (mAuth!!.currentUser!!.uid != null) {
                                        preferencesManager!!.saveUserDatas(email, password, userdto!!.getUserName().toString(), userdto.getUserPhone().toString(), mAuth!!.currentUser!!.uid)
                                        Application.reloadDatas(baseContext)
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                            gotoList()
                        }
                        if (!task.isSuccessful) {
                            // If sign in fails, display a message to the user.
                            Utils.createToast(this@LoginActivity, getString(R.string.errorLogin))
                            Utils.hideProgress()
                        }
                    }
        }
    }

    private fun gotoList() {
        val b = Intent(this, BeerListActivity::class.java)
        startActivity(b)
    }

    override fun onBackPressed() {
        if (i!!.getStringExtra("logout") != null) {
        }
    }
}