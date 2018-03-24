package com.expledia.HealthPlus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.expledia.HealthPlus.Services.VolleyCallBack
import com.expledia.HealthPlus.Services.VolleyService
import io.apptik.widget.multiselectspinner.BaseMultiSelectSpinner
import kotlinx.android.synthetic.main.remedies_fragment.view.*
import java.util.ArrayList


/**
 * Created by shubhamg931 on 24/3/18.
 */
open class remediesFragment :android.support.v4.app.Fragment(),View.OnClickListener
{
    lateinit var myBoolean: ArrayList<Boolean>
    lateinit var ans:ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var myView = View(context)
        ans=ArrayList<String>()
        myBoolean= ArrayList<Boolean>()
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
                        myBoolean.clear()
                        myBoolean.add(true)
                        for (i in selected.indices) {
                            if (selected[i] == true)
                            {
                                myBoolean.add(selected[i])
                                ans.add(options[i])
                                myAdapter.notifyDataSetChanged()
                            }

                            if (selected[i] == false)
                                try {
                                    myBoolean.add(selected[i])
                                    ans.remove(options[i])
                                    myAdapter.notifyDataSetChanged()
                                } catch (ex: Exception) {
                                }
                        }

                    }

                })
                .setAllCheckedText<BaseMultiSelectSpinner>("All types")
                .setAllUncheckedText<BaseMultiSelectSpinner>("none selected")
                .setSelectAll<BaseMultiSelectSpinner>(false)



        myView.RequestButton.setOnClickListener(this)


        return myView!!
    }

    override fun onClick(v: View?)
    {
//        VolleyService.getDisease(context,)
        var listOFInt=ArrayList<Int>()
        for (i in myBoolean)
        {
            if (i)
                listOFInt.add(1)
            else
                listOFInt.add(0)
        }
        Log.i("mytag",listOFInt.toString())
        var getResultCallback=object:VolleyCallBack
        {
            override fun onSuccess(result: String)
            {
                Log.i("mytag","fragment me "+result.toString())
            }
        }
        VolleyService.getDisease(context,listOFInt,getResultCallback)
        {requestResponse->
            Log.i("mytag",requestResponse.toString())

        }
    }

}