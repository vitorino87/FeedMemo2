package controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import model.Banco;

public class ControladorDoDB {

	protected SQLiteDatabase db;
	Banco banco = null;
	Cursor cursor;
	Boolean previous=false;
	int tipoDeQuery = 0;	
	String morto;
	int minId, maxId;
	int tag;
	int tagChangeTag;
	final String TABELA = "ideias";
	
	public int getTagChangeTag() {
		return tagChangeTag;
	}

	public void setTagChangeTag(int tagChangeTag) {
		this.tagChangeTag = tagChangeTag;
	}

	public int getTag() {		
			return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public void setMinId(int minId) {
		this.minId = minId;
	}
	
	public int getMaxId() {
		return maxId;
	}

	public int getMinId(){
		return minId;
	}

	public void setMaxId(int maxId) {
		this.maxId = maxId;
	}

	public String getMorto() {
		return morto;
	}

	public void setMorto(String morto) {
		this.morto = morto;
	}

	public int getTipoDeQuery() {
		return tipoDeQuery;
	}

	public void setTipoDeQuery(int tipoDeQuery) {
		this.tipoDeQuery = tipoDeQuery;
	}

	public ControladorDoDB(Context context){
		banco = new Banco(context, "Memoria");
		abrirConexao();
	}
	
	public int getIdMaxDB(){
		abrirConexao();
		return banco.getMaxId(db);
	}
	
	// fechando banco
	public void fecharConexao() {
		try{
			banco.close();
		}catch(Exception ex){			
		}		
	}

	// conectar no banco
	public void abrirConexao() {	
		try{
		db = banco.getWritableDatabase();
		}catch(Exception e){
			e.printStackTrace();
			db=null;
		}
	}
	
	public int getCurrentIdMin(){
		int pos = cursor.getPosition();
		cursor.moveToFirst();
		int minId = cursor.getInt(0);
		cursor.moveToPosition(pos);
		return minId;
	}
	
	public int getCurrentIdMax(){			
		int pos = cursor.getPosition();
		cursor.moveToLast();
		int maxId = cursor.getInt(0);
		cursor.moveToPosition(pos);
		return maxId;
	}
	
	public int currentId(){
		return cursor.getInt(0);		
	}
			
	public void retornarTodosResultados(String tabela){
		abrirConexao();
		//boolean checar = true;
		//int max = getCurrentIdMax();
		try{
		//while(checar && banco.getMaxId(db)>=maxId){
			switch(tipoDeQuery){
			case 1:
				cursor = banco.retornarTodosResultados(db, tabela,1, null);
				break;
			case 2:
				String[] info = {String.valueOf(tag), String.valueOf(minId), String.valueOf(maxId)};
				cursor = banco.retornarTodosResultados(db, tabela, 2, info);				
				break;
			case 3:
				String[] info2 = {morto, String.valueOf(minId), String.valueOf(maxId)};
				cursor = banco.retornarTodosResultados(db, tabela, 3, info2);
				break;
			case 4:
				String[] info3 = {morto};
				cursor = banco.retornarTodosResultados(db, tabela, 4 , info3);
				break;
			case 5:
				String[] info4 = {morto, String.valueOf(minId), String.valueOf(maxId)};
				cursor = banco.retornarTodosResultados(db, tabela, 5, info4);
				break;
			case 6:
				String[] info5 = {String.valueOf(tag), String.valueOf(minId), String.valueOf(maxId)};
				cursor = banco.retornarTodosResultados(db, tabela, 6, info5);
				break;
			}										
		}catch(Exception e){
			
		}
	}
	
	public String initialResult(){
		String b="";
		if(cursor.moveToFirst())
			b = cursor.getString(1);
		return b;
		
	}	
	
	public int getTagAtual(){
		int b=-1;
		try{
			b = cursor.getInt(3);
		}catch(Exception e){
			b=-1;
		}						
		return b;
	}
		
	
	public int getTagMax(){
		abrirConexao();
		return banco.getTagMax(db);
	}
	
	public int getCurrentId(){
		int b=-1;
		try{
			b = cursor.getInt(0);
		}catch(Exception e){
			b=-1;
		}						
		return b;
	}
	
	public String nextResult(){
		String b="";			
		try{
		if(!cursor.isLast())
			cursor.moveToNext();
		else if(getCurrentId()<banco.getMaxId(db)+1){
			minId = getCurrentIdMax()+1;
			maxId = banco.getMaxId(db)+1;                //(fixado)
			retornarTodosResultados(TABELA);
			if(cursor.getCount()<=0){
				minId = 0;
				maxId = banco.getMaxId(db)+1;
				retornarTodosResultados(TABELA);
			}
			cursor.moveToFirst();
		}else{
			minId = 0;
			maxId = banco.getMaxId(db)+1;
			retornarTodosResultados(TABELA);
			cursor.moveToFirst();
			//cursor.moveToPosition(0);
		}
					
		b = cursor.getString(1);	
		}catch(Exception e){}
		return b;
	}
	
	public String currentResult(int position){		
		String b="";	
		cursor.moveToPosition(position);			
		b = cursor.getString(1);								
		return b;
	}
	
	public String currentResultId(int id){		
		String b="";
		cursor.moveToFirst();
		try{
			if(cursor.getCount()>0){
				while(cursor.getInt(0)!=id){ //se no cursor houver o id procurado, o laço termina e o método retorna essa posição
					cursor.moveToNext();
					if(cursor.isLast()) //esse laço verifica se o cursor está na última posição
						break;			//se sim, ele quebra o laço e o método retorna a última posição.
				}
			}
		}catch(Exception ex){
			
		}				
		b = cursor.getString(1);								
		return b;
	}
	
	public String previousResult(){
		String b="";
		int current = getCurrentId();
		if(!cursor.isFirst()){
			cursor.moveToPrevious();					
		}else if(banco.getMinId(db)<getCurrentId()){  //verifica se o id min. do DB é menor que o id atual
			minId = 0;			
			maxId = getCurrentId()-1;				
			if(getTipoDeQuery()==2){
				setTipoDeQuery(6);
			}else if(getTipoDeQuery()==3){
				setTipoDeQuery(5);
			}
			retornarTodosResultados(TABELA);
			if(cursor.getCount()<=0){								
				maxId = banco.getMaxId(db)+1;
				//minId = maxId - 5;
				minId = current;
				//if(minId<0) minId = 0;
				retornarTodosResultados(TABELA);								
			}			
			cursor.moveToLast();
		}else{ 		
			if(getTipoDeQuery()==2){
				setTipoDeQuery(6);
			}else if(getTipoDeQuery()==3){
				setTipoDeQuery(5);
			}
			maxId = banco.getMaxId(db)+1;
			//minId = maxId - 5;
			minId = current;
			//if(minId<0) minId = 0;
			retornarTodosResultados(TABELA);
			cursor.moveToLast();				
		}
		if(getTipoDeQuery()==6){
			setTipoDeQuery(2);
		}else if(getTipoDeQuery()==5){
			setTipoDeQuery(3);
		}
		b = cursor.getString(1);		
		return b;
	}
	
	public Long inserirRow(Object ideia, Object morto, String tabela, int tag){
		//boolean b = banco.buscar(db, (String)ideia,tabela);
		//if(b == false){
			ContentValues cv = new ContentValues();
			cv.put("morto", (String)morto);
			cv.put("ideia", (String)ideia);
			cv.put("tag", tag);
			Long a = banco.inserir(db, cv, tabela);			
			return a;
		//}else{
			//return -1L;
		//}
		
	}
	
	public boolean deletarRow(String ideia, String tabela){
		abrirConexao();
		if(banco.deletarIdeia(ideia, db, tabela)>0){
			return true;
		}{
			return false;
		}
	}

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}
	
	public int atualizarDB(ContentValues cv, String tabela, String ideia){
		abrirConexao();		
		int a = banco.atualizarIdeia(db, cv, tabela, ideia);
		if(a!=-2){
			cursor = null;
		}
		return a;
	}
	
	public int atualizarDB2(ContentValues cv, String tabela, int id){
		abrirConexao();		
		int a = banco.atualizarIdeia2(db, cv, tabela, id);
		if(a!=-2){
			//cursor = null;
		}
		return a;
	}
	
	public int addOrChangeTag(String tabela, String ideia, int tag){
		ContentValues cv = new ContentValues();
		cv.put("tag", tag);
		int a = atualizarDB(cv, tabela, ideia);
		return a;
	}
	
	public int addOrChangeTag(String tabela, int id, int tag){
		ContentValues cv = new ContentValues();
		cv.put("tag", tag);
		int a = atualizarDB2(cv, tabela, id);
		return a;
	}
	
	public int addOrDelDeadFile(String tabela, String ideia, String dead){
		ContentValues cv = new ContentValues();
		cv.put("morto", dead);
		int a = atualizarDB(cv, tabela, ideia);
		return a;
	}		

	public int getIdMinDB() {
		abrirConexao();
		return banco.getMinId(db);
	}
}
