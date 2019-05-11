package view;

import feedme.feedmemo2.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import controller.ControladorDoDB;
import controller.FormatadorDeTexto;

@SuppressLint("NewApi")
public class AlteradorDeIdeia{

	Activity ac;
	ControladorDoDB mc;
	String tabela;
	TextView tx = null;
	Menu menu;
	MenuDoMainView mmv;
	EditText edt;
	ContentValues cv;
	private String text;
	private int idDB;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getId1() {
		return idDB;
	}

	public void setId(int id) {
		this.idDB = id;
	}

	AlteradorDeIdeia(Activity ac, ControladorDoDB mc, String tabela) {
		this.ac = ac;
		this.mc = mc;
		this.tabela = tabela;
	}

	public Dialog onCreateDialog() {		
		AlertDialog.Builder alert = new AlertDialog.Builder(ac);
		// Get the layout inflater
		LayoutInflater inflater = ac.getLayoutInflater();		
		final View layout = inflater.inflate(R.layout.atualizar_ideia, null);
		/////////ELEMENTOS EXTERNOS AO MÉTODO////////////////////////////////
		edt = (EditText) layout.findViewById(R.id.edtAlterar);
		edt.setText(text);		
		cv = new ContentValues();
		/////////////////////////////////////////////////////////////////////
		alert.setView(layout)
		.setTitle("Alterar")
		.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
		/////////ELEMENTOS EXTERNOS AO MÉTODO////////////////////////////////
				atualizarIdeia();
		/////////////////////////////////////////////////////////////////////				
			}
			})
        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });			
		return alert.create();
	}
	
	public void atualizarIdeia(){
		String ideia = edt.getText().toString();
		//ideia = ideia.replace(",", "\u0375");
		FormatadorDeTexto ft = new FormatadorDeTexto();
		ideia = ft.formatInputText(ideia);
		if (ideia.contains(","))
			ideia = ideia.replace(",", "\u0375");
		cv.put("ideia", ideia);
		if (mc.atualizarDB2(cv, tabela, idDB) != -2) {
			Toast.makeText(ac, "Ideia atualizada!", Toast.LENGTH_SHORT).show();
			;
		} else {
			Toast.makeText(ac, "Ideia não atualizada!", Toast.LENGTH_SHORT).show();
		}
		mc.setMinId(idDB-2);
		   if(mc.getMinId()<0)
			   mc.setMinId(0);
		   mc.setMaxId(idDB+2);
		   mc.setMorto("n");
		   mc.setTipoDeQuery(3);
		   mc.retornarTodosResultados(tabela);
		   MainView.carregarIdeia(idDB);
	}
}
