package com.omersusin.steppy.summary

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.omersusin.steppy.R
import com.omersusin.steppy.SteppyApplication
import kotlinx.coroutines.launch
import java.util.Locale

class SummaryFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SummaryViewModel.factory(requireActivity().application as SteppyApplication)
        )[SummaryViewModel::class.java]
    }

    private lateinit var stepsText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var goalText: TextView
    private lateinit var statsText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = inflater.context
        val padding = (24 * resources.displayMetrics.density).toInt()
        val spacing = (8 * resources.displayMetrics.density).toInt()

        stepsText = TextView(context).apply {
            textSize = 56f
            gravity = Gravity.CENTER
        }
        progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, spacing, 0, spacing) }
        }
        goalText = TextView(context).apply {
            gravity = Gravity.CENTER
        }
        statsText = TextView(context).apply {
            textSize = 16f
            gravity = Gravity.CENTER
        }

        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(padding, padding, padding, padding)
            addView(stepsText)
            addView(progressBar)
            addView(goalText)
            addView(statsText)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    stepsText.text = String.format(Locale.getDefault(), "%,d", state.steps)
                    progressBar.progress = state.progressPercent
                    goalText.text = if (state.hasGoal) {
                        getString(R.string.summary_goal, state.goal, state.progressPercent)
                    } else {
                        getString(R.string.summary_no_goal)
                    }
                    statsText.text = getString(R.string.summary_stats, state.distanceKm, state.calories)
                }
            }
        }
    }
}
