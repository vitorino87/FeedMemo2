package controller;

import android.annotation.SuppressLint;
import java.util.Random;

public class GeradorDeCorRandomizado {
	@SuppressLint("DefaultLocale")
	public String gerarCorRandomizada() {
		Random random = new Random();
		int hex = random.nextInt(0xF);
		String cor = "";
		for(int i=0;i<6;i++){
			while(hex<0xD){
				hex = random.nextInt(0xF);
			}			
			cor += String.format("%01x", hex);
			hex = random.nextInt(0xF);
		}				
		return cor;		
	}
}
