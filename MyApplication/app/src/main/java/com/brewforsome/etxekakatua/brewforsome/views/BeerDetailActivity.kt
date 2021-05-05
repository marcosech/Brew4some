package com.brewforsome.etxekakatua.brewforsome.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import butterknife.OnClick
import com.brewforsome.etxekakatua.brewforsome.R
import com.brewforsome.etxekakatua.brewforsome.Utils.Utils
import com.brewforsome.etxekakatua.brewforsome.databinding.ActivityBeerDetailBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by etxekakatua on 29/1/18.
 */
class BeerDetailActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    lateinit var binding: ActivityBeerDetailBinding

    var brewerName = ""
    var email = ""
    var phone = ""

    private val i: Intent? = null

    lateinit var emailFrom: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityBeerDetailBinding.inflate(layoutInflater).apply {
            setContentView(root)
            binding = this
            initializeUI()
        }
        loadData()
    }

    private fun initializeUI() {
        binding.emailBtn.setOnClickListener{ emailBeerClick()}
        binding.detailCloseBtn.setOnClickListener{ detailCloseClick()}
        binding.detailReachBtn.setOnClickListener{ detailReachClick()}
        binding.emailBtn.setOnClickListener{ emailBeerClick()}
        binding.phoneBtn.setOnClickListener{ phoneBeerClick()}
    }

    private fun loadData() {
        var i = intent
        binding.detailBeerNameTxt.text = i.getStringExtra("beerName")
        binding.detailBeerStyleTxt.text = i.getStringExtra("beerStyle")
        binding.detailBeerLocationTxt.text = i.getStringExtra("beerLocation")
        binding.detailBeerInterestsTxt.text = i.getStringExtra("beerInterests")
        if (i.getStringExtra("beerComments") == "") {
            binding.detailCommentLay.visibility = View.GONE
        } else {
           binding.detailBeerCommentsTxt.text = i.getStringExtra("beerComments")
        }
        if (i.getStringExtra("beerHop") == "") {
            binding.detailBeerHopsTxt.visibility = View.GONE
            binding.detailHopLay.visibility = View.GONE
        } else {
            binding.detailBeerHopsTxt.text = i.getStringExtra("beerHop")
        }
        if (i.getStringExtra("beerMalt") == "") {
            binding.detailBeerMaltsTxt.visibility = View.GONE
            binding.detailMaltLay.visibility = View.GONE
        } else {
            binding.detailBeerMaltsTxt.text = i.getStringExtra("beerMalt")
        }
        if (i.getStringExtra("beerYeast") == "") {
            binding.detailBeerYeastTxt.visibility = View.GONE
            binding.detailYeastLay.visibility = View.GONE
        } else {
            binding.detailBeerYeastTxt.text = i.getStringExtra("beerYeast")
        }
        if (i.getStringExtra("beerExtra") == "") {
            binding.detailExtraLay.visibility = View.GONE
        } else {
            binding.detailBeerExtrasTxt.text = i.getStringExtra("beerExtra")
        }
        if (i.getStringExtra("beerAbv") == "") {
            binding.detailAbvLay.visibility = View.GONE
        } else {
            binding.detailBeerAbvTxt.text = i.getStringExtra("beerAbv")
        }
        binding.detailBeerBrewerNameTxt.text = i.getStringExtra("beerBrewerName")
        brewerName = i.getStringExtra("beerBrewerName")
        phone = i.getStringExtra("beerPhone")
        email = i.getStringExtra("beerEmail")
        mAuth = FirebaseAuth.getInstance()
        emailFrom = mAuth!!.currentUser!!.email.toString()
    }


    fun emailBeerClick() {
        if (email != null) {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", email, null))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Querría intercambiar contigo tu " + i?.getStringExtra("beerName").toString())
            intent.putExtra(Intent.EXTRA_TEXT, """
     Hola ${i?.getStringExtra("beerBrewerName")}
     Estaría interesado en cambiar contigo tu cerveza ${i!!.getStringExtra("beerName")}
     """.trimIndent())
            startActivity(Intent.createChooser(intent, "Choose an Email client :"))
        } else {
            Utils.createToast(this, "The email cannot be send")
        }
    }

    fun detailCloseClick() {
        super.onBackPressed()
    }

    fun detailReachClick() {
        binding.beerContactLay.visibility = View.VISIBLE
        binding.detailScroll.post { binding.detailScroll.fullScroll(View.FOCUS_DOWN) }
    }

    @OnClick(R.id.phoneBtn)
    fun phoneBeerClick() {
        if (phone != null) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                startActivity(intent)
                return
            }
        } else {
            Utils.createToast(this, "No")
        }
    }
}