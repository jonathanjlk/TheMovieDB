package com.jonathan.themoviedb.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jonathan.themoviedb.R
import com.jonathan.themoviedb.ui.movie_details.DetailsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_open_details.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("id", 475557)
            this.startActivity(intent)
        }
    }
}
