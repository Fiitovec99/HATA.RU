package com.example.hataru.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.hataru.R

class FAQAdapter(
    private val context: Context,
    private val listDataHeader: List<String>,
    private val listHashMap: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return listHashMap[listDataHeader[groupPosition]]!![childPosition]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertViewVar = convertView
        val childText = getChild(groupPosition, childPosition) as String

        if (convertViewVar == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewVar = inflater.inflate(R.layout.list_item, null)
        }

        val txtListChild = convertViewVar!!.findViewById<TextView>(R.id.lblListItem)
        txtListChild.text = childText
        return convertViewVar
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listHashMap[listDataHeader[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return listDataHeader[groupPosition]
    }

    override fun getGroupCount(): Int {
        return listDataHeader.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup?): View {
        var convertViewVar = convertView
        val headerTitle = getGroup(groupPosition) as String

        if (convertViewVar == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewVar = inflater.inflate(R.layout.list_group, null)
        }

        val lblListHeader = convertViewVar!!.findViewById<TextView>(R.id.lblListHeader)
        lblListHeader.text = headerTitle
        return convertViewVar
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}


