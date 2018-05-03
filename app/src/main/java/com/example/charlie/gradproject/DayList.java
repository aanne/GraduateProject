package com.example.charlie.gradproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DayList extends ArrayList<DayList.Item2> {
	protected Map<String, Item2> map = new TreeMap<>();

	public DayList(){
		super();
	}

	@Override
	public boolean add(Item2 item) {
		if (map.get(item.week) == null) {
			map.put(item.week, item);
			return super.add(item);
		}
		return false;
	}

	public static class Item2{
		String week;
		List<Item> list=new ArrayList<>();

		public Item2(String w,Item a){
			week=w;
			list.add(a);
		}
		public Item2(String w,Item a,Item b){
			week=w;
			list.add(a);
			list.add(b);
		}

		public Item2(String w,Item a,Item b,Item c){
			week=w;
			list.add(a);
			list.add(b);
			list.add(c);
		}
	}
}
