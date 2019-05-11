package controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class GuardadorDeEstadosTemplate {

	public void guardarEstado(String key, int valor, Activity ac) {		
		SharedPreferences sharedPref = ac.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(key, valor);
		editor.commit();
	}
	
	public void guardarEstado(String key, String valor, Activity ac) {		
		SharedPreferences sharedPref = ac.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, valor);
		editor.commit();
	}
	
	public int restaurarEstado(String key, Activity ac) {
		SharedPreferences sharedPref = ac.getPreferences(Context.MODE_PRIVATE);
		int defaultValue = -1;
		int value = sharedPref.getInt(key, defaultValue);
		return value;
	}
	
	public String restaurarEstado2(String key, Activity ac){
		SharedPreferences sharedPref = ac.getPreferences(Context.MODE_PRIVATE);
		String defaultValue = "";
		String value = sharedPref.getString(key, defaultValue);
		return value;		
	}
	
}
