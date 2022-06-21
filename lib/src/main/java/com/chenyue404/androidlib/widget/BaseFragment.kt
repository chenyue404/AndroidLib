package com.chenyue404.androidlib.widget

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    protected var mRootView: View? = null
    protected val mContext: Context
        get() = requireContext()

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(getContentViewResId(), container, false)
        }
        return mRootView
    }

    abstract fun getContentViewResId(): Int
}