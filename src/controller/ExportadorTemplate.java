package controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

public class ExportadorTemplate {
	
	Activity ac;
	final String TIPOMIME="text/csv";   //esse é o tipo MIME que será exportado
	final String NOMEDOPROGRAMA="FeedMemo";
	
	
	public ExportadorTemplate(Activity ac){ //no construtor desta classe estou adicionando a Activity que a chamou
		this.ac = ac;
	}		
	
	/**
	 * Método para invocar o "salvar como" do Android 
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public int salvarComo(){
		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT); //invocando a intenção de criar um documento
		intent.addCategory(Intent.CATEGORY_OPENABLE); 			//adicionando a categoria
		intent.setType(TIPOMIME);								//adicionando o tipo MIME
		ac.startActivityForResult(intent, 1);			//invocando a intenção
		return 1;
	}
	
	/**
	 * Método para exportar uma informação para o arquivo que foi criado pelo método salvarComo()
	 * @param requestCode - codigo da requisição
	 * @param resultCode - código do resultado obtido
	 * @param data - dados retornados pela intent
	 * @param info - as informações que serão exportadas
	 */
	@SuppressWarnings("static-access")
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public boolean exportar(int requestCode, int resultCode, Intent data, String info){
		boolean sucess = false;
		switch (requestCode) { //checando o código de requisição                               
		case 1:
			if (resultCode == ac.RESULT_OK) { //verificando o código do resultado
				Uri uri =null;
				if(data!=null){     //verificando se a intent é nula
					uri = data.getData();  //adicionando os dados na URI
					Log.i(NOMEDOPROGRAMA, "Uri: "+ uri.toString());	//imprimindo informação no log				
				}				
					try {
						OutputStream is = ac.getContentResolver().openOutputStream(uri); //capturando um outputstream do ContentResolver
						FileOutputStream fos = (FileOutputStream) is;     //realizando cast para FileOutputStream - 
																		//OutputStream é super classe da FileOutputStream 
						try {
							Writer fw2 = new OutputStreamWriter(fos, StandardCharsets.UTF_8); //Gerando um OutputStreamWriter, UTF-8
							fw2.write(info); //gravando a informação no arquivo
							fw2.close(); 	 //fechando o writer
							sucess = true;
						} catch (IOException e) {
							e.printStackTrace();
						}																
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}														
			}
			break;
		}
		return sucess;
	}
}
