package ch.protonmail.android.protonmailtest.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ch.protonmail.android.protonmailtest.IClickedCallback
import ch.protonmail.android.protonmailtest.fragments.HottestFragment
import ch.protonmail.android.protonmailtest.fragments.UpcomingFragment

/**
 * Created by ProtonMail on 2/25/19. */
class TabsAdapter(val callback: IClickedCallback, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return UpcomingFragment(callback)
        }
        return HottestFragment(callback)
    }

    override fun getCount(): Int {
        return 2
    }
}