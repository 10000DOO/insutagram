package com.example.insutagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.insutagram.databinding.ActivityMainBinding
import com.example.insutagram.fragments.HomeFragment
import com.example.insutagram.fragments.ProfileFragment
import com.example.insutagram.fragments.SearchFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val SEARCH = "SearchFragment"
private const val HOME = "HomeFragment"
private const val PROFILE = "ProfileFragment"

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인이 안되어있으면 로그인 화면으로 이동
        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

        setFragment(HOME,HomeFragment())
        binding.navbar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home_icon -> setFragment(HOME, HomeFragment())
                R.id.search_icon -> setFragment(SEARCH, SearchFragment())
                R.id.profile_icon -> setFragment(PROFILE, ProfileFragment())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()
        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.contents, fragment, tag)
        }

        val profile = manager.findFragmentByTag(PROFILE)
        val home = manager.findFragmentByTag(HOME)
        val search = manager.findFragmentByTag(SEARCH)

        if (profile != null){
            fragTransaction.hide(profile)
        }

        if (home != null){
            fragTransaction.hide(home)
        }

        if (search != null) {
            fragTransaction.hide(search)
        }

        if (tag == SEARCH) {
            if (search!=null){
                fragTransaction.show(search)
            }
        }
        else if (tag == HOME) {
            if (home != null) {
                fragTransaction.show(home)
            }
        }

        else if (tag == PROFILE){
            if (profile != null){
                fragTransaction.show(profile)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }

}
