package ru.skillbranch.gameofthrones.ui.houses


import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_houses.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.ui.RootActivity
import kotlin.math.hypot
import kotlin.math.max

class HousesFragment : Fragment() {

    private lateinit var colors: Array<Int>
    private lateinit var housesPagerAdapter: HousesPagerAdapter

    @ColorInt
    private var currentColor: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        housesPagerAdapter = HousesPagerAdapter(childFragmentManager)
        colors = requireContext().run {
            arrayOf(
                getColor(R.color.stark_primary),
                getColor(R.color.lannister_primary),
                getColor(R.color.targaryen_primary),
                getColor(R.color.baratheon_primary),
                getColor(R.color.greyjoy_primary),
                getColor(R.color.martel_primary),
                getColor(R.color.tyrel_primary)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        with(menu.findItem(R.id.action_search)?.actionView as SearchView) {
            queryHint = "Search character"
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_houses, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as RootActivity).setSupportActionBar(toolbar)

        if (currentColor != -1) appbar.setBackgroundColor(currentColor)

        view_pager.adapter = housesPagerAdapter

        with(tabs) {
            setupWithViewPager(view_pager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) = Unit
                override fun onTabUnselected(p0: TabLayout.Tab?) = Unit
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val position = tab.position

                    if ((appbar.background as ColorDrawable).color != colors[position]) {
                        val rect = Rect()
                        @Suppress("INACCESSIBLE_TYPE")
                        val tabView = tab.view as View

                        //default animation 300 ms
                        tabView.postDelayed({
                            tabView.getGlobalVisibleRect(rect)
                            animateAppbarReveal(position, rect.centerX(), rect.centerY())
                        }, 300)
                    }
                }
            })
        }

    }

    private fun animateAppbarReveal(position: Int, centerX: Int, centerY: Int) {
        val endRadius = max(
            hypot(centerX.toDouble(), centerY.toDouble()),
            hypot(appbar.width.toDouble() - centerX.toDouble(), centerY.toDouble())
        )

        with(reveal_view) {
            isVisible = true
            setBackgroundColor(colors[position])
        }

        ViewAnimationUtils.createCircularReveal(
            reveal_view,
            centerX,
            centerY,
            0f,
            endRadius.toFloat()
        ).apply {
            doOnEnd {
                appbar.setBackgroundColor(colors[position])
                reveal_view.isInvisible = true
            }
        }.start()
    }

}
