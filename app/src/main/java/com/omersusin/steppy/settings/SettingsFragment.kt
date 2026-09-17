package com.omersusin.steppy.settings

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.omersusin.steppy.R
import com.omersusin.steppy.SteppyApplication
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SettingsViewModel.factory(requireActivity().application as SteppyApplication)
        )[SettingsViewModel::class.java]
    }

    private lateinit var goalInput: EditText
    private lateinit var stepLengthInput: EditText
    private lateinit var heightInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var saveButton: Button
    private lateinit var statusText: TextView

    private var seeded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = inflater.context
        val padding = (24 * resources.displayMetrics.density).toInt()
        val spacing = (12 * resources.displayMetrics.density).toInt()

        goalInput = numberInput(context)
        stepLengthInput = numberInput(context)
        heightInput = numberInput(context)
        weightInput = numberInput(context)

        statusText = TextView(context).apply {
            gravity = Gravity.CENTER
        }

        saveButton = Button(context).apply {
            setText(R.string.settings_save)
            setOnClickListener { onSaveClicked() }
        }

        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(padding, padding, padding, padding)
            addView(labeled(R.string.settings_daily_goal, goalInput, spacing))
            addView(labeled(R.string.settings_step_length, stepLengthInput, spacing))
            addView(labeled(R.string.settings_height, heightInput, spacing))
            addView(labeled(R.string.settings_weight, weightInput, spacing))
            addView(saveButton, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, spacing, 0, 0) })
            addView(statusText)
        }
    }

    private fun labeled(labelRes: Int, input: EditText, spacing: Int): View {
        val context = requireContext()
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            addView(TextView(context).apply { setText(labelRes) })
            addView(input, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, spacing) })
        }
    }

    private fun numberInput(context: android.content.Context): EditText =
        EditText(context).apply {
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            gravity = Gravity.CENTER
        }

    private fun onSaveClicked() {
        val goal = goalInput.text.toString().toIntOrNull() ?: return
        val stepLength = stepLengthInput.text.toString().toIntOrNull() ?: return
        val height = heightInput.text.toString().toIntOrNull() ?: return
        val weight = weightInput.text.toString().toIntOrNull() ?: return
        viewModel.save(goal, stepLength, height, weight)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (!seeded) {
                        seeded = true
                        goalInput.setText(state.dailyGoal.takeIf { it > 0 }?.toString().orEmpty())
                        stepLengthInput.setText(state.stepLength.takeIf { it > 0 }?.toString().orEmpty())
                        heightInput.setText(state.height.takeIf { it > 0 }?.toString().orEmpty())
                        weightInput.setText(state.weight.takeIf { it > 0 }?.toString().orEmpty())
                    }
                    statusText.text = if (state.saved) getString(R.string.settings_saved) else ""
                }
            }
        }
    }
}
