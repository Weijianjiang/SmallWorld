package edu.southalabama.csc527.smallworld.model;

public class Stats {
	
		private int f_HP;
		private int f_MP;
		private int f_Str;
		private int f_Def;
		private int f_Int;
		private int f_Hit;
		private String f_name;
		private int f_maxHP;
		private int f_maxMP;

		public Stats(int HP,int MP,int Str,int Def,int Int,int Hit, String name, int maxHP, int maxMP)
		{
			f_HP=HP;
			f_MP=MP;
			f_Str=Str;
			f_Def=Def;
			f_Int=Int;
			f_Hit=Hit;
			f_name = name;
			f_maxHP = maxHP;
			f_maxMP = maxMP;
		
		}
		public int getHP(){
		return f_HP;
		}

		public void setHP(int newHP){
		f_HP = newHP;
		}
		
		public int getMP(){
		return f_MP;
		}

		public void setMP(int newMP){
		f_MP = newMP;
		}
		
		public int getStr(){
		return f_Str;
		}

		public void setStr(int newStr){
		f_Str = newStr;
		}
		
		public int getDef(){
		return f_Def;
		}

		public void setDef(int newDef){
		f_Def = newDef;
		}
		
		public int getInt(){
		return f_Int;
		}

		public void setInt(int newInt){
		f_Int = newInt;
		}
		
		public int getHit(){
		return f_Hit;
		}

		public void setHit(int newHit){
		f_Hit = newHit;
		}
		
		public String getName(){
			return f_name;
		}
		
		public void setName(String name){
			f_name = name;
		}
		
		public int getMaxHP(){
			return f_maxHP;
		}
		
		public int getMaxMP(){
			return f_maxMP;
		}
}


