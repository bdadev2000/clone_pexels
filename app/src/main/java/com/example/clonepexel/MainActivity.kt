package com.example.clonepexel

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clonepexel.broadcast_reciever.ConnectivityReceiver
import com.example.clonepexel.data.response.Photo
import com.example.clonepexel.data.viewmodel.PexelsViewModel
import com.example.clonepexel.databinding.ActivityMainBinding
import com.example.clonepexel.ui.PhotoAdapter
import com.example.clonepexel.util.StateApi


class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityListener {
    private lateinit var viewModel: PexelsViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var photoAdapter: PhotoAdapter
    private var listPhoto = arrayListOf<Photo>()
    private val connectivityReceiver = ConnectivityReceiver(this)
    private var isAvailableInternet = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[PexelsViewModel::class.java]
        photoAdapter = PhotoAdapter(this@MainActivity)

        binding.rcv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = photoAdapter
        }
        observeViewModel()

        binding.rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItem >= totalItemCount && firstVisibleItem >= 0) {
                    // Load next page
                    viewModel.fetchData(isAvailableInternet)
                }
            }
        })

        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

    }

    private fun observeViewModel() {
        viewModel.stateApi.observe(this) { state ->
            when (state) {
                StateApi.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.txtNoInternet.visibility = View.GONE
                }

                StateApi.LOADED -> {
                    binding.progressBar.visibility = View.GONE
                    binding.txtNoInternet.visibility = View.GONE
                }

                StateApi.ERROR -> {

                }

                StateApi.NO_INTERNET -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rcv.visibility = View.GONE
                    binding.txtNoInternet.visibility = View.VISIBLE
                }
            }
        }
        viewModel.pexelsPhotosLiveData.observe(this) { photos ->
            photos.forEach {
                listPhoto.add(it)
            }
            photoAdapter.setData(listPhoto)
        }

        viewModel.errorLiveData.observe(this) { error ->
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
        listPhoto.clear()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        isAvailableInternet = isConnected
        if (isConnected) {
            viewModel.fetchData(isAvailableInternet)
            binding.progressBar.visibility = View.VISIBLE
            binding.rcv.visibility = View.VISIBLE
            binding.txtNoInternet.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rcv.visibility = View.GONE
            binding.txtNoInternet.visibility = View.VISIBLE
        }
    }

}