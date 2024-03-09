package com.seenwareapps.favbook

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.seenwareapps.favbook.databinding.ActivityAddBookBinding


class AddBookActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityAddBookBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (intent.hasExtra("bookId")) {

            binding.activityText.text = getString(R.string.update_book)
            binding.addBookBtn.isEnabled = false

            val bookName = intent.getStringExtra("bookName")
            val bookAuthor = intent.getStringExtra("bookAuthor")
            val bookPage = intent.getStringExtra("bookPage")

            binding.editTextBookName.setText(bookName.toString().trim())
            binding.editTextBookAuthor.setText(bookAuthor.toString().trim())
            binding.editTextBookPage.setText(bookPage.toString().trim())

            binding.updateBookBtn.setOnClickListener{

                val bookId = intent.getIntExtra("bookId", -1)
                val bookNameUpdate = binding.editTextBookName.text
                val bookAuthorUpdate = binding.editTextBookAuthor.text
                val bookPageUpdate = binding.editTextBookPage.text

                BookDBHelper(this).updateBook(bookId,"$bookNameUpdate", "$bookAuthorUpdate", "$bookPageUpdate")

                Toast.makeText(this, "Book updated", Toast.LENGTH_SHORT).show()

                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }else {
            binding.updateBookBtn.isEnabled = false
            binding.addBookBtn.setOnClickListener{

                if(binding.editTextBookName.length() != 0 && binding.editTextBookAuthor.length() != 0 && binding.editTextBookPage.length() != 0){
                    val bookName = binding.editTextBookName.text
                    val bookAuthor = binding.editTextBookAuthor.text
                    val bookPage = binding.editTextBookPage.text

                    BookDBHelper(this).addBook("$bookName", "$bookAuthor", "$bookPage")

                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }else{
                    Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                }
            }
        }



    }
}