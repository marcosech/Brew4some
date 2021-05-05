package com.brewforsome.etxekakatua.brewforsome.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.brewforsome.etxekakatua.brewforsome.R
import com.brewforsome.etxekakatua.brewforsome.Utils.Application
import com.brewforsome.etxekakatua.brewforsome.Utils.PreferencesManager
import com.brewforsome.etxekakatua.brewforsome.databinding.ActivitySplashBinding
import com.brewforsome.etxekakatua.brewforsome.model.UserDto
import com.brewforsome.etxekakatua.brewforsome.presenter.SplashView
import com.brewforsome.etxekakatua.brewforsome.views.SplashActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SplashActivity : AppCompatActivity(), SplashView {
    private var preferencesManager: PreferencesManager? = null
    private var mAuth: FirebaseAuth? = null
    var databaseReference: DatabaseReference? = null

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySplashBinding.inflate(layoutInflater).apply {
            setContentView(root)
            binding = this
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preferencesManager = PreferencesManager(this)
        mAuth = FirebaseAuth.getInstance()
        val ru = Runnable { startApp() }
        val h = Handler()
        h.postDelayed(ru, SPLASH_SCREEN_DELAY)
    }

    fun logUser() {
        /*preferencesManager = new PreferencesManager(this);
        mAuth.signInWithEmailAndPassword(preferencesManager.getUserEmail().toString(), preferencesManager.getUserPassword().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Entra", "signInWithEmail:onComplete:" + task.isSuccessful());
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();*/
        val i = Intent(this@SplashActivity, BeerListActivity::class.java)
        startActivity(i)
        /*if (!task.isSuccessful()) {
                            // If sign in fails, display a message to the user.
                            Utils.createToast(SplashActivity.this, getString(R.string.errorLogin));
                            goToLogin();
                        }
                    }
                });*/
    }

    override fun goToLogin() {
        val j = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(j)
    }

    protected fun startApp() {
        mAuth = FirebaseAuth.getInstance()
        if (FirebaseAuth.getInstance().currentUser != null) {
            if (mAuth!!.currentUser!!.uid != null) {
                databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(mAuth!!.currentUser!!.uid)
                databaseReference!!.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val userdto = dataSnapshot.getValue(UserDto::class.java)
                        //                                    userDataName = dataSnapshot.child("userName").getValue().toString();
//                                    userDataPhone = dataSnapshot.child("userPhone").getValue().toString();
                        if (mAuth!!.currentUser != null && mAuth!!.currentUser!!.uid != null) {
                            preferencesManager!!.saveUserDatas(userdto!!.getUserEmail().toString(), null, userdto.getUserName().toString(), userdto.getUserPhone().toString(), mAuth!!.currentUser!!.uid)
                            Application.reloadDatas(baseContext)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
                logUser()
            }
        } else {
            goToLogin()
        }
    }

    companion object {
        private const val SPLASH_SCREEN_DELAY: Long = 2500
    }
}