package view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Toast;
import controller.ControladorDoDB;
import controller.FormatadorDeTexto;

public class ConfirmadorDeDel {
	Activity ac;
	ControladorDoDB mc;
	String tabela, ideia;	
	boolean respUser = false;
	ConfirmadorDeDel(Activity ac, ControladorDoDB mc, String tabela, String ideia){
		this.ac = ac;
		this.mc = mc;
		this.tabela = tabela;
		this.ideia = ideia;
	}
	
	public Dialog onCreateDialog(){
		AlertDialog.Builder alert = new AlertDialog.Builder(ac);
		alert.setMessage("Deseja apagar essa ideia?");
		alert.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {			
				//ideia = ideia.replace(",", "\u0375");
				FormatadorDeTexto ft = new FormatadorDeTexto();
				ideia = ft.formatInputText(ideia);
				if(mc.deletarRow(ideia, tabela)){
					Toast.makeText(ac, "Apagada com sucesso!", Toast.LENGTH_SHORT).show();										
					mc.setMinId(mc.getCurrentId());
					mc.setMaxId(mc.getIdMaxDB());
					if(mc.getMaxId()< mc.getMinId())
						mc.setMinId(mc.getMaxId());
					mc.retornarTodosResultados(tabela);
					MainView.carregarFirst();
				}else
					Toast.makeText(ac, "Não foi possível apagar", Toast.LENGTH_SHORT).show();				
			}
		})
		.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		return alert.create();
	}
}
