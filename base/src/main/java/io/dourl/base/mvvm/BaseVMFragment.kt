package io.dourl.base.mvvm

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider

/**
 * File description.
 *   基于 MVVM模式的Fragment
 * @author dourl
 * @date 2022/10/20
 */
abstract class BaseVMFragment<B : ViewDataBinding, VM : BaseViewModel> : DataBindingFragment<B>() {
    protected lateinit var viewModel: VM

    override fun injectViewModel() {
        val vm = createViewModel()
        viewModel =
            ViewModelProvider(this, BaseViewModel.createViewModelFactory(vm)).get(vm::class.java)
        lifecycle.addObserver(viewModel);
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)


    }

    protected abstract fun createViewModel(): VM;
}