package view;

import feedme.feedmemo2.R;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TelaTemplate extends Activity implements OnTouchListener, OnGestureListener{
	GestureDetector gestureDetector;		
	/**
	 * Método para deslizar o dedo na tela
	 * e obter alguma ação
	 * @author Tiago Vitorino
	 * @since 16/02/2019
	 */
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
			} else {
				return true;
			}
		}
	}
	@Override
	public boolean onDown(MotionEvent e) {
		
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		return false;
	}
	
	/**Explicação:
	*método para criar o menu
	*No KitKat ele cria aqueles 3 pontinhos que serve para acessar o menu.
	*Para funcionar é necessário criar uma pasta chamada menu em /res
	*E adicionar um .xml do tipo menu
	*E por fim adicionar um item ou um grupo 
	*Neste projeto foi adicionado um item.
	*@author Tiago Vitorino
	*@since 16/02/2019
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		//getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}		
}
