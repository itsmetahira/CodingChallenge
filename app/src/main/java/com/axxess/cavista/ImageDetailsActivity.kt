package com.axxess.cavista

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_details.*

/**
 * Activity for Image Details screen
 */
class ImageDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private var mImage: ImageModel = ImageModel()
    private lateinit var mAdapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)

        mImage.imageId = intent.getStringExtra(ImageModel.IMAGE_ID_KEY)
        mImage.imageUrl = intent.getStringExtra(ImageModel.IMAGE_URL_KEY)
        mImage.imageTitle = intent.getStringExtra(ImageModel.IMAGE_TITLE_KEY)

        btn_submit.setOnClickListener(this)

        //Change title of activity to Name of image
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.title = mImage.imageTitle

        // load the image with Picasso
        Picasso.get()
                .load(mImage.imageUrl) // load the image
                .into(iv_image)

        loadImageComments()

        //Initialize the comments RecyclerView
        rv_comments.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            // set the custom adapter to the RecyclerView
            mAdapter = CommentsAdapter(mImage.comments)
            adapter = mAdapter
            //setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(rv_comments.getContext(), DividerItemDecoration.VERTICAL))
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_submit -> {

                val comment: String? = et_comment.text.toString()
                if (comment == null || comment.trim().isEmpty()) {
                    Toast.makeText(this, R.string.error_comment, Toast.LENGTH_LONG).show()
                } else {
                    mImage.addComment(comment)
                    addComment(comment)
                }
            }
        }
    }

    /**
     * Add comment to Database and update UI
     */
    fun addComment(comment: String) {
        val dbHandler = MyDBHandler(this, null, null, 1)
        dbHandler.addComment(mImage.imageId, comment)

        et_comment.text.clear()
        hideKeyboard()
        Toast.makeText(this, R.string.comment_added, Toast.LENGTH_LONG).show()

        mAdapter.notifyDataSetChanged()
    }

    /**
     * Load Comments from database
     */
    fun loadImageComments() {
        val dbHandler = MyDBHandler(this, null, null, 1)

        mImage.comments = dbHandler.loadImageComments(
                mImage.imageId)
    }

    /**
     * Hide keyboard
     */
    fun Context.hideKeyboard() {
        val view: View? = currentFocus
        if(view != null) {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}