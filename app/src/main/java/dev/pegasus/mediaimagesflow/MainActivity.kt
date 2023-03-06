package dev.pegasus.mediaimagesflow

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import dev.pegasus.mediaimagesflow.adapters.AdapterPicture
import dev.pegasus.mediaimagesflow.databinding.ActivityMainBinding
import dev.pegasus.mediaimagesflow.interfaces.OnPictureClickListener
import dev.pegasus.mediaimagesflow.models.Picture
import dev.pegasus.mediaimagesflow.viewModels.MediaViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val adapter by lazy { AdapterPicture(itemClick) }
    private val viewModel: MediaViewModel by viewModels()

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkStoragePermission()
        initRecyclerView()
        initObservers()
    }

    private fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted()
            } else {
                permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted()
            } else {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun onPermissionGranted() {
        viewModel.fetchMediaImages()
    }

    private fun initRecyclerView() {
        binding.rvListMain.adapter = adapter
    }

    private fun initObservers() {
        viewModel.mediaImages.onEach { images ->
            binding.progressBarMain.visibility = View.GONE
            adapter.submitList(images)
        }.launchIn(lifecycleScope)
    }

    private val itemClick = object : OnPictureClickListener {
        override fun onPictureClick(picture: Picture) {
            Toast.makeText(this@MainActivity, picture.fileName, Toast.LENGTH_SHORT).show()
        }
    }
}