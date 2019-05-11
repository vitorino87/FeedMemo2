package view;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;

public class MenuDoMainView {	
	Activity ac;
	Menu menu;
	
	MenuDoMainView(Activity ac, Menu menu){
		this.ac = ac;
		this.menu = menu;
	}
		
	public boolean chamarMenuInicial(int recursoMenu){
		MenuInflater inflater = ac.getMenuInflater();
		inflater.inflate(recursoMenu, menu);
		return true;
	}
}
