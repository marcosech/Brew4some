package com.brewforsome.etxekakatua.brewforsome.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.OnClick
import com.brewforsome.etxekakatua.brewforsome.R
import com.brewforsome.etxekakatua.brewforsome.Utils.Utils
import com.brewforsome.etxekakatua.brewforsome.databinding.ActivityForgotBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by etxekakatua on 4/3/18.
 */
class ForgotActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityForgotBinding.inflate(layoutInflater).apply {
            binding = this
            initializeUi()
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun initializeUi() {
        binding.forgotBtn.setOnClickListener { forgotClick() }
        binding.forgotLoginBtn.setOnClickListener { loginClick() }
    }

    fun loginClick() {
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    @OnClick(R.id.forgotBtn)
    fun forgotClick() {
        Utils.showProgress(this)
        recoverPass(binding.forgotUserTxt.text.toString())
    }

    private fun recoverPass(email: String) {
        if (!Utils.checkEmailFormat(email)) {
            Utils.createToast(this, getString(R.string.emailErrorFormat))
            Utils.hideProgress()
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Utils.createToast(this@ForgotActivity, "mail enviado")
                            Utils.hideProgress()
                        } else {
                            Utils.createToast(this@ForgotActivity, "ERROR")
                            Utils.hideProgress()
                        }
                    }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}