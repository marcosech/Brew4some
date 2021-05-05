package com.brewforsome.etxekakatua.brewforsome.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import com.brewforsome.etxekakatua.brewforsome.R
import com.brewforsome.etxekakatua.brewforsome.Utils.DatabaseUtils
import com.brewforsome.etxekakatua.brewforsome.Utils.Utils
import com.brewforsome.etxekakatua.brewforsome.databinding.ActivityNewBeerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewBeerActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    var i: Intent? = null

    lateinit var binding: ActivityNewBeerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityNewBeerBinding.inflate(layoutInflater).apply {
            setContentView(root)
            binding = this
            initializeUi(this)
        }

        mAuth = FirebaseAuth.getInstance()
        initializeFirebase()
        i = intent
        if (i != null) {
            if (i!!.getStringExtra("type") != null) if (i!!.getStringExtra("type") == "edit") loadEditData() else loadNewBeer()
        } else {
            loadNewBeer()
        }
    }

    private fun initializeUi(binding: ActivityNewBeerBinding) {
        binding.editBtn.setOnClickListener{ editBeerClick()}
        binding.cancelBtn.setOnClickListener{ cancelBeerClick()}
        binding.newCloseBtn.setOnClickListener{ newCloseClick()}
        binding.deleteBtn.setOnClickListener{ deleteBeerClick()}
        binding.newBeerBtn.setOnClickListener{ newBeerClick()}
    }

    private fun loadNewBeer() {
        binding.newNameTxt.isEnabled = true
        binding.newStyleTxt.isEnabled = true
        binding.newComentTxt.isEnabled = true
        binding.newMaltsTxt.isEnabled = true
        binding.newHopsTxt.isEnabled = true
        binding.newYeastTxt.isEnabled = true
        binding.newExtrasTxt.isEnabled = true
        binding.newAbvTxt.isEnabled = true
        binding.newInterestsTxt.isEnabled = true
        binding.newLocationTxt.isEnabled = true
        binding.newBeerBtn.visibility = View.VISIBLE
        binding.editBtn.visibility = View.GONE
    }

    private fun loadEditData() {
        binding.newNameTxt.setText(i!!.getStringExtra("beerName"))
        binding.newNameTxt.isEnabled = false
        binding.newStyleTxt.setText(i!!.getStringExtra("beerStyle"))
        binding.newStyleTxt.isEnabled = false
        binding.newComentTxt.setText(i!!.getStringExtra("beerComments"))
        binding.newComentTxt.isEnabled = false
        binding.newMaltsTxt.setText(i!!.getStringExtra("beerMalt"))
        binding.newMaltsTxt.isEnabled = false
        binding.newHopsTxt.setText(i!!.getStringExtra("beerHop"))
        binding.newHopsTxt.isEnabled = false
        binding.newAbvTxt.setText(i!!.getStringExtra("beerAbv"))
        binding.newAbvTxt.isEnabled = false
        binding.newYeastTxt.setText(i!!.getStringExtra("beerYeast"))
        binding.newYeastTxt.isEnabled = false
        binding.newExtrasTxt.setText(i!!.getStringExtra("beerExtra"))
        binding.newExtrasTxt.isEnabled = false
        binding.newInterestsTxt.setText(i!!.getStringExtra("beerInterests"))
        binding.newInterestsTxt.isEnabled = false
        binding.newLocationTxt.setText(i!!.getStringExtra("beerInterests"))
        binding.newLocationTxt.isEnabled = false
        binding.editBtn.visibility = View.VISIBLE
        binding.newBeerBtn.visibility = View.GONE
    }

    fun editBeerClick() {
        binding.newStyleTxt.isEnabled = true
        binding.newComentTxt.isEnabled = true
        binding.newMaltsTxt.isEnabled = true
        binding.newHopsTxt.isEnabled = true
        binding.newYeastTxt.isEnabled = true
        binding.newExtrasTxt.isEnabled = true
        binding.newAbvTxt.isEnabled = true
        binding.newInterestsTxt.isEnabled = true
        binding.newLocationTxt.isEnabled = true
        binding.newBeerBtn.visibility = View.VISIBLE
        binding.editBtn.visibility = View.GONE
        binding.deleteBtn.visibility = View.GONE
        binding.cancelBtn.visibility = View.VISIBLE

    }

    fun cancelBeerClick() {
        binding.newStyleTxt.isEnabled = true
        binding.newComentTxt.isEnabled = true
        binding.newMaltsTxt.isEnabled = true
        binding.newHopsTxt.isEnabled = true
        binding.newYeastTxt.isEnabled = true
        binding.newExtrasTxt.isEnabled = true
        binding.newAbvTxt.isEnabled = true
        binding.newInterestsTxt.isEnabled = true
        binding.newLocationTxt.isEnabled = true
        binding.newBeerBtn!!.visibility = View.GONE
        binding.editBtn!!.visibility = View.VISIBLE
        binding.deleteBtn!!.visibility = View.GONE
        binding.cancelBtn!!.visibility = View.VISIBLE
    }

    fun newCloseClick() {
        super.onBackPressed()
    }

    fun deleteBeerClick() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = mAuth!!.currentUser!!.uid
            val currentUserBeerCharacteristics = FirebaseDatabase.getInstance().reference.child("Beers").child(userId).child(binding.newNameTxt.text.toString())
            currentUserBeerCharacteristics.setValue(null)
        }
        Utils.createToast(this@NewBeerActivity, getString(R.string.editBeerDeleted))
        val i = Intent(this@NewBeerActivity, MyBeerListActivity::class.java)
        startActivity(i)
    }

    fun newBeerClick() {
        createNewBeer(binding.newNameTxt.text.toString(), binding.newStyleTxt.text.toString(), binding.newComentTxt.text.toString(), binding.newMaltsTxt.text.toString(), binding.newHopsTxt.text.toString(),
                binding.newYeastTxt.text.toString(), binding.newExtrasTxt.text.toString(), binding.newInterestsTxt.text.toString(), binding.newAbvTxt.text.toString(), binding.newLocationTxt.text.toString())
    }

    fun createNewBeer(name: String?, style: String?, comments: String?, malt: String?, hop: String?, yeast: String?, extra: String?, interests: String?, abv: String?, location: String?) {
        if (Utils.isEmpty(name)) {
            Utils.createToast(this, getString(R.string.mustName))
        } else if (Utils.isEmpty(style)) {
            Utils.createToast(this, getString(R.string.mustStyle))
        } else if (Utils.isEmpty(interests)) {
            Utils.createToast(this, getString(R.string.mustInterests))
        } else if (Utils.isEmpty(location)) {
            Utils.createToast(this, getString(R.string.mustLocation))
        } else {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val userId = mAuth!!.currentUser!!.uid
                val userName = FirebaseDatabase.getInstance().getReference("Users")
                DatabaseUtils.createNewBeer(name, style, comments, malt, hop, yeast, extra, interests, abv, location, userId, mAuth)
            }
            goToMyList()
        }
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

    override fun onBackPressed() {
        if (i!!.getStringExtra("type") == "edit") goToMyList() else goToBeerList()
    }

    private fun goToBeerList() {
        val i = Intent(this@NewBeerActivity, BeerListActivity::class.java)
        startActivity(i)
    }

    fun goToMyList() {
        val i = Intent(this@NewBeerActivity, MyBeerListActivity::class.java)
        startActivity(i)
    }

    companion object {
        const val BEERNAME = "name"
        const val BEERSTYLE = "style"
        const val BEERCOMMENTS = "beerComments"
        const val BEERMALT = "beerMalt"
        const val BEERHOP = "beerHop"
        const val BEERYEAST = "beerYeast"
        const val BEEREXTRA = "beerExtra"
        const val BEERINTERESTS = "beerInterests"
        private const val REQUEST_IMAGE_CAPTURE = 111
    }
}