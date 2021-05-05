package com.brewforsome.etxekakatua.brewforsome.views

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import butterknife.OnClick
import com.brewforsome.etxekakatua.brewforsome.R
import com.brewforsome.etxekakatua.brewforsome.Utils.Utils
import com.brewforsome.etxekakatua.brewforsome.adapter.BeerAdapter
import com.brewforsome.etxekakatua.brewforsome.adapter.RecyclerItemClickListener
import com.brewforsome.etxekakatua.brewforsome.databinding.ActivityBeerListBinding
import com.brewforsome.etxekakatua.brewforsome.model.BeerDto
import com.brewforsome.etxekakatua.brewforsome.views.BeerListActivity
import com.brewforsome.etxekakatua.brewforsome.views.MyBeerListActivity
import com.brewforsome.etxekakatua.brewforsome.views.NewBeerActivity
import com.firebase.client.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

/**
 * Created by etxekakatua on 19/12/17.
 */
class BeerListActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private val mAuthListener: FirebaseAuth.AuthStateListener? = null
    var list: MutableList<BeerDto?> = ArrayList()

    lateinit var adapter: BeerAdapter
    var scroll: ScrollView? = null
    var firebase: Firebase? = null
    var databaseReference: DatabaseReference? = null

    lateinit var binding: ActivityBeerListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityBeerListBinding.inflate(layoutInflater).apply {
            setContentView(root)
            binding = this
            initializeUi()
        }
        mAuth = FirebaseAuth.getInstance()
        //        configureRecycler();
        loadListData()
        addTextListener()
    }

    private fun initializeUi() {
        binding.listNewBtn.setOnClickListener { newBeerClick() }
        binding.listMyBeersBtn.setOnClickListener { listMyBeersClick() }
        binding.listSearchCloseBtn.setOnClickListener { listSearchCloseClick() }
        binding.listListTitle.text = "ALL BEERS"
        binding.listSearchCloseBtn.visibility = View.VISIBLE
    }

    private fun loadListData() {
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path)
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot in snapshot.children) {
                    for (dataSnapshot2 in dataSnapshot.children) {
                        val beerDetails = dataSnapshot2.getValue(BeerDto::class.java)
                        list.add(beerDetails)
                    }
                }
                adapter = BeerAdapter(this@BeerListActivity, list)
                binding.beerListRecycler.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        configureRecycler()
        Utils.hideProgress()
    }

    private fun configureRecycler() {
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        binding.beerListRecycler.layoutManager = mLayoutManager
        binding.beerListRecycler.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        binding.beerListRecycler.adapter = adapter
        binding.beerListRecycler.addOnItemTouchListener(
                RecyclerItemClickListener(this,  binding.beerListRecycler, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val beerClicked = list[position]
                        val i = Intent(this@BeerListActivity, BeerDetailActivity::class.java)
                        i.putExtra("beerName", beerClicked!!.getName().toString())
                        i.putExtra("beerStyle", beerClicked.getStyle())
                        i.putExtra("beerEmail", beerClicked.getUserEmail())
                        i.putExtra("beerPhone", beerClicked.getPhone())
                        i.putExtra("beerBrewerName", beerClicked.getBrewerName())
                        i.putExtra("beerAbv", beerClicked.getAbv())
                        i.putExtra("beerHop", beerClicked.getHop())
                        i.putExtra("beerMalt", beerClicked.getMalt())
                        i.putExtra("beerExtra", beerClicked.getExtra())
                        i.putExtra("beerInterests", beerClicked.getInterests())
                        i.putExtra("beerComments", beerClicked.getComments())
                        i.putExtra("beerYeast", beerClicked.getYeast())
                        i.putExtra("beerLocation", beerClicked.getLocation())
                        startActivity(i)
                    }

                    override fun onLongItemClick(view: View, position: Int) {
                        // do whatever
                    }
                })
        )
    }

    fun newBeerClick() {
        val i = Intent(this, NewBeerActivity::class.java)
        startActivity(i)
    }

    fun listMyBeersClick() {
        val i = Intent(this, MyBeerListActivity::class.java)
        startActivity(i)
        overridePendingTransition(R.transition.turn_right_in, R.transition.turn_right_out)
    }

    fun listSearchCloseClick() {
        if (binding.listSearchTxt.visibility == View.GONE) {
            binding.listSearchTxt.visibility = View.VISIBLE
            binding.listSearchCloseBtn.setImageResource(R.drawable.close_icon)
            binding.listListTitle.visibility = View.GONE
            scroll!!.post { scroll!!.fullScroll(View.FOCUS_UP) }
        } else {
            binding.listSearchTxt.visibility = View.GONE
            binding.listSearchTxt.setText("")
            binding.listSearchCloseBtn.setImageResource(R.drawable.magnifying_icon)
            binding.listListTitle.visibility = View.VISIBLE
            scroll!!.post { scroll!!.fullScroll(View.FOCUS_UP) }
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.listSearchTxt.getWindowToken(), 0)
        }
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

    fun addTextListener() {
        binding.listSearchTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {
                var query = query
                query = query.toString().toLowerCase()
                val filteredList: MutableList<BeerDto?> = ArrayList()
                for (i in list.indices) {
                    val title = list[i]!!.getName().toLowerCase()
                    val style = list[i]!!.getStyle().toLowerCase()
                    val location = list[i]!!.getLocation().toLowerCase()
                    val interests = list[i]!!.getInterests().toLowerCase()
                    //final String brewer = list.get(i).getBrewerName().toLowerCase();
                    if (title.contains(query) || style.contains(query) || location.contains(query) || interests.contains(query)) {
                        filteredList.add(list[i])
                    }
                }
                adapter = BeerAdapter(this@BeerListActivity, filteredList)
                binding.beerListRecycler.adapter = adapter
                adapter.notifyDataSetChanged()

                // data set changed
            }
        })
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }

    override fun onBackPressed() {}

    companion object {
        const val Firebase_Server_URL = "https://brewforsome-1b53b.firebaseio.com/"
        const val Database_Path = "Beers"
    }
}