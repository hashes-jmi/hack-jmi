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
import kotlinx.android.synthetic.main.blank_fragment.view.*
import kotlinx.android.synthetic.main.remedies_fragment.view.*
import java.util.ArrayList


/**
 * Created by shubhamg931 on 24/3/18.
 */
open class blankFragment :android.support.v4.app.Fragment()
{

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var myView = View(context)
        myView = LayoutInflater.from(context).inflate(R.layout.blank_fragment, container, false)
        myView!!.diseaseName.setText(this.arguments["disease"].toString())
        return myView!!
    }
}