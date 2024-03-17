import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hataru.domain.GetPhotosUseCase
import com.example.hataru.domain.entity.Photos
import kotlinx.coroutines.launch

class FlatFragmentViewPagerViewModel(private val photos : GetPhotosUseCase) : ViewModel() {

    private var _photos = MutableLiveData<Photos>()

    val photo: LiveData<Photos> get() = _photos

    init{
        viewModelScope.launch{
            _photos.value = photos.getPhotos()

            //Log.d("asdasd",photos.getPhotos().rooms.toString())
        }
    }

}