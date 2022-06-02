package com.yzq.geekweather.bean;

import java.util.List;



public class Bean {
	public City aqi;
	public Weather now;
	public Suggestion suggestion;
	public List<Forecast> daily_forecast;
	
	public List<Forecast> getDaily_forecast() {
		return daily_forecast;
	}

	public void setDaily_forecast(List<Forecast> daily_forecast) {
		this.daily_forecast = daily_forecast;
	}

	public City getAqi() {
		return aqi;
	}

	public void setAqi(City aqi) {
		this.aqi = aqi;
	}

	public Weather getNow() {
		return now;
	}

	public void setNow(Weather now) {
		this.now = now;
	}

	public Suggestion getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(Suggestion suggestion) {
		this.suggestion = suggestion;
	}

	
}
