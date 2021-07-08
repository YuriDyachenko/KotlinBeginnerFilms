package dyachenko.kotlinbeginnerfilms.view.settings

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.databinding.SettingsFragmentBinding
import dyachenko.kotlinbeginnerfilms.hideItems
import dyachenko.kotlinbeginnerfilms.model.FilmsListType

class SettingsFragment : Fragment() {
    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        (radioGroup.getChildAt(Settings.FILMS_LIST_TYPE.ordinal) as RadioButton)
            .isChecked = true
        applyButton.setOnClickListener {
            Settings.FILMS_LIST_TYPE = when (radioGroup.checkedRadioButtonId) {
                R.id.top_rated_radio_button -> FilmsListType.TOP_RATED
                R.id.now_playing_radio_button -> FilmsListType.NOW_PLAYING
                R.id.upcoming_radio_button -> FilmsListType.UPCOMING
                else -> FilmsListType.POPULAR
            }
            writeSettings()
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun writeSettings() {
        activity?.let {
            with(it.getSharedPreferences(Settings.PREFERENCE_NAME, Context.MODE_PRIVATE).edit()) {
                putInt(Settings.FILMS_LIST_TYPE_NAME, Settings.FILMS_LIST_TYPE.ordinal)
                apply()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.hideItems(
            R.id.action_settings,
            R.id.action_history
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}