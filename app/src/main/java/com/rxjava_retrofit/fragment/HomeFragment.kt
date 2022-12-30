package com.rxjava_retrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.mvp.bean.HomeBean
import com.rxjava_retrofit.R
import com.rxjava_retrofit.adapter.HomePagerAdapter

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class HomeFragment :Fragment(){

    private var mRootView: View? = null

    private lateinit var mRecyclerView: RecyclerView

    private var mHomeAdapter: HomePagerAdapter? = null

    private var dataList: MutableList<HomeBean.Issue.Item> = mutableListOf()


    private val linearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRecyclerView=mRootView?.findViewById(R.id.recycler_view)!!
        mHomeAdapter= mRootView?.let { HomePagerAdapter(it.context, dataList ) }
        mRecyclerView.adapter = mHomeAdapter
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
    }
}