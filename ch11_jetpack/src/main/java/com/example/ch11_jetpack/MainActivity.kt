package com.example.ch11_jetpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ch11_jetpack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    // 화면에 뷰페이저가 나오는데, 뷰페이저도 어뎁터가 필요하다.
    // 뷰페이저를 위한 어뎁터 준비
    class MyFragmentPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        // 뷰페이저는 항목 자체가 프레그먼트 화면으로 꾸미기 위한게 목적
        // 아래 변수는 fragment 객체를 여러 개 가지는, 결국 항목이 화면 하나이고 그 화면을 하나하나 구성하기 위해 fragment 객체를 여러 개 가지는... 그런 프로퍼티
        val fragments: List<Fragment>

        init {
            fragments = listOf(OneFragment(), TwoFragment(), ThreeFragment()) // 항목을 세 개 추가한 것이니, 세 개의 항목이 나올 것이다...
        }

        override fun getItemCount(): Int {
            // 항목 개수를 판단하기 위한 함수
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            // 항목을 구성하기 위한, 그런데 나는 fragment로 구성할 것이고, fragment가 준비되어 있는 순서대로 넘기면 된다
            return fragments[position]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 원래 액션바에 들어갔어야되는 내용이 toolbar에 적용하게끔 함수 명령 내리기
        // toolbar가 actionbar와 같이 동일한 역할을 하도록 처리
        setSupportActionBar(binding.toolbar)

        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()


        // 화면에 뷰페이저인데 어뎁터 적용 필요
        val adapter = MyFragmentPagerAdapter(this)
        binding.viewpager.adapter = adapter // 이렇게 되면 우리가 만든 어뎁터에 의해서 뷰페이저가 나오게 된다
    }

    // 원래 activity의 메뉴를 구성하기 위한 함수..
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("eggjam82", "Search Text: $query")
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}