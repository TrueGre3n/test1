package com.ubaya.studentapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.studentapp.R
import com.ubaya.studentapp.databinding.FragmentStudentListBinding
import com.ubaya.studentapp.viewmodel.ListViewModel


class StudentListFragment : Fragment() {
    private  lateinit var binding:FragmentStudentListBinding
    private lateinit var viewModel: ListViewModel
    private val studentListAdapter = StudentListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentListBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()

        binding.recView.layoutManager = LinearLayoutManager(context)
        binding.recView.adapter = studentListAdapter

        observerViewModel()

        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
            binding.recView.visibility = View.GONE
            binding.txtError.visibility = View.GONE
            binding.progressLoad.visibility = View.VISIBLE
            binding.refreshLayout.isRefreshing = false
        }
    }

    fun observerViewModel(){
        //it berisi arraylistofstudent yang teraktual
        viewModel.studentsLD.observe(viewLifecycleOwner, Observer {
            studentListAdapter.updateStudentList(it)
        })

        viewModel.studentLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it == true){
                binding.txtError.visibility = View.VISIBLE

            }else{
                binding.txtError.visibility = View.GONE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == true){
                binding.progressLoad.visibility = View.VISIBLE
                binding.recView.visibility = View.GONE
            }else{
                binding.progressLoad.visibility = View.GONE
                binding.recView.visibility = View.VISIBLE
            }
        })
    }
}