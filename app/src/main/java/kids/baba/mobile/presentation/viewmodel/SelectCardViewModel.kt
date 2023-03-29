package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.presentation.event.CardSelectEvent
import kids.baba.mobile.presentation.model.CardStyleIconUiModel
import kids.baba.mobile.presentation.model.CardStyleUiModel
import kids.baba.mobile.presentation.model.CardStyles
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resources: Resources
) : ViewModel() {

    private var currentTakenMedia = savedStateHandle.get<MediaData>(MEDIA_DATA)

    val cardState = MutableStateFlow<CardStyles?>(null)
    private val cardSelectEventChannel = Channel<CardSelectEvent>()

    init {
        getCards()
    }

    fun setPreviewImg(imageView: ImageView) {
        if (currentTakenMedia != null) {
            imageView.setImageURI(currentTakenMedia!!.mediaPath.toUri())
        }
    }

    fun setTitle(title: TextView) {
        if (currentTakenMedia != null) {
            title.text = currentTakenMedia!!.mediaName
        }
    }

    fun onCardSelected(card: CardStyleUiModel, position: Int) = viewModelScope.launch {
        cardState.value!!.selected = position
        cardSelectEventChannel.send(CardSelectEvent.CardSelect(card, position))
    }



    private fun getCards() {
        val cardStyle = CardStyles(
            cardStyles = listOf(
                CardStyleUiModel(CardStyleIconUiModel.CARD_BASIC_1, resources.getString(R.string.card_basic_1), "true"),
                CardStyleUiModel(CardStyleIconUiModel.CARD_SKY_1, resources.getString(R.string.card_sky_1), "false"),
                CardStyleUiModel(
                    CardStyleIconUiModel.CARD_CLOUD_1,
                    resources.getString(R.string.card_cloud_1),
                    "false"
                ),
                CardStyleUiModel(
                    CardStyleIconUiModel.CARD_CLOUD_2,
                    resources.getString(R.string.card_cloud_2),
                    "false"
                ),
                CardStyleUiModel(CardStyleIconUiModel.CARD_TOY_1, resources.getString(R.string.card_toy_1), "false"),
                CardStyleUiModel(
                    CardStyleIconUiModel.CARD_CANDY_1,
                    resources.getString(R.string.card_candy_1),
                    "false"
                ),
                CardStyleUiModel(
                    CardStyleIconUiModel.CARD_SNOWFLOWER_1,
                    resources.getString(R.string.card_snowflower_1),
                    "false"
                ),
                CardStyleUiModel(
                    CardStyleIconUiModel.CARD_SNOWFLOWER_2,
                    resources.getString(R.string.card_snowflower_2),
                    "false"
                ),
                CardStyleUiModel(CardStyleIconUiModel.CARD_LINE_1, resources.getString(R.string.card_line_1), "false"),
                CardStyleUiModel(
                    CardStyleIconUiModel.CARD_SPRING_1,
                    resources.getString(R.string.card_spring_1),
                    "false"
                ),
                CardStyleUiModel(
                    CardStyleIconUiModel.CARD_CHECK_1,
                    resources.getString(R.string.card_check_1),
                    "false"
                ),
                CardStyleUiModel(
                    CardStyleIconUiModel.CARD_CHECK_2,
                    resources.getString(R.string.card_check_2),
                    "false"
                ),
            ),
            selected = 0
        )
        cardState.value = cardStyle
    }



    companion object {
        const val MEDIA_DATA = "mediaData"
    }

}