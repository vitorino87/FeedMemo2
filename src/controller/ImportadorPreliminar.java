package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

/**
 * Esta classe � preliminar, ela n�o realiza a importa��o completa ela s�
 * carrega o arquivo selecionado pelo usu�rio na mem�ria do programa � na classe
 * MainView2 que � definido onde ser� salvo os dados
 * 
 * @author tiago.lucas
 *
 */
public class ImportadorPreliminar {
	private static final String TIPOMIME = "*/*";
	private static final String NOMEDOPROGRAMA = "FeedMemo";
	Activity ac;

	public ImportadorPreliminar(Activity ac) {
		this.ac = ac;
	}

	/**
	 * Abre o arquivo que ser� exportado
	 * 
	 * @return n�mero 2, significa que trata-se de uma importa��o
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public int abrirArquivo() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType(TIPOMIME);
		ac.startActivityForResult(intent, 1);
		return 2;
	}

	/**
	 * Programa para realizar a importa��o do arquivo para a mem�ria
	 * 
	 * @param requestCode
	 *            - codigo da requisi��o
	 * @param resultCode
	 *            - c�digo do resultado obtido
	 * @param data
	 *            - dados retornados pela intent
	 * @return
	 */
	@SuppressWarnings({ "static-access"})
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public boolean importar(int requestCode, int resultCode, Intent data, ControladorDoDB mc, String TABELA) {
		boolean b = true;
		switch (requestCode) {
		case 1:
			if (resultCode == ac.RESULT_OK) {
				Uri uri = null;
				uri = data.getData();
				Log.i(NOMEDOPROGRAMA, "Uri: " + uri.toString());
				try {
					InputStream is = ac.getContentResolver().openInputStream(uri);
					Reader rd = new InputStreamReader(is, StandardCharsets.UTF_8);// utilizando
																					// utf-8
					int ch;
					String text = "";
					while ((ch = rd.read()) != -1) {
						if (ch != 10)
							if (ch != 13) {
								// if(ch!=34) //se char n�o � "
								text += String.valueOf((char) ch);
							} else {
								distribuidor: {
									int tag = Integer.valueOf(text.substring(text.lastIndexOf(",") + 1, text.length()));
									text = text.substring(0, text.lastIndexOf(","));
									String morto = text.substring(text.lastIndexOf(",") + 2, text.length() - 1);
									text = text.substring(0, text.lastIndexOf(","));
									String ideia = text.substring(1, text.length() - 1);
									Long l = mc.inserirRow(ideia, morto, TABELA, tag);
									text = ""; // limpa variavel
									if (l == -1) {
										b = false;
									}
									break distribuidor;
								} // esse trecho de codigo chamado distribuidor
									// (label), distribui o conte�do da vari�vel
									// text nas vari�veis: tag, morto e ideia.
							}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return b;
	}
}
