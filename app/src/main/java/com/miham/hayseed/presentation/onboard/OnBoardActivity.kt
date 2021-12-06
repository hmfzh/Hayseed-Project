package com.miham.hayseed.presentation.onboard

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.miham.hayseed.R
import com.miham.hayseed.databinding.ActivityOnBoardBinding
import com.miham.hayseed.databinding.ActivityRegisterBinding
import com.miham.hayseed.presentation.login.LoginActivity
import com.miham.hayseed.presentation.main.MainActivity
import com.miham.hayseed.utils.Preferences
import org.jetbrains.anko.startActivity
import org.miham.hayseed.onBoard.model.OnBoard
import org.miham.hayseed.onBoard.model.OnBoardAdapter

class OnBoardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOnBoardBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dialogLoading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        firebaseAuth = FirebaseAuth.getInstance()

        with(binding.recyclerViewOb) {
            binding.recyclerViewOb.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = OnBoardAdapter(getData())
            setHasFixedSize(true)
        }

        binding.apply {
            btnLanjut.setOnClickListener { startActivity<LoginActivity>() }
        }
//        btnLanjut = findViewById(R.id.btn_lanjut)

    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            startActivity<MainActivity>()
            finishAffinity()
        }
    }

    private fun getData(): List<OnBoard> {
        return listOf(
            OnBoard(R.drawable.semangka_pic),
            OnBoard(R.drawable.stroberi_pic),
            OnBoard(R.drawable.melon_pic),
            OnBoard(R.drawable.semangka_pic),
            OnBoard(R.drawable.stroberi_pic),
            OnBoard(R.drawable.melon_pic))
    }
}