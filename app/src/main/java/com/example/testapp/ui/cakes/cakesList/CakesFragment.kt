package com.example.testapp.ui.cakes.cakesList

import android.widget.Toast
import com.example.testapp.R
import com.example.testapp.base.BaseFragment
import com.example.testapp.base.SharedActivityParent
import com.example.testapp.base.SharedFragmentChild
import com.example.testapp.data.network.response.CakeResponse
import com.example.testapp.databinding.FragmentCakesBinding
import com.example.testapp.tools.setPaddingBottom
import com.example.testapp.tools.setPaddingTop

class CakesFragment(override val layoutId: Int = R.layout.fragment_cakes) :
    BaseFragment<CakesViewModel, FragmentCakesBinding>(), SharedFragmentChild {

    private val cakesAdapter: CakesAdapter = CakesAdapter(::onCakeClick)

    override val sharedParentActivity: SharedActivityParent?
        get() = activity as? SharedActivityParent

    override fun observeViewModel() {
        viewModel.run {
            cakesLiveData.observe {
                handleCakes(it.data)
                if (it.isFromRemote) {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
            errorLiveData.observe { error ->
                binding.swipeRefreshLayout.isRefreshing = false
                showDefaultErrorDialog(error)
            }
        }
    }

    override fun initViews() {
        requestCakes()
        binding.run {
            cakesRv.adapter = cakesAdapter
            swipeRefreshLayout.setOnRefreshListener {
                requestCakes()
            }
        }
    }

    override fun applyInsetsPadding(
        systemStatusBarSize: Int,
        systemNavigationBarSize: Int,
        isKeyboardOpen: Boolean
    ) {
        binding.toolbar.setPadding(0, systemStatusBarSize, 0, 0)
        binding.cakesRv.setPaddingBottom(systemNavigationBarSize)
    }

    private fun handleCakes(list: CakeResponse) {
        cakesAdapter.setCakes(list)
    }

    private fun requestCakes() {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.requestCakes()
    }

    private fun onCakeClick(cake: CakeResponse.CakeResponseItem){
        Toast.makeText(requireContext(), cake.desc, Toast.LENGTH_LONG).show()
    }
}