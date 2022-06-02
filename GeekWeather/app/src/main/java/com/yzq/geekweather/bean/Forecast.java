package com.yzq.geekweather.bean;

public class Forecast {
	public Cond cond;
	public String date;
	public Tmp tmp;
	
	public Cond getCond() {
		return cond;
	}
	public void setCond(Cond cond) {
		this.cond = cond;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Tmp getTmp() {
		return tmp;
	}
	public void setTmp(Tmp tmp) {
		this.tmp = tmp;
	}
	public static class Cond {
		public String txt_d;

		public String getTxt_d() {
			return txt_d;
		}

		public void setTxt_d(String txt_d) {
			this.txt_d = txt_d;
		}
		
	}
	public static class Tmp {
		public String max;
		public String min;
		public String getMax() {
			return max;
		}
		public void setMax(String max) {
			this.max = max;
		}
		public String getMin() {
			return min;
		}
		public void setMin(String min) {
			this.min = min;
		}
		
	}
	
}
