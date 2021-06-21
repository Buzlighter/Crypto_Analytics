package com.example.crypto_analytics.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.api.RetrofitClient

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<GraphFragment>(R.id.graph_container)
            }
        }
    }
}