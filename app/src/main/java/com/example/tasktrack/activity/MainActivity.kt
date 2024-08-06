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
import com.example.domain.models.TaskModel
import com.example.tasktrack.R
import com.example.tasktrack.adapters.ViewPagerAdapter
import com.example.tasktrack.databinding.ActivityMainBinding
import com.example.tasktrack.fragment.FilterableFragment
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

        // Sorting button
        binding.btnSort.setOnClickListener{ view ->
            showSortMenu(view, vpAdapter.getCurrentFragment(binding.viewPager.currentItem))
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

    private fun showSortMenu(view: View, fragment: FilterableFragment?) {
        val popup = PopupMenu(this, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_sort, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            handleMenuItemClick(menuItem, fragment)
            true
        }
        popup.show()
    }

    private fun handleMenuItemClick(menuItem: MenuItem, fragment: FilterableFragment?) {
        when (menuItem.itemId) {
            R.id.sort_by_name_asc -> fragment?.sortTasks("title_asc")
            R.id.sort_by_name_desc -> fragment?.sortTasks("title_desc")
            R.id.sort_by_date_asc -> fragment?.sortTasks("date_asc")
            R.id.sort_by_date_desc -> fragment?.sortTasks("date_desc")
        }
    }
}