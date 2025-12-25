package com.lawyer_archives.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.lawyer_archives.R
import com.lawyer_archives.adapters.MainViewPagerAdapter
import com.lawyer_archives.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDrawer()
        setupViewPager()
        setupBackPressHandler()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setupViewPager() {
        viewPagerAdapter = MainViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "جلسات"
                1 -> "موکلین"
                2 -> "پرونده‌ها"
                3 -> "مدیریت دفتر وکالت"
                4 -> "محاسبات قضایی"
                5 -> "محاسبات اسنادی"
                6 -> "محاسبات عمومی"
                7 -> "مواعد قانونی"
                8 -> "آراء وحدت رویه"
                9 -> "نظریه های مشورتی"
                10 -> "رویه های قضایی"
                11 -> "بانک قوانین"
                12 -> "نیازهای حقوقی"
                13 -> "مکان یاب قضایی"
                14 -> "خدمات ثبتی"
                else -> "Tab $position"
            }
        }.attach()
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                } else {
                    finish()
                }
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_meetings -> {
                binding.viewPager.currentItem = 0
            }
            R.id.nav_clients -> {
                binding.viewPager.currentItem = 1
            }
            R.id.nav_cases -> {
                binding.viewPager.currentItem = 2
            }
            R.id.nav_sessions -> {
                binding.viewPager.currentItem = 0
            }
            R.id.nav_daily_tasks -> {
                // می‌تونی بعداً اضافه کنی
            }
            R.id.nav_documents -> {
                // می‌تونی بعداً اضافه کنی
            }
        }
        
        binding.drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }
}
     /* 
    private fun setupViewPagerAndTabs() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        val adapter = MainViewPagerAdapter(this)
        viewPager.adapter = adapter

        val tabTitles = arrayOf(
            "جلسات",
            "موکلین",
            "پرونده‌ها",
            "مدیریت دفتر وکالت",
            "محاسبات قضایی",
            "محاسبات اسنادی",
            "محاسبات عمومی",
            "مواعد قانونی",
            "آراء وحدت رویه",
            "نظریه های مشورتی",
            "رویه های قضایی",
            "بانک قوانین",
            "نیازهای حقوقی",
            "مکان یاب قضایی",
            "خدمات ثبتی"
        )
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        when (item.itemId) {
            R.id.nav_profile -> {
                Toast.makeText(this, "پروفایل من", Toast.LENGTH_SHORT).show()
            }
            // کامنت کردن آیتم‌های منوی که وجود ندارند
            /*
            R.id.nav_backup -> {
                Toast.makeText(this, "پشتیبان‌گیری و بازیابی", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_reminder -> {
                Toast.makeText(this, "یادآورها", Toast.LENGTH_SHORT).show()
            }
            */
            R.id.nav_settings -> {
                Toast.makeText(this, "تنظیمات", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_meetings -> {
                viewPager.currentItem = 0
                tabLayout.getTabAt(0)?.select()
            }
            R.id.nav_clients -> {
                viewPager.currentItem = 1
                tabLayout.getTabAt(1)?.select()
            }
            R.id.nav_cases -> {
                viewPager.currentItem = 2
                tabLayout.getTabAt(2)?.select()
            }
            // کامنت کردن آیتم‌های منوی که وجود ندارند
            /*
            R.id.nav_office -> {
                viewPager.currentItem = 3
                tabLayout.getTabAt(3)?.select()
            }
            R.id.nav_judicial -> {
                viewPager.currentItem = 4
                tabLayout.getTabAt(4)?.select()
            }
            R.id.nav_document -> {
                viewPager.currentItem = 5
                tabLayout.getTabAt(5)?.select()
            }
            R.id.nav_public -> {
                viewPager.currentItem = 6
                tabLayout.getTabAt(6)?.select()
            }
            R.id.nav_legal -> {
                viewPager.currentItem = 7
                tabLayout.getTabAt(7)?.select()
            }
         /* R.id.nav_unanimity -> {
                viewPager.currentItem = 8
                tabLayout.getTabAt(8)?.select()
            }*/
            R.id.nav_consultative -> {
                viewPager.currentItem = 9
                tabLayout.getTabAt(9)?.select()
            }
            R.id.nav_procedures -> {
                viewPager.currentItem = 10
                tabLayout.getTabAt(10)?.select()
            }
            R.id.nav_procedures -> {
                viewPager.currentItem = 11
                tabLayout.getTabAt(11)?.select()
            }
            R.id.nav_needs -> {
                viewPager.currentItem = 12
                tabLayout.getTabAt(12)?.select()
            }
            R.id.nav_locator -> {
                viewPager.currentItem = 13
                tabLayout.getTabAt(13)?.select()
            }
            R.id.nav_registration -> {
                viewPager.currentItem = 14
                tabLayout.getTabAt(14)?.select()
  */