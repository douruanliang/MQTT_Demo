package io.dourl.mqtt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.dourl.base.mvvm.DataBindingFragment
import io.dourl.mqtt.R
import io.dourl.mqtt.bean.CityBean
import io.dourl.mqtt.databinding.FragmentAddressBinding
import io.dourl.mqtt.decoration.DividerItemDecoration
import io.dourl.mqtt.decoration.LetterCategoryDecoration
import io.dourl.mqtt.helper.IIndexBarDataHelper
import io.dourl.mqtt.helper.IndexBarDataHelperImpl
import kotlinx.android.synthetic.main.item_address.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddressFragment : DataBindingFragment<FragmentAddressBinding>() {

    private var param1: String? = null
    private var param2: String? = null
    private val INDEX_STRING_TOP = "↑"
    private var mDatas = mutableListOf<CityBean>()

    //汉语->拼音，拼音->tag
    private val mDataHelper: IIndexBarDataHelper = IndexBarDataHelperImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {
        val TO_ADDRESS: Int = 2 // 通讯录

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddressFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        @JvmStatic
        fun newInstance() =
            AddressFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_address

    override fun injectViewModel() {

    }

    override fun init(savedInstanceState: Bundle?) {
        initDatas(resources.getStringArray(R.array.provinces));
        //先排序
        mDataHelper.sortSourceDatas(mDatas)
        //汉语->拼音
        mDataHelper.convertPY(mDatas)
        //拼音->tag
        mDataHelper.setFirstLetter(mDatas)
        context?.let {
            mBinding.addressRv.layoutManager = LinearLayoutManager(it)
            mBinding.addressRv.adapter = AddressAdapter()

            val decoration = LetterCategoryDecoration(it)
            decoration.mData = mDatas as ArrayList

            mBinding.addressRv.addItemDecoration(decoration)
            mBinding.addressRv.addItemDecoration(DividerItemDecoration(it))
        }

    }


    private fun initDatas(data: Array<String>) {

        //微信的头部 也是可以右侧IndexBar导航索引的，
        // 但是它不需要被ItemDecoration设一个标题titile
        mDatas.add(CityBean("新的朋友", true).setBaseLetter(INDEX_STRING_TOP) as CityBean)
        mDatas.add(CityBean("群聊", true).setBaseLetter(INDEX_STRING_TOP) as CityBean)
        mDatas.add(CityBean("标签", true).setBaseLetter(INDEX_STRING_TOP) as CityBean)

        for (i in data.indices) {
            val cityBean = CityBean(data[i])
            mDatas.add(cityBean)
        }
    }

    internal inner class AddressAdapter : RecyclerView.Adapter<AddressAdapter.ViewHodler>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodler {
            val itemView: View = LayoutInflater.from(this@AddressFragment.context)
                .inflate(R.layout.item_address, parent, false)
            return ViewHodler(itemView)
        }

        override fun onBindViewHolder(holder: ViewHodler, position: Int) {
            holder.address.text = mDatas[position].name
            holder.content.setOnClickListener({
                Toast.makeText(context, "pos:$position", Toast.LENGTH_SHORT).show()
            })
        }

        override fun getItemCount(): Int {
            return mDatas.size
        }

        internal inner class ViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val address: TextView = itemView.tvAddress
            val content: LinearLayout = itemView.content
        }
    }


}