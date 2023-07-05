package io.dourl.mqtt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.dourl.mqtt.adapter.AdapterImage
import io.dourl.mqtt.databinding.FragmentHomeBinding
import io.dourl.mqtt.utils.log.LoggerUtil
import io.dourl.mqtt.view_model.MainViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentHomeBinding

    private val viewModelFactory: MainViewModelFactory = MainViewModelFactory()
    private lateinit var viewModel: MainViewModel
    private val adapterImage = AdapterImage()

    private val observerImages = Observer<List<Pics>> {

        LoggerUtil.d("home", it.toString())
        adapterImage.submitList(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        startObservers()
        startRecyclerView()
        binding.title.setOnClickListener { viewModel.getAllPics(); }

    }
    private fun startRecyclerView() = binding.recyclerVIewSreen.apply {
        adapter = adapterImage
        layoutManager = LinearLayoutManager(requireContext())
        addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    hideSoftInput()

                }
            }
        )
    }

    fun View.hideSoftInput() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun startObservers() {
        viewModel = viewModelFactory.create(MainViewModel::class.java)
        viewModel.getAllPics()

        viewModel.pics.observe(viewLifecycleOwner, observerImages)

        viewModel.toastLiveData.observe(
            viewLifecycleOwner
        ) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        val index: Int = 0 // 首页

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}