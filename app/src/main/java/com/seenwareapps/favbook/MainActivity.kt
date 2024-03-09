package com.seenwareapps.favbook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.seenwareapps.favbook.adapters.CustomAdapter
import com.seenwareapps.favbook.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var status: TextView
    private lateinit var addBook: FloatingActionButton

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        recyclerView = binding.recyclerView
        status = binding.booksAvailableStatus
        
        // db helper
        val db = BookDBHelper(this)

        // Get all notes from the database
        val dataFromDB = db.getAllBooks()

        // Create an ArrayList with the retrieved data
        val data = ArrayList(dataFromDB)

        if(data.isEmpty()){
            recyclerView.visibility = View.INVISIBLE
            status.visibility = View.VISIBLE
        }else{
            recyclerView.visibility = View.VISIBLE
            binding.noOfBooks.text = "Available Books: ${data.size}"
            status.visibility = View.INVISIBLE
        }

        // Create the adapter
        val adapter = CustomAdapter(data)

        recyclerView.startLayoutAnimation()

        // Set the layout manager for the RecyclerView (assuming LinearLayoutManager)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Set the adapter for the RecyclerView
        recyclerView.adapter = adapter

        addBook = binding.addBook

        addBook.setOnClickListener{
           Intent(this, AddBookActivity::class.java).apply {
               startActivity(this)
           }
        }

    }
}