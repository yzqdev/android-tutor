package com.yzq.geekweather.bean;


public class Weather {
	public Cond cond;
	public String tmp;
	public Wind wind;
	
	public Cond getCond() {
		return cond;
	}

	public void setCond(Cond cond) {
		this.cond = cond;
	}

	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	public Wind getWind() {
		return wind;
	}

	public void setWind(Wind wind) {
		this.wind = wind;
	}

	public static class Cond{
		String txt;

		public String getTxt() {
			return txt;
		}

		public void setTxt(String txt) {
			this.txt = txt;
		}
		
	}
	
	public static class Wind{
		String dir;
		String sc;
		public String getDir() {
			return dir;
		}
		public void setDir(String dir) {
			this.dir = dir;
		}
		public String getSc() {
			return sc;
		}
		public void setSc(String sc) {
			this.sc = sc;
		}
	}
}
