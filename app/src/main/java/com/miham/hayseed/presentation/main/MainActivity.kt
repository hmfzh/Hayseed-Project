package com.miham.hayseed.presentation.main

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.miham.hayseed.adapter.MaterialsAdapter
import com.miham.hayseed.databinding.ActivityMainBinding
import com.miham.hayseed.model.User
import com.miham.hayseed.presentation.content.ContentActivity
import com.miham.hayseed.presentation.user.UserActivity
import com.miham.hayseed.repository.Repository
//import com.miham.hayseed.utils.showDialogError
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

  companion object{
    const val EXTRA_POSITION = "extra_position"
  }
  private lateinit var mainBinding: ActivityMainBinding
  private lateinit var materialsAdapter: MaterialsAdapter
  private lateinit var userDatabase: DatabaseReference
  private var currentUser: FirebaseUser? = null

  private var listenerUser = object : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
      hideLoading()
      val user = snapshot.getValue(User::class.java)
      user?.let {
        mainBinding.apply {
          tvNameUserMain.text = it.nameUser

          Glide
            .with(this@MainActivity)
            .load(it.avatarUser)
            .placeholder(android.R.color.darker_gray)
            .into(ivAvatarMain)
        }
      }
    }

    override fun onCancelled(error: DatabaseError) {
      hideLoading()
      Log.e("MainActivity", "[onCancelled] - ${error.message}")
      //showDialogError(this@MainActivity, error.message)
      toast("Gagal Akses")
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mainBinding.root)
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    //Init
    materialsAdapter = MaterialsAdapter()
    userDatabase = FirebaseDatabase.getInstance().getReference("users")
    currentUser = FirebaseAuth.getInstance().currentUser

    getDataFirebase()
    getDataMaterial()
    onAction()
  }

  private fun getDataFirebase() {
    showLoading()
    userDatabase
      .child(currentUser?.uid.toString())
      .addValueEventListener(listenerUser)
  }

  override fun onResume() {
    super.onResume()
    if (intent != null){
      val position = intent.getIntExtra(EXTRA_POSITION, 0)
      mainBinding.rvMaterialsMain.smoothScrollToPosition(position)
    }
  }

  private fun getDataMaterial() {
    showLoading()

    val materials = Repository.getMaterials(this)

    Handler(Looper.getMainLooper())
      .postDelayed({
        hideLoading()
        materials?.let {
          materialsAdapter.materials = it
        }
      }, 1200)

    mainBinding.rvMaterialsMain.adapter = materialsAdapter
  }

  private fun showLoading() {
    mainBinding.swipeMain.isRefreshing = true
  }

  private fun hideLoading() {
    mainBinding.swipeMain.isRefreshing = false
  }

  private fun onAction() {
    mainBinding.apply {
      ivAvatarMain.setOnClickListener {
        startActivity<UserActivity>()
      }

      etSearchMain.addTextChangedListener {
        materialsAdapter.filter.filter(it.toString())
      }

      etSearchMain.setOnEditorActionListener{_, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
          val dataSearch = etSearchMain.text.toString().trim()
          materialsAdapter.filter.filter(dataSearch)
          return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
      }

      rvMaterialsMain.layoutManager = GridLayoutManager(applicationContext,2)

      swipeMain.setOnRefreshListener {
        getDataFirebase()
        //getDataMaterial()
      }
    }

    materialsAdapter.onClick { material, position ->
      startActivity<ContentActivity>(
        ContentActivity.EXTRA_MATERIAL to material,
        ContentActivity.EXTRA_POSITION to position
      )
    }
  }
}