package com.mitsuki.ehit.core.ui.fragment.setting

import android.net.Uri
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.mitsuki.armory.span.SpannableBuilder
import com.mitsuki.ehit.R
import com.mitsuki.ehit.being.ShareData
import com.mitsuki.ehit.being.extend.debug
import com.mitsuki.ehit.being.network.Url

@Suppress("unused")
class SettingEhFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_eh, rootKey)

        findPreference<Preference>("setting_logout")?.setOnPreferenceClickListener { showLogoutDialog() }

        findPreference<Preference>("setting_cookie")?.apply {
            isVisible = ShareData.spCookies.isNotEmpty()
            setOnPreferenceClickListener { showCookieDialog() }
        }

        findPreference<ListPreference>(ShareData.SP_DOMAIN)?.apply {
            entries = Url.domain.map { it.first }.toTypedArray()
            entryValues = Url.domain.map { it.second }.toTypedArray()
        }

        findPreference<Preference>("setting_site")?.setOnPreferenceClickListener { openSiteSetting() }
    }

    private val allCookieInfo
        get() = SpannableBuilder()
            .append(ShareData.spCookies)
            .build()

    private fun showLogoutDialog(): Boolean {
        MaterialDialog(requireContext()).show {
            title(res = R.string.title_sign_out)
            message(res = R.string.text_sign_out_desc)
            positiveButton(R.string.text_confirm) { /*退出操作*/ }
            lifecycleOwner(this@SettingEhFragment)
        }
        return true
    }

    private fun showCookieDialog(): Boolean {
        MaterialDialog(requireContext()).show {
            title(res = R.string.title_cookie)
            message(text = allCookieInfo)
            positiveButton(R.string.text_copy) { /*复制json操作*/ }
            lifecycleOwner(this@SettingEhFragment)
        }
        return true
    }

    private fun openSiteSetting(): Boolean {
        return true
    }

}