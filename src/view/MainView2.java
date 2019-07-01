package view;

import java.util.ArrayList;
import java.util.Iterator;
import feedme.feedmemo2.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import controller.ControladorDoDB;
import controller.ExportadorTemplate;
import controller.FormatadorDeTexto;
import controller.GeradorDeCSV;
import controller.ImportadorPreliminar;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)

public class MainView2 extends Activity {
	public static EditText txtIdeia;
	Button btnInserir, btnDeletar;
	LinearLayout ll;
	ControladorDoDB mc;
	int pixelAnterior=0;
	Button btnExportar,btnImportar;
	final String TABELA="ideias";
	int exportarOuImportar = 0;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);// chama o método onCreate da Classe mãe
		setContentView(R.layout.tela2);// Carrega a tela2
		final Context context = this.getApplicationContext();// pega o contexto desta View
		txtIdeia = (EditText) findViewById(R.id.ideia);// conecta o EditText à variável txtIdeia
		btnInserir = (Button) findViewById(R.id.button1);// conecta o Button à variável btnInserir
		ll = (LinearLayout) findViewById(R.id.linearLayout); 
		btnExportar = (Button) findViewById(R.id.btnExportar);
		btnImportar = (Button) findViewById(R.id.btnImportar);
		
		btnImportar.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {				
				ImportadorPreliminar it = new ImportadorPreliminar(MainView2.this);		
				exportarOuImportar = it.abrirArquivo();
			}
		});
		
		btnExportar.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {				
				ExportadorTemplate et = new ExportadorTemplate(MainView2.this); //instanciando o Exportador
				exportarOuImportar = et.salvarComo();  //salvando o arquivo onde será exportado a informação desejada
			}
		});

		btnInserir.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mc = new ControladorDoDB(context);// instancia um MainControl com o contexto atual
				mc.abrirConexao();// abre a conexão com o banco
				String ideia = txtIdeia.getText().toString();// adiciona o texto adicionado pelo usuário na variável ideia				
				ideia = ideia.replace(",", "\u0375");
				FormatadorDeTexto ft = new FormatadorDeTexto();
				ideia = ft.formatInputText(ideia);
				if (!ideia.equals("")) { // se ideia não for ""
					Long l = mc.inserirRow(ideia,"n", TABELA,0); // insere no DB a string ideia na tabela memoria
					if (l > -1) { // se o método anterior retornar um valor maior que -1
						Toast.makeText(context, "Ideia Salva!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "Ideia já existe ou não pode ser salva!", Toast.LENGTH_SHORT).show();
					}
				}
				mc.fecharConexao(); // fecha a conexão
				txtIdeia.setText("");// limpa a tela
			}
		});

		

		FormatadorDeTexto ew2 = new FormatadorDeTexto();
		ew2.setEdt(txtIdeia);
		txtIdeia.addTextChangedListener(ew2);
		
//		txtIdeia.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {  //método para quando acontece algo no EditText
//			FormatadorDeTexto ew = new FormatadorDeTexto();
//			@Override
//			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
//					int oldRight, int oldBottom) {
//				setPixelAnterior(alterarTamanhoTexto(v, bottom, oldBottom, ew));//invocando o método para alterar tamanho texto
//				//ew.impedirMaisDeDuasLinhas(txtIdeia.getEditableText());
//			}			
//		});							
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { //método invocando ao retornar uma intent
		Context context = this.getApplicationContext();
		mc = new ControladorDoDB(context);// instancia um MainControl com o contexto atual
		mc.abrirConexao();
		switch(exportarOuImportar){
		case 1:			
			if(data!=null){
				GeradorDeCSV geraCSV = new GeradorDeCSV();					
				if(geraCSV.gerarAndSave(mc, TABELA, this,data)){
					Toast.makeText(this, "Exportado com Sucesso!", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(this, "Erro na exportação", Toast.LENGTH_LONG).show();
				}
			}						
			break;
		case 2:
			ImportadorPreliminar i = new ImportadorPreliminar(MainView2.this);
			boolean verifica=true; 
			verifica = i.importar(requestCode, resultCode, data, mc, TABELA); 
			if(verifica)
				Toast.makeText(this, "Importado com Sucesso!", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "Erro! Alguns itens não importados.", Toast.LENGTH_LONG).show();
			break;
		}					
		exportarOuImportar = 0;
	}	
	
	public void setPixelAnterior(int pixel){ //métodos para trabalhar com o tamanho do texto 
		this.pixelAnterior = pixel;				//reduzindo-o caso seja necessário
	}	
	public int getPixelAnterior(){
		return this.pixelAnterior;
	}
	public int alterarTamanhoTexto(View v, int bottom, int oldBottom, FormatadorDeTexto ew) {
		EditText et = (EditText) v;
		et.addTextChangedListener(ew);
		int b=0;
		if (bottom != oldBottom && oldBottom != 0) {
			b = ew.getQtdePixelHeightAntes();
			int a = et.getHeight();
			if (a > b) {
				et = ew.alterarTextSize(et);
				a = et.getHeight();								
			}
		}
		return b;
	}

	/**
	 * Método de pause
	 */
	@Override
	protected void onPause() {super.onPause();}

	/**
	 * Método de stop
	 */
	@Override
	protected void onStop() {super.onStop();}

	/**
	 * Método que ocorre ao fechar app
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			mc.fecharConexao();
		} catch (Exception ex) {}
	}

	/**
	 * Método resume
	 */
	@Override
	protected void onResume() {
		super.onResume();
		try {
			mc.abrirConexao();
		} catch (Exception ex) {}
	}

	/**
	 * Método iniciar
	 */
	@Override
	protected void onStart() {
		super.onStart();
		try {
		} catch (Exception ex) {}
	}
}
