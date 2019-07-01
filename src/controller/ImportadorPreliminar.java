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
 * Esta classe é preliminar, ela não realiza a importação completa ela só
 * carrega o arquivo selecionado pelo usuário na memória do programa É na classe
 * MainView2 que é definido onde será salvo os dados
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
	 * Abre o arquivo que será exportado
	 * 
	 * @return número 2, significa que trata-se de uma importação
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
	 * Programa para realizar a importação do arquivo para a memória
	 * 
	 * @param requestCode
	 *            - codigo da requisição
	 * @param resultCode
	 *            - código do resultado obtido
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
								// if(ch!=34) //se char não é "
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
									// (label), distribui o conteúdo da variável
									// text nas variáveis: tag, morto e ideia.
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
