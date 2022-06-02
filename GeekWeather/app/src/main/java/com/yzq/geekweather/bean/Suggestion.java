package com.yzq.geekweather.bean;


public class Suggestion {
	
	public Flu flu;
	public Drsg drsg;
	public Sport sport;
	
	public Flu getFlu() {
		return flu;
	}

	public void setFlu(Flu flu) {
		this.flu = flu;
	}

	public Drsg getDrsg() {
		return drsg;
	}

	public void setDrsg(Drsg drsg) {
		this.drsg = drsg;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public static class Flu{
		public String txt;

		public String getTxt() {
			return txt;
		}

		public void setTxt(String txt) {
			this.txt = txt;
		}
	}

	public static class Drsg{
		public String txt;

		public String getTxt() {
			return txt;
		}

		public void setTxt(String txt) {
			this.txt = txt;
		}
	}
	
	public static class Sport{
		public String txt;

		public String getTxt() {
			return txt;
		}

		public void setTxt(String txt) {
			this.txt = txt;
		}
	}
	
	
}
