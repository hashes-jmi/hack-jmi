package com.expledia.hmm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import io.apptik.widget.multiselectspinner.BaseMultiSelectSpinner
import kotlinx.android.synthetic.main.remedies_fragment.view.*
import io.apptik.widget.multiselectspinner.MultiSelectSpinner
import java.util.ArrayList


/**
 * Created by shubhamg931 on 24/3/18.
 */
open class remediesFragment :android.support.v4.app.Fragment()
{
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var myView = View(context)
        var ans=ArrayList<String>()
        myView = LayoutInflater.from(context).inflate(R.layout.remedies_fragment, container, false)
        var myAdapter:ArrayAdapter<String>
        val options = ArrayList<String>()
        options.add("fever")
        options.add("headache")
        options.add("rash")
        options.add("nosebleed")
        options.add("backpain")
        options.add("diziness")
        options.add("stomachache")
        options.add("laziness")
        options.add("cough")
        options.add("sneeze")
        myAdapter=ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,ans)
        myView.remedyList.adapter=myAdapter

        var multiSelectSpinner1=myView.multiselectSpinner
        multiSelectSpinner1.setItems(options)

                .setListener<BaseMultiSelectSpinner>(object : BaseMultiSelectSpinner.MultiSpinnerListener {
                    override fun onItemsSelected(selected: BooleanArray) {
                        ans.clear()
                        for (i in selected.indices) {
                            if (selected[i] == true)
                            {
                                ans.add(options[i])
                                myAdapter.notifyDataSetChanged()
                            }

                            if (selected[i] == false)
                                try {
                                    ans.remove(options[i])
                                } catch (ex: Exception) {
                                }
                        }

                    }

                })
                .setAllCheckedText<BaseMultiSelectSpinner>("All types")
                .setAllUncheckedText<BaseMultiSelectSpinner>("none selected")
                .setSelectAll<BaseMultiSelectSpinner>(false)



        return myView!!
    }

}