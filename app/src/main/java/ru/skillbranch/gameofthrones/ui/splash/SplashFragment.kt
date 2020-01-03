package ru.skillbranch.gameofthrones.ui.splash


import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_splash.*
import ru.skillbranch.gameofthrones.R

class SplashFragment : Fragment() {

    private lateinit var va: ValueAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val color = getColor(requireContext(), R.color.color_accent)

        va = (AnimatorInflater.loadAnimator(requireContext(), R.animator.heart_bit)
                as ValueAnimator)
        va.addUpdateListener {
            val animateColor = ColorUtils.setAlphaComponent(color, it.animatedValue as Int)
            splash_iv.setColorFilter(animateColor, android.graphics.PorterDuff.Mode.DARKEN)
        }
        va.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        va.cancel()
    }

}
