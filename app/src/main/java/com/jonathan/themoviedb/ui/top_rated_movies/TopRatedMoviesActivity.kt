package com.jonathan.themoviedb.ui.top_rated_movies

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.jonathan.themoviedb.R
import com.jonathan.themoviedb.data.api.Interface
import com.jonathan.themoviedb.data.api.TheMovieDBClient
import com.jonathan.themoviedb.data.repository.NetworkState
import kotlinx.android.synthetic.main.movie_list_activity.*
import java.util.*
import kotlin.collections.ArrayList

class TopRatedMoviesActivity : AppCompatActivity() {

    private lateinit var viewModel: TopRatedMoviesViewModel

    lateinit var movieRepository: TopRatedMoviesPagedListRepository

    internal lateinit var sp: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_list_activity)

        val apiService: Interface = TheMovieDBClient.getClient()

        movieRepository = TopRatedMoviesPagedListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = TopRatedMoviesPagedListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, 3)

        val mPickTimeFromBtn = findViewById<Button>(R.id.pickDateFromBtn)
        val mPickTimeToBtn = findViewById<Button>(R.id.pickDateToBtn)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        mPickTimeFromBtn.setOnClickListener {

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    mPickTimeFromBtn.text = "$year-$month-$dayOfMonth"
                },
                year,
                month,
                day
            )
            dpd.show()

        }

        mPickTimeToBtn.setOnClickListener {

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    mPickTimeToBtn.text = "$year-$month-$dayOfMonth"
                },
                year,
                month,
                day
            )
            dpd.show()

        }

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                return if (viewType == movieAdapter.MOVIE_VIEW_TYPE) 1 else 3
            }
        }

        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): TopRatedMoviesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TopRatedMoviesViewModel(movieRepository) as T
            }
        })[TopRatedMoviesViewModel::class.java]
    }

    private fun getYears(): MutableList<String> {
        val years: MutableList<String> = ArrayList()
        for (year in 1800..2019) {
            years.add("$year-01-01")
        }
        return years
    }
}
