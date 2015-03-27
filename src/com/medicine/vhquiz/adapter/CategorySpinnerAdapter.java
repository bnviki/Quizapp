package com.medicine.vhquiz.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.medicine.vhquiz.R;
import com.medicine.vhquiz.data.entity.CategoryItem;

public class CategorySpinnerAdapter extends ArrayAdapter<CategoryItem> {	
	private List<CategoryItem> values;
	//private int[] images = {R.drawable.anatomy, R.drawable.pharmacology, R.drawable.micro, R.drawable.pathology};
	private Context context;

	public CategorySpinnerAdapter(Context context, List<CategoryItem> objects) {
		super(context, R.layout.category_item_spinner, objects);
		this.values = objects;
		this.context = context;
	}

	@Override
	public View getDropDownView(int position, View convertView,	ViewGroup parent) {
		// TODO Auto-generated method stub
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.category_item_spinner, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.title);
		//ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		textView.setText(values.get(position).name);
		//imageView.setImageResource(images[position]);
		return rowView;
	}
}
