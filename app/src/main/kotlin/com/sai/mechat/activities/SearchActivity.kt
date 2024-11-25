package com.sai.mechat.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sai.mechat.R
import com.sai.mechat.adapters.SearchAdapter
import com.sai.mechat.databinding.ActivitySearchBinding
import com.sai.mechat.fragments.MirrorFragment
import com.sai.mechat.models.User

class SearchActivity : AppCompatActivity() {
    private lateinit var views : ActivitySearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(views.root)

        val db = FirebaseDatabase.getInstance().getReference("users")
        val q = db.orderByChild("userName")
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        views.searchRecycleView.layoutManager = LinearLayoutManager(this)

        views.searchInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                q.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val arrayList = ArrayList<User>()
                        for (data in snapshot.children) {
                            val user = data.getValue(User::class.java)
                            if (user != null) {
                                if (s?.let { user.userName?.contains(it,ignoreCase = true) } == true && !user.uid.equals(uid)){
                                    arrayList.add(user)
                                }
                            }
                        }
                        val searchAdapter = SearchAdapter(arrayList,s.toString(),this@SearchActivity)
                        views.searchRecycleView.adapter = searchAdapter
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

    }
    fun showFragment(fragment: MirrorFragment){
        views.fragmentHolder.visibility = View.VISIBLE
        views.searchLayout.visibility = View.GONE
        supportFragmentManager.beginTransaction().replace(R.id.fragment,fragment)
            .commit()

    }
}