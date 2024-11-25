package com.sai.mechat.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sai.mechat.adapters.NotificationAdapter
import com.sai.mechat.databinding.ActivityNotificationsBinding
import com.sai.mechat.models.NotificationModel

class NotificationsActivity : AppCompatActivity() {
    private lateinit var views: ActivityNotificationsBinding
    private var showNotification:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(views.root)
        val fUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseDatabase.getInstance().getReference("users")

        views.notificationRecycleView.layoutManager = LinearLayoutManager(this)
        val itemList = ArrayList<NotificationModel>()

        if (fUser != null) {
            db.child(fUser.uid).child("Notifications").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    itemList.clear()
                    for (data in snapshot.children){
                        val notification = data.getValue(NotificationModel::class.java)
                        notification?.let { itemList.add(it) }
                    }
                    val adapter = NotificationAdapter(this@NotificationsActivity,itemList)
                    views.notificationRecycleView.adapter = adapter

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }






    }

}