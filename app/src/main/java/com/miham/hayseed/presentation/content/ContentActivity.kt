package com.miham.hayseed.presentation.content

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.viewpager.widget.ViewPager
import com.miham.hayseed.adapter.PagesAdapter
import com.miham.hayseed.databinding.ActivityContentBinding
import com.miham.hayseed.model.Material
import com.miham.hayseed.model.Page
import com.miham.hayseed.presentation.main.MainActivity
import com.miham.hayseed.repository.Repository
import com.miham.hayseed.utils.disabled
import com.miham.hayseed.utils.enabled
import com.miham.hayseed.utils.invisible
import com.miham.hayseed.utils.visible
import org.jetbrains.anko.startActivity

class ContentActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_MATERIAL = "extra_material"
        const val EXTRA_POSITION = "extra_position"
    }
    private lateinit var contentBinding: ActivityContentBinding
    private lateinit var pagesAdapter: PagesAdapter
    private var currentPosition = 0
    private var materialPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentBinding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(contentBinding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Init
        pagesAdapter = PagesAdapter(this)

        getDataIntent()
        onAction()
        viewPagerCurrentPosition()
    }

    private fun viewPagerCurrentPosition() {
        contentBinding.vpContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val totalIndex = pagesAdapter.count
                currentPosition = position
                val textIndex = "${currentPosition + 1} / $totalIndex"
                contentBinding.tvIndexContent.text = textIndex

                if (currentPosition == 0){
                    contentBinding.btnPrevContent.invisible()
                    contentBinding.btnPrevContent.disabled()
                }else{
                    contentBinding.btnPrevContent.visible()
                    contentBinding.btnPrevContent.enabled()
                }
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun getDataIntent() {
        if (intent != null){
            materialPosition = intent.getIntExtra(EXTRA_POSITION, 0)
            val material = intent.getParcelableExtra<Material>(EXTRA_MATERIAL)

            contentBinding.tvTitleContent.text = material?.titleMaterial

            material?.let { getDataContent(material) }
        }
    }

    private fun getDataContent(material: Material) {
        showLoading()
        val content = material.idMaterial?.let { Repository.getContents(this)?.get(it) }

        Handler(Looper.getMainLooper())
            .postDelayed({
                hideLoading()

                pagesAdapter.pages = content?.pages as MutableList<Page>
                contentBinding.vpContent.adapter = pagesAdapter
                contentBinding.vpContent.setPagingEnabled(false)

                //Init untuk tampilan awal index
                val textIndex = "${currentPosition + 1} / ${pagesAdapter.count}"
                contentBinding.tvIndexContent.text = textIndex
            }, 1200)
    }

    private fun showLoading() {
        contentBinding.swipeContent.isRefreshing = true
    }

    private fun hideLoading() {
        contentBinding.swipeContent.isRefreshing = false
    }

    private fun onAction() {
        contentBinding.apply {
            btnCloseContent.setOnClickListener { finish() }

            btnNextContent.setOnClickListener {
                if (currentPosition < pagesAdapter.count - 1){
                    contentBinding.vpContent.currentItem += 1
                }else{
                    startActivity<MainActivity>(
                        MainActivity.EXTRA_POSITION to materialPosition + 1
                    )
                    finish()
                }
            }

            btnPrevContent.setOnClickListener {
                contentBinding.vpContent.currentItem -= 1
            }

            swipeContent.setOnRefreshListener {
                getDataIntent()
            }
        }
    }
}