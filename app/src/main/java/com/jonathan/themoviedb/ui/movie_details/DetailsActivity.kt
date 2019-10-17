package com.jonathan.themoviedb.ui.movie_details

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.jonathan.themoviedb.R
import com.jonathan.themoviedb.data.api.DetailsClient
import com.jonathan.themoviedb.data.api.DetailsInterface
import com.jonathan.themoviedb.data.api.POSTER_BASE_URL
import com.jonathan.themoviedb.data.repo.NetworkState
import com.jonathan.themoviedb.data.model.MovieDetails
import kotlinx.android.synthetic.main.activity_details.*
import java.text.NumberFormat
import java.util.*


class DetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailsViewModel
    private lateinit var detailsRepo: DetailsRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService: DetailsInterface = DetailsClient.getClient()
        detailsRepo = DetailsRepo(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer { bindUI(it) })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    fun bindUI(it: MovieDetails) {
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + " minutes"
        movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster);
    }

    private fun getViewModel(movieId: Int): DetailsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetailsViewModel(
                    detailsRepo,
                    movieId
                ) as T
            }
        })[DetailsViewModel::class.java]
    }
}

