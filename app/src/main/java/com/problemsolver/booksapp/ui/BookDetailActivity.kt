package com.problemsolver.booksapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.problemsolver.booksapp.R
import com.problemsolver.booksapp.data.model.Status
import com.problemsolver.booksapp.databinding.ActivityBookDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailActivity : AppCompatActivity(R.layout.activity_book_detail) {

    private val binding by viewBinding(ActivityBookDetailBinding::bind)
    private val viewModel: BooksViewModel by viewModels()
    private var sharedBookDetail = ""
    private var imageUrl = ""
    private var key = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        key = intent.getStringExtra("key") ?: ""
        imageUrl = intent.getStringExtra("imageUrl") ?: ""
        initObserver()
        initData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_share -> {
                shareContent(sharedBookDetail)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initData() {
        viewModel.getBookDetail(key)
    }

    private fun initObserver() {
        viewModel.bookDetail.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.veilLayout.veil()
                }

                Status.SUCCESS -> {
                    binding.veilLayout.unVeil()
                    it.data?.let { bookDetail ->
                        sharedBookDetail = bookDetail.getAllContent()
                        with(binding) {
                            ivCover.load(imageUrl) {
                                crossfade(true)
                            }
                            tvTitle.text = bookDetail.title
                            tvDescription.text = bookDetail.description?.value
                            bookDetail.subjects?.let { subjects ->
                                rvSubjects.adapter = SubjectAdapter(subjects)
                            }
                        }
                    }
                }

                Status.ERROR -> {
                    binding.veilLayout.unVeil()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun shareContent(content: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)

        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}