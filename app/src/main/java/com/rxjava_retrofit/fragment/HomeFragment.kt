package com.rxjava_retrofit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.framework.http.http.RxHttp
import com.framework.http.interfac.SimpleResponseListener
import com.rxjava_retrofit.bean.HomeBean
import com.rxjava_retrofit.HttpApi
import com.rxjava_retrofit.R
import com.rxjava_retrofit.adapter.HomePagerAdapter
import java.util.TreeMap

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class HomeFragment :Fragment(){
    private val TAG="HomeFragment";
    private var mRootView: View? = null

    private lateinit var mRecyclerView: RecyclerView

    private var mHomeAdapter: HomePagerAdapter? = null

    private var dataList: MutableList<HomeBean.Issue.Item> = mutableListOf()

    private var parameter = TreeMap<String,Any>();

    private lateinit var mTitle:TextView


    private val linearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTitle=mRootView?.findViewById(R.id.tv_title)!!
        mRecyclerView=mRootView?.findViewById(R.id.recycler_view)!!
        mHomeAdapter= mRootView?.let { HomePagerAdapter(it.context, dataList ) }
        mRecyclerView.adapter = mHomeAdapter
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()

        parameter["num"] = "1"
        initRequestHttp()

    }


    private fun initRequestHttp(){
        RxHttp.getRxHttpBuilder()
            .setApiUrl(HttpApi.test)
            .setLifecycle(this)
            .setParameter(parameter)
            .get()
            .build()
            .execute(object : SimpleResponseListener<HomeBean>() {
                override fun onSucceed(data: HomeBean, method: String) {
                    super.onSucceed(data, method)
                    mTitle.text=data.toString()
                    Log.e(TAG,"输出的数据(onSuccess)${data}")

                    data?.let {
                        dataList.addAll( it.issueList[0].itemList)
                        mHomeAdapter?.notifyItemRangeChanged(0,it.issueList[0].itemList.size)
                    }

//                    if(data.getData() is HomeBean ){
//                        val bean=data.getData()
//                        bean?.let {
//                             Log.e(TAG,"输出的数据(isSuccess)${data.isSuccess()}")
//                            dataList.addAll( it.issueList[0].itemList)
//                            mHomeAdapter?.notifyItemRangeChanged(0,it.issueList[0].itemList.size)
//                        }
//                    }
                }

                override fun onCompleted() {
                    super.onCompleted()
                    Log.e(TAG,"输出的数据(onCompleted)")
                }

                override fun onError(exception: Throwable) {
                    super.onError(exception)
                    Log.e(TAG,"(onError)${exception}")
                }
            })
    }
}