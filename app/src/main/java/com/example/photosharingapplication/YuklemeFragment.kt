package com.example.photosharingapplication

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.photosharingapplication.databinding.FragmentYuklemeBinding
import com.google.android.material.snackbar.Snackbar

class YuklemeFragment : Fragment() {

    private var _binding: FragmentYuklemeBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var secilenGorsel: Uri? =null
    var secilenBitmap: Bitmap? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLaunchers()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYuklemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.yukleButton.setOnClickListener {
            // Yükle butonu tıklandığında yapılacak işlemler
            
        }

        binding.imageView2.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13 (API 33) ve sonrası için
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        )
                    ) {
                        Snackbar.make(
                            view,
                            "Medyaya erişim iznini ekleyeceğiniz yemek tarifi için istiyoruz",
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction("İzin Ver") {
                            permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                        }.show()

                    } else {
                        // İzin isteyeceğiz
                        permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                    }
                } else {
                    // İzin verilmiş, galeriye gidilebilir.
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        Snackbar.make(
                            view,
                            "Medyaya erişim istiyoruz.",
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction("İzin Ver") {
                            // İzin isteyeceğiz
                            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        }.show()

                    } else {
                        // İzin isteyeceğiz
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                } else {
                    // İzin verilmiş, galeriye gidilebilir.
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                }
            }
        }
    }

    private fun registerLaunchers(){
      activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
          if (result.resultCode==RESULT_OK){
              val intentFromResult = result.data
              if (intentFromResult != null){
                 secilenGorsel=intentFromResult.data
                  try {
                      if (Build.VERSION.SDK_INT >= 28){
                        val source=ImageDecoder.createSource(requireActivity().contentResolver,secilenGorsel!!)
                          secilenBitmap=ImageDecoder.decodeBitmap(source)
                          binding.imageView2.setImageBitmap(secilenBitmap)
                      }else{
                          secilenBitmap=MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,secilenGorsel)
                          binding.imageView2.setImageBitmap(secilenBitmap)
                      }

                  }catch (e:Exception){
                      e.printStackTrace()
                  }
              }
          }
      }
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if (result){
                //izin verildi
                val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                //kullanıcı izni reddetti
                Toast.makeText(requireContext(),"İzni reddettiniz,uygulamaya devam etmek için izin vermeniz gerekiyor !",Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
