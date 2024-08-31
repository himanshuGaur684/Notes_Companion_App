package gaur.himanshu.notescompanionapp

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

const val AUTHORITY = "gaur.himanshu.notesapp.provider"
const val NOTES_TABLE = "notes"
val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$NOTES_TABLE")

fun getALlNotes(context: Context): List<Note> {
    val list = mutableListOf<Note>()

    val cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)

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

fun ContentResolver.insertNote(title: String, desc: String) {
    val values = ContentValues().apply {
        put("title", title)
        put("desc", desc)
    }
    insert(CONTENT_URI, values)
}

fun ContentResolver.updateNote(id: Int, title: String, desc: String) {
    val values = ContentValues().apply {
        put("title", title)
        put("desc", desc)
    }
    val updateUri = Uri.parse("content://$AUTHORITY/$NOTES_TABLE/$id")
    update(updateUri, values, null, null)

}

fun ContentResolver.deleteNote(id: Int) {
    val deleteUri = Uri.parse("content://$AUTHORITY/$NOTES_TABLE/$id")
    delete(deleteUri, null, null)
}

fun ContentResolver.observe(context: Context, uri: Uri) = callbackFlow<List<Note>> {

    val observer = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            val notes = getALlNotes(context)
            trySend(notes)
        }
    }
    registerContentObserver(uri, true, observer)

    awaitClose {
        unregisterContentObserver(observer)
    }
}.flowOn(Dispatchers.IO)