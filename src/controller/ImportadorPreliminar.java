package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

/**
 * Esta classe é preliminar, ela não realiza a importação completa
 * ela só carrega o arquivo selecionado pelo usuário na memória do programa
 * É na classe MainView2 que é definido onde será salvo os dados
 * @author tiago.lucas
 *
 */
public class ImportadorPreliminar {
	private static final String TIPOMIME = "*/*";
	private static final String NOMEDOPROGRAMA = "FeedMemo";
	Activity ac;
	
	public ImportadorPreliminar(Activity ac){
		this.ac = ac;
	}

	/**
	 * Abre o arquivo que será exportado
	 * @return número 2, significa que trata-se de uma importação
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public int abrirArquivo(){
		//Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		//Intent intent = new Intent(Intent.ACTION_PICK);
		
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		//intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType(TIPOMIME);
		//intent.setType(TIPOMIME);
		//intent.setType("plain/text");
		ac.startActivityForResult(intent, 1);
		return 2;
	}
	
	/**
	 * Programa para realizar a importação do arquivo para a memória
	 * @param requestCode - codigo da requisição
	 * @param resultCode - código do resultado obtido
	 * @param data - dados retornados pela intent
	 * @return
	 */
	@SuppressWarnings("static-access")
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public ArrayList<String> importar(int requestCode, int resultCode, Intent data){
		ArrayList<String> lista = new ArrayList<String>();
		switch(requestCode){
		case 1:
			if(resultCode == ac.RESULT_OK){
				Uri uri = null;
				uri = data.getData();
				Log.i(NOMEDOPROGRAMA, "Uri: "+uri.toString());
				try {					
					InputStream is  = ac.getContentResolver().openInputStream(uri);
					//FileInputStream fis = (FileInputStream)is;
					Reader rd = new InputStreamReader(is, StandardCharsets.UTF_8);//utilizando utf-8				
					int ch;
					String text = "";
					while((ch = rd.read())!=-1){ 
						if(ch != 10 && ch != 13){ //se char não é enter
							//if(ch!=34) //se char não é "
							text+=String.valueOf((char)ch);						
						}else{
							lista.add(text); //adiciona na lista
							text=""; //limpa variavel
						}						
					}										
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lista;
	}	
}
