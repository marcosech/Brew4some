package com.brewforsome.etxekakatua.brewforsome.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import butterknife.OnClick
import com.brewforsome.etxekakatua.brewforsome.R
import com.brewforsome.etxekakatua.brewforsome.Utils.Application
import com.brewforsome.etxekakatua.brewforsome.Utils.PreferencesManager
import com.brewforsome.etxekakatua.brewforsome.Utils.Utils
import com.brewforsome.etxekakatua.brewforsome.databinding.ActivityUserBinding
import com.brewforsome.etxekakatua.brewforsome.views.MyBeerListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 * Created by etxekakatua on 11/3/18.
 */
class UserActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    var databaseReference: DatabaseReference? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var preferencesManager: PreferencesManager? = null

    lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUserBinding.inflate(layoutInflater).apply {
            setContentView(root)
            binding = this
            initializeUi()
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mAuth = FirebaseAuth.getInstance()
        preferencesManager = PreferencesManager(this)
        initializeFirebase()
        loadUserData()
    }

    private fun initializeUi(){
        binding.userChangeBtn.setOnClickListener { changeDataClick() }
        binding.userCloseBtn.setOnClickListener { userCloseClick() }
        binding.userPassResetBtn.setOnClickListener { userPassResetClick() }
    }

    private fun loadUserData() {
        binding.signEmailTxt.setText(Application.email)
        binding.signUserTxt.setText(Application.name)
        binding.signPhoneTxt.setText(Application.phone)
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

    fun userPassResetClick() {
        val i = Intent(this@UserActivity, ForgotActivity::class.java)
        i.putExtra("from", "userData")
        startActivity(i)
    }

    fun changeDataClick() {
        signupUser(binding.signUserTxt.text.toString(), binding.signEmailTxt.text.toString(), binding.signPassTxt.text.toString(), binding.signPhoneTxt.text.toString())
    }

    fun userCloseClick() {
        val i = Intent(this@UserActivity, MyBeerListActivity::class.java)
        startActivity(i)
    }

    fun signupUser(name: String, email: String, password: String, phone: String) {
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
            setUserProfile(name, email, phone)
        }
    }

    fun setUserProfile(name: String, email: String, phone: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            mAuth = FirebaseAuth.getInstance()
            user.updateEmail(email!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Utils.createToast(this@UserActivity, "Cambiado")
                        }
                    }
            val userId = mAuth!!.currentUser!!.uid
            val currentUser = FirebaseDatabase.getInstance().reference.child("Users").child(userId)
            lateinit var newUser: HashMap<String, String>
            newUser["userName"] = name
            newUser["userEmail"] = email
            newUser["userPhone"] = phone
            currentUser.setValue(newUser)
        }
        preferencesManager!!.saveUserDatas(null, null, null, null, null)
        Application.reloadDatas(baseContext)
        mAuth!!.signOut()
        val i = Intent(this@UserActivity, LoginActivity::class.java)
        startActivity(i)
    }
}