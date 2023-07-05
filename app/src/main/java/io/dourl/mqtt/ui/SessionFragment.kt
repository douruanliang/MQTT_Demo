package io.dourl.mqtt.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.dourl.mqtt.R
import io.dourl.mqtt.adapter.SessionAdapter
import io.dourl.mqtt.bean.SessionModel
import io.dourl.mqtt.databinding.FragmentSessionBinding
import io.dourl.mqtt.model.SessionViewModel
import io.dourl.mqtt.utils.log.LoggerUtil

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SessionFragment : Fragment(R.layout.fragment_session) {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mBinding: FragmentSessionBinding

    private var mViewModel: SessionViewModel? = null
    private val mSessionAdapter = SessionAdapter(ArrayList())


    private val observerSession = Observer<List<SessionModel>> {
        LoggerUtil.d("session", it.toString())
        mSessionAdapter.setData(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentSessionBinding.bind(view)
        startObservers()
        startRecyclerView()
    }


    override fun onResume() {
        super.onResume()
    }

    private fun startObservers() {
        if (mViewModel == null) {
            mViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        }
        mViewModel?.sessionList?.observe(viewLifecycleOwner, observerSession)
        mViewModel?.loadData()
    }


    private fun startRecyclerView() {
        mSessionAdapter.register(SessionItemBinder())
        mBinding.recyclerViewSession.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mSessionAdapter
        }

    }

    companion object {
        val TO_CIRCLE: Int = 1 // 会话

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SessionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        @JvmStatic
        fun newInstance() =
            SessionFragment()
    }


}