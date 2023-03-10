package com.rxjava_retrofit.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rxjava_retrofit.fragment.HomeFragment
import com.rxjava_retrofit.fragment.MallsFragment
import com.rxjava_retrofit.fragment.UserFragment
import com.rxjava_retrofit.R


class MainActivity : AppCompatActivity() {


    companion object{
        private const val TAG = "MainActivity"

        const val TAG_HOME = "TAG_HOME" //首页


        const val TAG_MALLS = "TAG_MALLS" //商城


        const val TAG_ME = "TAG_ME" //个人中心
    }

    private var tag: String = TAG_HOME //标识点击了那个Fragment,默认的是定位到首页

    private var mHomeFragment: HomeFragment?=null

    private var mMallsFragment: MallsFragment?=null

    private var mUserFragment: UserFragment?=null

    private lateinit var mAHBottomNavigation: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAHBottomNavigation=findViewById(R.id.bottom_navigation)


        /**
         * 切换Fragment
         */
        commitFragment(tag)


        /**
         * 实例化底部导航栏
         */
        initBottomNavigation()
    }



    /**
     * 页面切换
     * @param tag
     */
    private fun commitFragment(tag: String) {
        this.tag = tag
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        when (tag) {
            TAG_HOME -> {
                if(mHomeFragment==null){
                    mHomeFragment= HomeFragment()
                    fragmentTransaction.add(R.id.container, mHomeFragment!!)
                }

                mHomeFragment!!.arguments = intent.extras
                fragmentTransaction.show(mHomeFragment!!)


                //商城隐藏
                if (mMallsFragment != null && mMallsFragment!!.isAdded) {
                    fragmentTransaction.hide(mMallsFragment!!)
                }

                //个人中心隐藏
                if (mUserFragment != null && mUserFragment!!.isAdded) {
                    fragmentTransaction.hide(mUserFragment!!)
                }
            }

            TAG_MALLS -> {
                if(mMallsFragment==null){
                    mMallsFragment= MallsFragment()
                    fragmentTransaction.add(R.id.container, mMallsFragment!!)
                }

                mMallsFragment!!.arguments = intent.extras
                fragmentTransaction.show(mMallsFragment!!)


                //首页隐藏
                if (mHomeFragment != null && mHomeFragment!!.isAdded) {
                    fragmentTransaction.hide(mHomeFragment!!)
                }

                //个人中心隐藏
                if (mUserFragment != null && mUserFragment!!.isAdded) {
                    fragmentTransaction.hide(mUserFragment!!)
                }
            }

            TAG_ME -> {
                if(mUserFragment==null){
                    mUserFragment= UserFragment()
                    fragmentTransaction.add(R.id.container, mUserFragment!!)
                }

                mUserFragment!!.arguments = intent.extras
                fragmentTransaction.show(mUserFragment!!)


                //首页隐藏
                if (mHomeFragment != null && mHomeFragment!!.isAdded) {
                    fragmentTransaction.hide(mHomeFragment!!)
                }

                //商城隐藏
                if (mMallsFragment != null && mMallsFragment!!.isAdded) {
                    fragmentTransaction.hide(mMallsFragment!!)
                }
            }
        }
        fragmentTransaction.commitAllowingStateLoss()
    }


    private fun initBottomNavigation(){
        mAHBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_search -> commitFragment(TAG_HOME)
                R.id.action_settings -> commitFragment(TAG_MALLS)
                R.id.action_navigation -> commitFragment(TAG_ME)
            }

            true
        }
    }
}