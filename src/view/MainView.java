package view;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import feedme.feedmemo2.R;
import android.annotation.SuppressLint;	
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import controller.ControladorDoDB;
import controller.GuardadorDeEstadosTemplate;

@SuppressLint({ "ClickableViewAccessibility", "DefaultLocale" })
public class MainView extends TelaTemplate implements OnTouchListener, OnGestureListener{	
	protected static ControladorDoDB mc = null;		
	static LinearLayout ll = null;		
	public static Context context;
	static EditText ideia = null;
	protected final static String TABELA="ideias";
	ViewGroup gi = null;
	protected Menu menu = null;
	Menu menu2 = null;	
	static boolean allcaps = false;
	static boolean isColored = true;
	private static String bkp = "";
	protected JanelaDeTags jt;
	static TextView tagView = null;
	static TextView tagMax;	
	int minId = 0;
	int maxId = 0;
	int currentId =0;
	AlteradorDeIdeia ai;
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); //chama o método onCreate da Classe mãe
		context = this.getApplicationContext(); //pega o contexto desta View		
		mc = new ControladorDoDB(context); //instancia um MainControl com o contexto desta View
		setContentView(R.layout.tela1);	//Carrega a tela1	
		ll = (LinearLayout)findViewById(R.id.linearLayout);//conecta o linearLayout a variável ll
		gestureDetector = new GestureDetector(MainView.this, MainView.this);//instancia o gesture para trabalhar com os gestos na tela
		ideia = (EditText)findViewById(R.id.editText1);//conecta o editText1 a variável ideia
		tagView = (TextView) findViewById(R.id.textView1);
		tagMax = (TextView)findViewById(R.id.tagMax);
		
		//método para adicionar a ação de Touch no LinearLayout
		ll.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				gestureDetector.onTouchEvent(event);
				ll.onTouchEvent(event);
				return true;
			}
		});		
		//método para adicionar a ação de Touch no EditText
		ideia.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {				
				gestureDetector.onTouchEvent(event);
				ideia.onTouchEvent(event);
				return true;
			}
		});				
		loadIdeias();//faz a busca e carrega os dados do banco num cursor		
		carregarFirst();//move o cursor para o primeiro elemento retornado do banco							
	}	
	
	/**
	 * Método inicial para carregar os resultados da tabela memoria do banco sem dead files
	 */
	public static void loadIdeias(){
		try{
			mc.setMorto("n");
			mc.setMinId(mc.getIdMinDB());
			mc.setMaxId(mc.getMinId()+5);
			mc.setTipoDeQuery(3);
			mc.retornarTodosResultados(TABELA);
		}catch(Exception e){
			
		}
	}
	
	/**
	 * Método para carregar a ideia no EditText
	 */
	@SuppressLint("NewApi")
	public static void carregarIdeia(){
		try{			
			String b = mc.nextResult();			
			carregar(b);			
		}catch(Exception ex){}		
	}
		
	/**
	 * Método para carregar a 1ª ideia no EditText
	 */
	public static void carregarFirst(){
		try{
			String b = mc.initialResult();
			carregar(b);
		}catch(Exception ex){			
		}
	}
	
	/**
	 * Método para carregar a ideia no EditText
	 */
	public static void carregarIdeiaAnterior(){
		try{
			String b = mc.previousResult();
			if(!b.equals(""))
				carregar(b);
		}catch(Exception ex){}		
	}
	
	public static void carregarIdeia(int id){
		try{
			String b = mc.currentResultId(id);
			carregar(b);
		}catch(Exception ex){}
	}
	
	@SuppressLint("DefaultLocale")
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static void carregar(String b){
		bkp = b;
		String a = null;
		try {              //por questões de compatibilidade será necessário deixar esses replaces
			a = new String(b.getBytes("UTF8"), StandardCharsets.UTF_8);
			a = a.replace("\u0375", ",");
			controller.FormatadorDeTexto ft = new controller.FormatadorDeTexto();
			a = ft.formatOutputText(a);          //troca por char(10)
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}	
		
		if(allcaps)
			ideia.setText(a.toUpperCase());
		else
			ideia.setText(a);
		
		if(ideia.getLineCount()>1)
			ideia.setGravity(Gravity.FILL);
		else
			ideia.setGravity(Gravity.CENTER);			
		if(isColored){
			controller.GeradorDeCorRandomizado gcr = new controller.GeradorDeCorRandomizado();
			String cor = gcr.gerarCorRandomizada();
			ll.setBackgroundColor(Color.parseColor("#"+cor));
			ideia.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}else
			ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
		
		tagMax.setText("Tag Max: "+mc.getTagMax());	
		tagView.setText("Tag: "+mc.getTagAtual());		
	}
	
	/**Explicação:
	 * Método executado ao tocar em algum item do menu
	 * @author Tiago Vitorino
	 * @since 16/02/2019 
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		jt = new JanelaDeTags(MainView.this, mc, TABELA, ideia.getText().toString(),menu);
		ai = new AlteradorDeIdeia(MainView.this , mc , TABELA);
		int id = item.getItemId();		
		int a;
		switch (id) {
		case R.id.item1:		
			try{
			//mc.armazenarPositionDoCursor();//esse método armazena a posição do Cursor, antes de chamar a próxima tela 			
			Intent it = new Intent(context, MainView2.class);//Criando a intenção de chamar a próxima classe/Tela
			startActivity(it);//Inicia o método onCreate da classe MainView2
			}catch(Exception e){
				
			}
			break;
		case R.id.item2:
			//a = mc.armazenarPositionDoCursor();
			int idDb = mc.getCurrentId();
			if(mc.addOrDelDeadFile(TABELA, ideia.getText().toString(),"s")!=-2){
				Toast.makeText(context, "Adicionado ao arquivo morto", Toast.LENGTH_LONG).show();	
				mc.setMorto("n");
				mc.setMinId(idDb);
				mc.setTipoDeQuery(3);
				mc.setMaxId(mc.getMinId()+5);
				mc.retornarTodosResultados(TABELA);
				if(mc.getCursor().getCount()<=0){
					mc.setMinId(mc.getIdMinDB());
					mc.setMaxId(idDb);
					mc.retornarTodosResultados(TABELA);
				}
				carregarFirst();
			}else{
				Toast.makeText(context, "Não foi possível adicionar ao arquivo morto", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.item3:
			int tipoQuery = mc.getTipoDeQuery();	
			int idCurrent = mc.getCurrentId();
			int tagAtual = mc.getTagAtual();
			mc.setTipoDeQuery(3);
			mc.setMorto("s");
			mc.retornarTodosResultados(TABELA);
			if(mc.getCursor().getCount()<=0){
				Toast.makeText(context, "Não há Dead Files", Toast.LENGTH_LONG).show();
				if(tipoQuery==2){
					mc.setTipoDeQuery(2);
					mc.setTag(tagAtual);
					mc.setMinId(idCurrent-2);
					mc.setMaxId(idCurrent+2);
					mc.retornarTodosResultados(TABELA);
					carregarIdeia(idCurrent);
				}else if(tipoQuery==3){
					mc.setTipoDeQuery(3);
					mc.setMorto("n");
					mc.setMinId(idCurrent-2);
					mc.setMaxId(idCurrent+2);
					mc.retornarTodosResultados(TABELA);
					carregarIdeia(idCurrent);
				}
			}else{								
				Toast.makeText(context, "Dead Files", Toast.LENGTH_LONG).show();				
				menu.clear();
				MenuDoMainView mmv = new MenuDoMainView(MainView.this, menu);	   
			    mmv.chamarMenuInicial(R.menu.menu2);
			    carregarFirst();
			}
			break;		
		case R.id.item4:			
			if (item.isChecked()) item.setChecked(false);
            else item.setChecked(true);			
			allcaps = item.isChecked();				
			if(allcaps)
				ideia.setText(ideia.getText().toString().toUpperCase());
			else
				ideia.setText(bkp);
			break;
		case R.id.item5:
			a = mc.getCurrentId();
			if(mc.addOrDelDeadFile(TABELA, ideia.getText().toString(), "n")!=2){
				Toast.makeText(context, "Removido do arquivo morto", Toast.LENGTH_LONG).show();
				mc.setMinId(a);
				mc.setMaxId(mc.getIdMaxDB());
				mc.setMorto("s");
				mc.setTipoDeQuery(3);
				mc.retornarTodosResultados(TABELA);
				if(mc.getCursor().getCount()<=0){
					mc.setMinId(mc.getIdMinDB());
					mc.setMaxId(a);
					mc.retornarTodosResultados(TABELA);
					if(mc.getCursor().getCount()<=0){
						menu.clear();
						MenuDoMainView mmv = new MenuDoMainView(MainView.this, menu);	   
					    mmv.chamarMenuInicial(R.menu.menu);
						Toast.makeText(context, "Não há Dead Files, por isso Retornou", Toast.LENGTH_LONG).show();
						mc.setMorto("n");
						mc.retornarTodosResultados(TABELA);						
					}
				}
				carregarFirst();								
			}else{
				Toast.makeText(context, "Não foi possível remover do arquivo morto", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.item6:
			retornar();
			break;
			
		case R.id.item7:
			if(item.isChecked())
				item.setChecked(false);
			else
				item.setChecked(true);				
			isColored=item.isChecked();		
			break;			
		case R.id.item8:			
			jt.onCreateDialog(0).show();	
			JanelaDeTags.setChooseTela(0);
			break;			
		case R.id.itemVisualizarItensTag:
			jt.onCreateDialog(2).show();
			JanelaDeTags.setChooseTela(2);
			break;			
		case R.id.itemChangeTag:
			jt.onCreateDialog(1).show();
			JanelaDeTags.setChooseTela(1);
			break;			
		case R.id.itemTagRetornar:
			retornar();
			JanelaDeTags.checarMenu = false;
			break;
		case R.id.itemAtualizar:
			ai.setId(mc.getCurrentId());
			ai.setText(ideia.getText().toString());
			ai.onCreateDialog().show();
			break;
		case R.id.itemDeletar:
			ConfirmadorDeDel cd = new ConfirmadorDeDel(this, mc,TABELA,ideia.getText().toString());
			cd.onCreateDialog().show();			
			break;
		}		
		return super.onOptionsItemSelected(item);
	}
	
	public void retornar(){	
		menu.clear();
		MenuDoMainView mmv = new MenuDoMainView(MainView.this, menu);	   
	    mmv.chamarMenuInicial(R.menu.menu);
	    JanelaDeTags.setChooseTela(0);
	    loadIdeias();
		carregarFirst();
	}
		
	/**
	 * Método de pause	
	 */
	@Override
	protected void onPause(){
		super.onPause();
	}	
	/**
	 * Método de stop
	 */
	@Override
	protected void onStop(){
		super.onStop();
		try{
			GuardadorDeEstadosTemplate gd = new GuardadorDeEstadosTemplate();
			if(mc.getTipoDeQuery()==4 || mc.getTipoDeQuery()==-1)
				gd.guardarEstado("tipoSql", 3, this);
			else
				gd.guardarEstado("tipoSql", mc.getTipoDeQuery(), this);
				gd.guardarEstado("minId", mc.getCurrentIdMin(), this);
				gd.guardarEstado("maxId", mc.getCurrentIdMax(), this);
				gd.guardarEstado("currentId", mc.getCurrentId(), this);
				gd.guardarEstado("tag", JanelaDeTags.tagCarregada, this);
				gd.guardarEstado("morto", mc.getMorto(), this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que ocorre ao fechar app
	 */
	@Override
	protected void onDestroy(){
		super.onDestroy();	
	}
	
	/**
	 * Método resume
	 */
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		try{
			mc.abrirConexao();		
			GuardadorDeEstadosTemplate gd = new GuardadorDeEstadosTemplate();
			int a = gd.restaurarEstado("currentId", this);
			mc.setMaxId(gd.restaurarEstado("maxId", this));
			mc.setMinId(gd.restaurarEstado("minId", this));
			mc.setTipoDeQuery(gd.restaurarEstado("tipoSql", this));	
			mc.setTag(gd.restaurarEstado("tag", this));
			mc.setMorto(gd.restaurarEstado2("morto", this));						
			if(mc.getTipoDeQuery()==2 && mc.getMaxId()!=-1 && mc.getMinId()!=-1 || mc.getTipoDeQuery()==3
					&& mc.getMaxId()!=-1 && mc.getMinId()!=-1){	
				mc.retornarTodosResultados(TABELA);
				//mc.nextResult();
				if(menu!=null)
					onCreateOptionsMenu(menu);
				if(a==-1){
					mc.getCursor().moveToFirst();
					mc.initialResult();
				}else{
					carregarIdeia(a);
				}									
			}else{
				loadIdeias();
				carregarFirst();
			}
		}catch(Exception ex){
			loadIdeias();
			carregarFirst();
		}		
	}
	
	@SuppressLint("DefaultLocale")
	public static void mudarACor() {
		Random random = new Random();
		String id = String.format("%06d", random.nextInt(999999));
		ideia.setTextColor(Color.parseColor("#" + id));		
	}		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	    	    
	    MenuDoMainView mmv = new MenuDoMainView(MainView.this, menu);	
	    this.menu = menu;
	    menu.clear();
	    try{	    	
	    	switch(mc.getTipoDeQuery()){
	    	case 2:
	    		if(mc.getTipoDeQuery()==2 && mc.getMaxId()!=-1 && mc.getMinId()!=-1){	    			
	    			mmv.chamarMenuInicial(R.menu.menu);
	    			mmv.chamarMenuInicial(R.menu.menutags);
	    			menu.removeItem(R.id.item8);
	    			menu.removeItem(R.id.item2);
	    			JanelaDeTags.checarMenu = true;
	    		}else{
	    			mmv.chamarMenuInicial(R.menu.menu);
	    		}
	    		break;
	    	case 3:
	    		if(mc.getTipoDeQuery()==3 && mc.getMorto().equals("s") && mc.getMaxId()!=-1 && mc.getMinId()!=-1){	    			   
	    			mmv.chamarMenuInicial(R.menu.menu2);
	    		}else{
	    			mmv.chamarMenuInicial(R.menu.menu);
	    		}
	    		break;	    		    		
	    	default:
	    		mmv.chamarMenuInicial(R.menu.menu);
	    		break;
	    	}
		}catch(Exception e){
				
		}
	    return true;
	}
	@Override
	public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float velocityX, float velocityY) {
		if(motionEvent1 == null){
			return false;
		}else{
			//movimento da direita para esquerda
			if (motionEvent1.getX() - motionEvent2.getX() > 50) {
				controller.TelaAux.moverDireitaParaEsquerda();
				return true;
			}
			//movimento da esquerda para direita
			if (motionEvent2.getX() - motionEvent1.getX() > 50) {
				controller.TelaAux.moverEsquerdaParaDireita();
				return true;
//			} else {
//				return true;
			}else if (motionEvent1.getY() - motionEvent2.getY() > 100){

				return true;
			}else if (motionEvent2.getY() - motionEvent1.getY() > 50){
				changeTag();
				return true;
			}
			return false;
		}
	}
	
	public void changeTag(){
		try {											
			int a = mc.getCurrentId(); // captura o id
										// atual			
			mc.addOrChangeTag(TABELA, a, mc.getTagChangeTag());
			MainView.tagMax.setText("Tag Max: " + mc.getTagMax());
			if (JanelaDeTags.getChooseTela() == 0) {
				mc.setMinId(a - 2);
				if (mc.getMinId() < 0)
					mc.setMinId(0);
				mc.setMaxId(a + 2);
				mc.setMorto("n");
				mc.setTipoDeQuery(3);
				mc.retornarTodosResultados(TABELA);
				MainView.carregarIdeia(a);
				MainView.tagView.setText("Tag: " + mc.getTagAtual());
				Toast.makeText(MainView.this, "Adicionado na tag " + mc.getTagChangeTag(), Toast.LENGTH_SHORT).show();
			} else {
				// essa parte do código só ocorre na
				// funcionalidade
				// "Visualizar tag..."
				// nas próximas 6 linhas o app tentará
				// carregar as
				// ideias do id atual até o id máximo.
				Toast.makeText(MainView.this, "Alterado para tag " + mc.getTagChangeTag(), Toast.LENGTH_SHORT).show();
				mc.setTipoDeQuery(2);
				mc.setTag(JanelaDeTags.tagCarregada);
				mc.setMinId(a);
				mc.setMaxId(mc.getIdMaxDB());
				mc.retornarTodosResultados(TABELA);
				if (mc.getCursor().getCount() <= 0) {
					// ...se não conseguir encontrar
					// nada o app
					// tentará carregas as ideias do id
					// mínimo até o
					// id atual
					mc.setMinId(mc.getIdMinDB());
					mc.setMaxId(a);
					mc.retornarTodosResultados(TABELA);
					if (mc.getCursor().getCount() <= 0) {
						// ...se não conseguir nada ele
						// retorna às
						// ideias gerais
						menu.clear();
						JanelaDeTags.checarMenu = false;
						MenuDoMainView mmv = new MenuDoMainView(MainView.this, menu);
						mmv.chamarMenuInicial(R.menu.menu);
						mc.setMorto("n");
						mc.setTipoDeQuery(4);
						mc.retornarTodosResultados(TABELA);
						Toast.makeText(MainView.this, "Retornou porque não há mais tag " + JanelaDeTags.tagCarregada,
								Toast.LENGTH_LONG).show();
						JanelaDeTags.setChooseTela(0);
					}
				}
				MainView.carregarFirst();
			}
		} catch (NumberFormatException e) {
			Toast.makeText(MainView.this, "Não foi possível adicionar a tag", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}
}
