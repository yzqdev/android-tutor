package com.yzq.geekweather.bean;

public class City {
	public CityBean city;


	public CityBean getCity() {
		return city;
	}


	public void setCity(CityBean city) {
		this.city = city;
	}


	public static class CityBean{
		//在返回的第一个Json数据里面，只拿所需展示的aqi，所以其他的不使用GSON映射
		public String aqi;

		public String getAqi() {
			return aqi;
		}
		public void setAqi(String aqi) {
			this.aqi = aqi;
		}
	}




}
