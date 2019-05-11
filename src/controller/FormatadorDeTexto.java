package controller;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.EditText;


//classe para lidar com o EditText, ao digitar alguma coisa nele.
public class FormatadorDeTexto implements TextWatcher {

	String text = "";
	int qtdePixelHeightAntes = 0;
	int qtdePixelWidthAntes=0;

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	@Override
	public void afterTextChanged(Editable s) {
	}
	
	public EditText alterarTextSize(EditText text){
		if(text.getLineCount()>1 && text.getTextSize()>20){
			float size = text.getTextSize();
			text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (size - 5));		
		}		 
		return text;
	}

	public int getQtdePixelHeightAntes(){
		return qtdePixelHeightAntes;
	}
	
	public void setQtdePixelHeightAntes(int pixel){
		this.qtdePixelHeightAntes = pixel;
	}
	
	public String formatInputText(String text){
		char c = 13;
		char d = 10;
		if(text.contains(""+d))
			text = text.replace(""+d, "\\\\n");
		return text;
	}
	
	public String formatOutputText(String text){
		char a = '\n';
		if(text.contains("\\\\n"))
			text = text.replace("\\\\n", ""+a);		
		return text;
	}
}
