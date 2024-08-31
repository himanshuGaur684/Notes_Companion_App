package gaur.himanshu.notescompanionapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {

    val notes = MutableStateFlow<List<Note>>(emptyList<Note>())

}