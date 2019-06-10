package controller;

import java.io.IOException;
import java.io.Writer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

public class GeradorDeCSV {			
	final String CHAVEPRIMARIA = "id";
	final String ULTIMACOLUNA = "tag";
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public boolean gerarAndSave(ControladorDoDB mc, String TABELA, Activity ac, Intent data){
		mc.abrirConexao();
		mc.setTipoDeQuery(1);
		mc.retornarTodosResultados(TABELA);
		Cursor cursor = mc.getCursor();		
		String csv="";
		int qtdeColunas = cursor.getColumnCount();
		int qtdeDeRows = cursor.getCount();		
		ExportadorTemplate et = new ExportadorTemplate(ac);
		Writer wr = null;
		cursor.moveToFirst();
		for(int i=0; i<qtdeDeRows;i++){
			for(int j=0; j<qtdeColunas;j++){				
				//n�o estou armazenando o campo id, porque ele � auto-increment�vel e chave prim�ria
				//abaixo, verifica se o campo � integer e converte para String
				if(cursor.getType(j)==Cursor.FIELD_TYPE_INTEGER && !cursor.getColumnName(j).equals(CHAVEPRIMARIA))  
					csv+=String.valueOf(cursor.getString(j));
				else if(cursor.getType(j)==Cursor.FIELD_TYPE_STRING) //verificando se o tipo � String
					csv+="\""+cursor.getString(j)+"\"";	//armazenando o campo ideia
				if(!cursor.getColumnName(j).equals(ULTIMACOLUNA) && !cursor.getColumnName(j).equals(CHAVEPRIMARIA))//verifica se a coluna � ideia e se n�o � a... 
					//...primeira coluna, pois a primeira coluna � id, isso evita que ele adiciona v�rgula na frente da ideia
					csv+=",";	//tag � o �ltimo campo, por isso n�o � necess�rio adicionar virgula
			}
			cursor.moveToNext();	//movendo para o pr�ximo resultado					
			csv+=((char)10);				//pulando linha
			wr = et.prepararExport(data);
			try {
				et.salvar(wr, csv);
			} catch (IOException e) {
				e.printStackTrace();
			}
			csv="";
		}
		if(wr!=null){
			try {
				wr.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}else{
			return false;
		}			
	}	
}
