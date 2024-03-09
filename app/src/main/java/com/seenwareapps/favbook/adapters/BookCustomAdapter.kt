package com.seenwareapps.favbook.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.seenwareapps.favbook.*
import com.seenwareapps.favbook.R
import com.seenwareapps.favbook.models.Book

class CustomAdapter(private var mList: List<Book>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    lateinit var onResponse: (r : ResponseType) -> Unit

    enum class ResponseType {
        YES, NO
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookcard_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.bookId.text = "${position + 1}"
        holder.bookName.text = itemsViewModel.bookName
        holder.bookAuthor.text = itemsViewModel.bookAuthor
        holder.bookPages.text = itemsViewModel.bookPages

        val context = holder.bookId.context

        holder.deleteBtn.setOnClickListener {

            fun confirmDelete( listener: (r : ResponseType) -> Unit){
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Do you confirm deletion of book?")
                builder.setMessage(itemsViewModel.bookName)
                builder.setIcon(android.R.drawable.ic_delete)

                onResponse =  listener

                // performing positive action
                builder.setPositiveButton("Yes") { _, _ ->
                    onResponse(ResponseType.YES)
                }

                // performing negative action
                builder.setNegativeButton("No") { _, _ ->
                    onResponse(ResponseType.NO)
                }

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()

                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()

            }

            confirmDelete { responseType ->
                // Handle the response based on the enum ResponseType
                when (responseType) {
                    ResponseType.YES -> {
                        // User confirmed deletion, perform deletion logic here
                        // For example, you can call your existing logic for deletion
                        val bookIdToDelete = itemsViewModel.id
                        val db = BookDBHelper(context)
                        val isDeleted = db.deleteBook(bookIdToDelete)

                        if (isDeleted) {
                            val newData = db.getAllBooks()
                            updateData(newData)
                        } else {
                            Toast.makeText(context, "Book Not deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
                    ResponseType.NO -> {
                        // User declined deletion, handle accordingly
                        // You can add any specific logic here
                    }
                }
            }
        }

        // update note
        holder.bookUpdate.setOnClickListener{
            val intent = Intent(context, AddBookActivity::class.java)
            intent.putExtra("bookId", itemsViewModel.id)
            intent.putExtra("bookName", itemsViewModel.bookName)
            intent.putExtra("bookAuthor", itemsViewModel.bookAuthor)
            intent.putExtra("bookPage", itemsViewModel.bookPages)
            (context as Activity).startActivityForResult(intent, itemsViewModel.id)
        }

          //sharing book to social media
          holder.shareBtn.setOnClickListener{

              // Example text to share
              val shareText = "Hello, I am sharing this book amazing book with you" +
                      "\nBook Name: ${itemsViewModel.bookName}" +
                      "\nBook Author: ${itemsViewModel.bookAuthor}"

              // Create a sharing intent
              val sendIntent: Intent = Intent().apply {
                  action = Intent.ACTION_SEND
                  putExtra(Intent.EXTRA_TEXT, shareText)
                  type = "text/plain"
              }

              // Create a chooser intent to show the user a list of available sharing options
              val shareIntent = Intent.createChooser(sendIntent, "My Favourite Book")
              context.startActivity(Intent.createChooser(shareIntent, "Share Book"))
          }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val bookId: TextView = view.findViewById(R.id.bookId)
        val bookName: TextView = view.findViewById(R.id.bookName)
        val bookAuthor: TextView = view.findViewById(R.id.bookAuthor)
        val bookPages: TextView = view.findViewById(R.id.bookPages)
        val bookUpdate:ConstraintLayout = view.findViewById(R.id.cardViewLayout)
        val shareBtn:ImageButton = view.findViewById(R.id.shareBtn)
        val deleteBtn:ImageButton = view.findViewById(R.id.deleteBtn)
    }

    private fun updateData(newData: List<Book>) {
        val mutableList = mutableListOf<Book>()
        mutableList.addAll(newData)
        mList = mutableList
        notifyDataSetChanged()
    }
}
