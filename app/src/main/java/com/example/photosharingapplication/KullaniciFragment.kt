package com.example.photosharingapplication

import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.photosharingapplication.databinding.FragmentKullaniciBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class KullaniciFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentKullaniciBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth

    }

    override fun onStart() {
        super.onStart()

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKullaniciBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val guncelKullanici=auth.currentUser
        if (guncelKullanici!=null){
            val action= KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
            Navigation.findNavController(view).navigate(action)
        }

        binding.kayitOlButton.setOnClickListener {
            val email=binding.emailText.text.toString()
            val password=binding.sifreText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task ->
                if (task.isSuccessful){
                    val action= KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                    Navigation.findNavController(view).navigate(action)
                }
                }.addOnFailureListener {exception->
                   Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
                    exception.printStackTrace()
                }
            }


        }

        binding.girisYapButton.setOnClickListener {
            val email=binding.emailText.text.toString()
            val password=binding.sifreText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        val action= KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                        Navigation.findNavController(view).navigate(action)
                    }
                }.addOnFailureListener { exception->
                    Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}