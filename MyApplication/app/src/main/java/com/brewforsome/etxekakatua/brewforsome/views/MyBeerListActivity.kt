package com.brewforsome.etxekakatua.brewforsome.views

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import butterknife.OnClick
import com.brewforsome.etxekakatua.brewforsome.R
import com.brewforsome.etxekakatua.brewforsome.Utils.Application
import com.brewforsome.etxekakatua.brewforsome.Utils.PreferencesManager
import com.brewforsome.etxekakatua.brewforsome.Utils.Utils
import com.brewforsome.etxekakatua.brewforsome.adapter.BeerAdapter
import com.brewforsome.etxekakatua.brewforsome.adapter.RecyclerItemClickListener
import com.brewforsome.etxekakatua.brewforsome.databinding.ActivityMyBeerListBinding
import com.brewforsome.etxekakatua.brewforsome.model.BeerDto
import com.firebase.client.Firebase
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

/**
 * Created by etxekakatua on 17/2/18.
 */
class MyBeerListActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private val mAuthListener: FirebaseAuth.AuthStateListener? = null
    var list: MutableList<BeerDto?> = ArrayList()
    var recyclerView: RecyclerView? = null
    var adapter: RecyclerView.Adapter<*>? = null
    var profileBar: AppBarLayout? = null
    var firebase: Firebase? = null
    var databaseReference: DatabaseReference? = null
    private var preferencesManager: PreferencesManager? = null
    var i: Intent? = null

    lateinit var binding: ActivityMyBeerListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.showProgress(this)
        super.onCreate(savedInstanceState)
        ActivityMyBeerListBinding.inflate(layoutInflater).apply {
            setContentView(root)
            binding = this
        }
        mAuth = FirebaseAuth.getInstance()
        preferencesManager = PreferencesManager(this)
        i = intent
        initializeUi()
        loadRecycler()
        loadDatabase()
        Utils.hideProgress()
    }

    private fun loadDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference(BeerListActivity.Database_Path)
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot in snapshot.children) {
                    if (dataSnapshot.key.toString() == mAuth!!.currentUser!!.uid) {
                        for (dataSnapshot2 in dataSnapshot.children) {
                            val beerDetails = dataSnapshot2.getValue(BeerDto::class.java)
                            list.add(beerDetails)
                        }
                    }
                }
                adapter = BeerAdapter(this@MyBeerListActivity, list)
                binding.beerListRecycler.adapter = adapter
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun loadRecycler() {
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        binding.beerListRecycler.layoutManager = mLayoutManager
        binding.beerListRecycler.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        binding.beerListRecycler.adapter = adapter
        binding.beerListRecycler.addOnItemTouchListener(
                RecyclerItemClickListener(this, binding.beerListRecycler, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val beerClicked = list[position]
                        val i = Intent(this@MyBeerListActivity, NewBeerActivity::class.java)
                        i.putExtra("beerName", beerClicked!!.getName().toString())
                        i.putExtra("beerStyle", beerClicked.getStyle())
                        i.putExtra("beerEmail", beerClicked.getUserEmail())
                        i.putExtra("beerPhone", beerClicked.getPhone())
                        i.putExtra("beerBrewerName", beerClicked.getBrewerName())
                        i.putExtra("beerHop", beerClicked.getHop())
                        i.putExtra("beerMalt", beerClicked.getMalt())
                        i.putExtra("beerExtra", beerClicked.getExtra())
                        i.putExtra("beerInterests", beerClicked.getInterests())
                        i.putExtra("beerComments", beerClicked.getComments())
                        i.putExtra("beerYeast", beerClicked.getYeast())
                        i.putExtra("type", "edit")
                        startActivity(i)
                    }

                    override fun onLongItemClick(view: View, position: Int) {
                        // do whatever
                    }
                })
        )
    }

    private fun initializeUi() {
        binding.listSearchCloseBtn.setOnClickListener { listSearchCloseClick() }
        binding.userLogoutBtn.setOnClickListener { logoutClick() }
        binding.profileEditBtn.setOnClickListener { profileEditClick() }
        binding.listNewBtn.setOnClickListener { newBeerClick() }
        binding.listListTitle.text = getString(R.string.myTitleTxt)
        binding.profileBrewerNameTxt.text = Application.name
        binding.profileBrewerMailTxt .text = Application.email
        binding.profileBrewerPhoneTxt.text = Application.phone
        binding.listSearchCloseBtn.setImageResource(R.drawable.black_arrow)
        profileBar = findViewById<View>(R.id.myProfileAppbar) as AppBarLayout
        profileBar!!.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isVisible = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset > 0) {
                    binding.listListTitle.text = getString(R.string.myProfileTxt)
                    isVisible = true
                } else if (isVisible) {
                    binding.listListTitle.setText(R.string.myBeersTxt)
                    isVisible = false
                }
            }
        })
    }

    fun newBeerClick() {
        val i = Intent(this, NewBeerActivity::class.java)
        startActivity(i)
    }

    fun profileEditClick() {
        val i = Intent(this, UserActivity::class.java)
        startActivity(i)
    }

    fun logoutClick() {
        preferencesManager!!.saveUserDatas(null, null, null, null, null)
        Application.reloadDatas(baseContext)
        mAuth!!.signOut()
        val i = Intent(this@MyBeerListActivity, LoginActivity::class.java)
        startActivity(i)
    }

    fun listSearchCloseClick() {
        val i = Intent(this, BeerListActivity::class.java)
        startActivity(i)
        overridePendingTransition(R.transition.turn_left_in, R.transition.turn_left_out)
    }

    inner class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }

    override fun onBackPressed() {
        if (i!!.getStringExtra("user") != null) {
            val j = Intent(this@MyBeerListActivity, UserActivity::class.java)
            startActivity(j)
            overridePendingTransition(R.transition.turn_right_in, R.transition.turn_right_out)
        } else {
            val j = Intent(this@MyBeerListActivity, BeerListActivity::class.java)
            startActivity(j)
            overridePendingTransition(R.transition.turn_right_in, R.transition.turn_right_out)
        }
    }

    companion object {
        const val Firebase_Server_URL = "https://brewforsome-1b53b.firebaseio.com/"
        const val Database_Path = "Beers"
    }
}