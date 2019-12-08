package ru.skillbranch.gameofthrones.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import ru.skillbranch.gameofthrones.repositories.RootRepository
import java.util.*

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    val tabs = runBlocking(Dispatchers.IO) {
        RootRepository.findAllHouses()
    }

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return CharactersListFragment.newInstance(tabs[position].name)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position].name.toUpperCase(Locale.getDefault())
    }

    override fun getCount(): Int {
        return tabs.size
    }
}