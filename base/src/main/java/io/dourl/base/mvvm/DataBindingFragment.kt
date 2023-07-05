package io.dourl.base.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * File description.
 *  基于 ViewDataBinding 的fragment
 * @author dourl
 * @date 2022/10/20
 */
abstract class DataBindingFragment<B : ViewDataBinding> : Fragment() {

    lateinit var mBinding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: ViewDataBinding =
            DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        mBinding = binding as B
        injectViewModel()
        init(savedInstanceState)
        return mBinding.root
    }

    abstract fun getLayoutId(): Int
    abstract fun injectViewModel()
    abstract fun init(savedInstanceState: Bundle?)

}