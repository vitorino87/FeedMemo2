package controller;

import view.MainView;

/**
 * Esta classe trabalha com a classe TelaTemplate
 * Serve para ajudar a TelaTemplate executar suas ações contribuindo 
 * no reaproveitamento de ambas as classes
 * É nesta classe em que irei alterar os métodos da classe TelaTemplate
 * @author Tiago Vitorino
 * @since 17/02/2019
 */
public class TelaAux extends MainView{	

	private TelaAux(){}
	/**
	 * Método executado ao mover o dedo na tela da direita para esquerda
	 */
	public static void moverDireitaParaEsquerda() {
		
		//System.out.println("Fui para esquerda");
		carregarIdeia();
		mudarACor();
	}

	/**
	 * Método executado ao mover o dedo na tela da esquerda para direita
	 */
	public static void moverEsquerdaParaDireita() {
	
		//System.out.println("Fui para direita");
		carregarIdeiaAnterior();
		mudarACor();
	}		
}
