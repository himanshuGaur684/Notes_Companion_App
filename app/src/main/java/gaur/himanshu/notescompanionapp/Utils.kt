package gaur.himanshu.notescompanionapp

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri

const val AUTHORITY = "gaur.himanshu.notesapp.provider"
val NOTES_TABLE = "notes"
val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$NOTES_TABLE")

fun getAllNotes(context: Context): List<Note> {
    val list = mutableListOf<Note>()
    val cursor = context.contentResolver.query(
        CONTENT_URI, null, null, null, null
    )
    cursor?.let {
        while (it.moveToNext()) {
            val id = it.getInt(it.getColumnIndexOrThrow("id"))
            val title = it.getString(it.getColumnIndexOrThrow("title"))
            val desc = it.getString(it.getColumnIndexOrThrow("desc"))

            list.add(Note(id = id, title = title, desc = desc))
        }
        it.close()
    }
    return list
}

fun ContentResolver.insertNote(title:String,desc:String){
    val values = ContentValues().apply {
        put("title",title)
        put("desc",desc)
    }
    insert(CONTENT_URI,values)
}

fun ContentResolver.updateNote(id:Int,title: String,desc: String){
    val values = ContentValues().apply {
        put("title",title)
        put("desc",desc)
    }
    val updateUri = Uri.parse("content://$AUTHORITY/$NOTES_TABLE/$id")
    update(updateUri,values,null,null)
}

fun ContentResolver.deleteNote(id:Int){
    val deleteUri = Uri.parse("content://$AUTHORITY/$NOTES_TABLE/$id")
    delete(deleteUri,null,null)
}

