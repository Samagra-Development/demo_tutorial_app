package com.samagra.ancillaryscreens.screens.change_password

import android.os.Bundle
import android.view.View
import com.samagra.ancillaryscreens.R
import com.samagra.ancillaryscreens.base.BaseActivity

class ChangePasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password_screen)

        if (findViewById<View>(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return
            }
            val firstFragment = ChangePasswordFragment()
            firstFragment.setArguments(intent.extras)
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit()
        }

    }

}