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

public class ExportadorTemplate {

	Activity ac;
	final String TIPOMIME = "text/csv"; // esse � o tipo MIME que ser� exportado
	final String NOMEDOPROGRAMA = "FeedMemo";

	public ExportadorTemplate(Activity ac) { // no construtor desta classe estou adicionando a Activity que a chamou
		this.ac = ac;
	}

	/**
	 * M�todo para invocar o "salvar como" do Android
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public int salvarComo() {
		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT); // invocando a inten��o de criar um documento
		intent.addCategory(Intent.CATEGORY_OPENABLE); // adicionando a categoria
		intent.setType(TIPOMIME); // adicionando o tipo MIME
		ac.startActivityForResult(intent, 1); // invocando a inten��o
		return 1;
	}

	/**
	 * M�todo para exportar uma informa��o para o arquivo que foi criado pelo
	 * m�todo salvarComo()
	 * 
	 * @param requestCode
	 *            - codigo da requisi��o
	 * @param resultCode
	 *            - c�digo do resultado obtido
	 * @param data
	 *            - dados retornados pela intent
	 * @param info
	 *            - as informa��es que ser�o exportadas
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public Writer prepararExport(Intent data) {
		Writer fw2 = null;
		Uri uri = null;
		if (data != null) { // verificando se a intent � nula
			uri = data.getData(); // adicionando os dados na URI
			//Log.i(NOMEDOPROGRAMA, "Uri: " + uri.toString()); // imprimindo informa��o no log
			try {
				OutputStream is = ac.getContentResolver().openOutputStream(uri); // capturando um outputstream do ContentResolver
				FileOutputStream fos = (FileOutputStream) is; // realizando cast
				fw2 = new OutputStreamWriter(fos, StandardCharsets.UTF_8); // Gerando um OutputStreamWriter, UTF-8					
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		return fw2;
	}

	public void fecharArquivo(Writer fw2) throws IOException {
		fw2.close(); // fechando o writer
	}
	
	public void salvar(Writer fw2, String info) throws IOException{
		fw2.append(info); // gravando a informa��o no arquivo
	}
}
