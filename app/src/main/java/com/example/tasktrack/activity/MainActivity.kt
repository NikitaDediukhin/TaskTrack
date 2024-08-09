package com.example.tasktrack.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager2.widget.ViewPager2
import com.example.domain.models.TaskModel
import com.example.tasktrack.R
import com.example.tasktrack.adapters.ViewPagerAdapter
import com.example.tasktrack.databinding.ActivityMainBinding
import com.example.tasktrack.fragment.ViewPagerFragment
import com.example.tasktrack.fragment.TaskViewModel
import com.example.tasktrack.utils.DialogManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val vm: TaskViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val vpAdapter: ViewPagerAdapter = ViewPagerAdapter(this)
    private var isFirstLaunch = true

    companion object {
        const val SORT_TITLE_ASC = "title_asc"
        const val SORT_TITLE_DESC = "title_desc"
        const val SORT_DATE_ASC = "date_asc"
        const val SORT_DATE_DESC = "date_desc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        // Floating button AddTask
        val btnAddTask: View = binding.btnAddTask
        btnAddTask.setOnClickListener{
            showAddTaskDialog()
        }

        //SearchView
        //Search tasks by title and description
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchQuery(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchQuery(it) }
                return false
            }
        })

        // TabLayout & ViewPager2
        // set adapter for ViewPager2
        binding.viewPager.adapter = vpAdapter
        // binding TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "В процессе"
                1 -> "Готовые"
                else -> null
            }
        }.attach()
        // clear searchView after switch Fragment
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // clear search
                binding.searchView.setQuery("", false)
                binding.searchView.clearFocus()
                // clear sort
                vpAdapter.getCurrentFragment(binding.viewPager.currentItem)?.sortTasks("date_asc")

            }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (!isFirstLaunch) {
                    // clear sort
                    val previousPosition = if(position == 0) 1 else 0
                    val previousFragment = vpAdapter.getCurrentFragment(previousPosition)
                    previousFragment?.sortTasks("date_asc")
                    // clear search
                    previousFragment?.clearFilter()
                }
                isFirstLaunch = false
            }
        })

        // Sorting button
        binding.btnSort.setOnClickListener{ view ->
            showSortMenu(view, vpAdapter.getCurrentFragment(binding.viewPager.currentItem))
        }

        // Grid button & isGridView Live Data
        binding.btnGrid.setOnClickListener {
            vm.toggleView()
        }
        vm.isGridView.observe(this) {isGrid ->
            val iconRes = if (isGrid) R.drawable.ic_list_view else R.drawable.ic_grid_view
            binding.btnGrid.setBackgroundDrawable(AppCompatResources.getDrawable(this, iconRes))
        }
    }


    private fun showAddTaskDialog() {
        DialogManager.addTaskDialog(this, object : DialogManager.CreateTaskListener {
            // add Task to DataBase
            override fun onClick(title: String, description: String, dueDate: Date) {
                try {
                    vm.createTask(
                        TaskModel(
                            id = 0,
                            title = title,
                            description = description,
                            creationDate = Date(),
                            dueDate = dueDate,
                            competitionStatus = false
                        )
                    )
                } catch (e: Exception){
                    Log.e("task", e.message.toString())
                }
            }
        })
    }

    private fun searchQuery(query: String) {
        val currentFragment = vpAdapter.getCurrentFragment(binding.viewPager.currentItem)
        currentFragment?.searchTask(query)
    }

    //Restore deleted task with SnackBar
    fun restoreDeletedTask(task: TaskModel) {
        val snackBar = Snackbar.make(binding.root, "Удалено '${task.title}'", Snackbar.LENGTH_SHORT)
        snackBar.setAction("Отменить") {
            vm.createTask(task)
        }
        snackBar.show()
    }

    private fun showSortMenu(view: View, fragment: ViewPagerFragment?) {
        val popup = PopupMenu(this, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_sort, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            handleMenuItemClick(menuItem, fragment)
            true
        }
        popup.show()
    }

    private fun handleMenuItemClick(menuItem: MenuItem, fragment: ViewPagerFragment?) {
        when (menuItem.itemId) {
            R.id.sort_by_name_asc -> fragment?.sortTasks(SORT_TITLE_ASC)
            R.id.sort_by_name_desc -> fragment?.sortTasks(SORT_TITLE_DESC)
            R.id.sort_by_date_asc -> fragment?.sortTasks(SORT_DATE_ASC)
            R.id.sort_by_date_desc -> fragment?.sortTasks(SORT_DATE_DESC)
        }
    }
}