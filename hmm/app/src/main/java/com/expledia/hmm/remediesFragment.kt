package com.expledia.hmm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import kotlinx.android.synthetic.main.remedies_fragment.view.*

/**
 * Created by shubhamg931 on 24/3/18.
 */
open class remediesFragment :android.support.v4.app.Fragment()
{
    lateinit var myView:View
    lateinit var searchableSpinner: SearchableSpinner
    var myArrayList= arrayOf("1,2","3","4","hello","hibro")


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        myView= inflater?.inflate(R.layout.remedies_fragment,container,false)!!
        var adapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_spinner_item, myArrayList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myView.spinner.adapter = adapter
        return myView!!

    }



}