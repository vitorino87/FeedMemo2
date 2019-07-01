package controller;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.EditText;


//classe para lidar com o EditText, ao digitar alguma coisa nele.
public class FormatadorDeTexto implements TextWatcher {

	EditText edt;
	String text = "";
	int qtdePixelHeightAntes = 0;
	int qtdePixelWidthAntes=0;
	
	public FormatadorDeTexto(){}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	@Override
	public void afterTextChanged(Editable s) {
		try{
			if(edt!=null){
				if(edt.getLineCount()>2)
					impedirMaisDeDuasLinhas(edt);
				else
					reduzirTamanhoDoTexto(edt);				
			}
		}catch(Exception ex){
			
		}
		
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
	
	public void impedirMaisDeDuasLinhas(EditText text){
		if(text.getLineCount()>2){
			String texto = text.getText().toString();
			int tam = texto.length();			
			if (tam > 0)
				text.setText(texto.subSequence(0, tam - 1));
			//text.setMaxLines(2);
		}
	}
	
	public void reduzirTamanhoDoTexto(EditText text){
		if(text.getLineCount()>1 && text.getTextSize()>20){
			float size = text.getTextSize();
			text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (size - 5));		
		}		 
	}

	public EditText getEdt() {
		return edt;
	}

	public void setEdt(EditText edt) {
		this.edt = edt;
	}		
}
