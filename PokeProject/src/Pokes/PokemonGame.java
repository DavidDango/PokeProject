package Pokes;

public class PokemonGame {
	private Pokemon activeA;
	private Pokemon activeB;
	private int[] avaibleA;
	private int[] avaibleB;
	private Player playerA;
	private Player playerB;
	private int currentA;
	private int currentB;
	private int turnCounterA;
	private int turnCounterB;
	private boolean pokemonTurn;

	public void players(Player player1, Player player2) {
		playerA = player1;
		playerB = player2;
		activeA = playerA.getPokemon(0);
		activeB = playerB.getPokemon(0);
		avaibleA = new int[playerA.avaiblePokemon()];
		avaibleB = new int[playerB.avaiblePokemon()];
		for(int i = 0; i < playerA.avaiblePokemon(); i++) {
			avaibleA[i] = 1;
		}
		for(int i = 0; i < playerB.avaiblePokemon(); i++) {
			avaibleB[i] = 1;
		}
		pokemonTurn = true;
		turnCounterA = 1;
		turnCounterB = 1;
	}

	public Pokemon PokemonA() {
		return activeA;
	}
	
	public Pokemon PokemonB() {
		return activeB;
	}

	public int[] avaibleA() {
		return avaibleA;
	}
	
	public int[] avaibleB() {
		return avaibleB;
	}

	public void playerASwitch(int i) {
		if(playerA.getPokemon(i).canFight()) {
			activeA.resetStats();
			activeA = playerA.getPokemon(i);
			currentA = i;
			turnCounterA = 1;
		}
	}
	
	public void playerBSwitch(int i) {
		if(playerB.getPokemon(i).canFight()) {
			activeB.resetStats();
			activeB = playerB.getPokemon(i);
			currentB = i;
			turnCounterB = 1;
		}
	}

	public void AtoB(int i) {
		double power = activeA.getMove(i).getPower();
		double a = activeA.getMove(i).effectiveAttack(activeA);
		double d = activeA.getMove(i).effectiveDefense(activeB);
		double damage = (22*power*a/(d*50)) + 2;
		double stab = activeA.type1().stab(activeA.getMove(i).type())*activeA.type2().stab(activeA.getMove(i).type());
		double extra = activeA.getAbility().offensive(activeA.getMove(i), activeB)*activeB.getAbility().defensive(activeA.getMove(i), activeB);
		activeB.receiveDamage((int) (damage*stab*extra));
		activeA.apply(activeA.getMove(i).getStatus());
		if(!activeB.canFight()) {
			avaibleB[currentB] = 0;
			playerB.lost1();
		}
	}
	
	public void BtoA(int i) {
		double power = activeB.getMove(i).getPower();
		double a = activeB.getMove(i).effectiveAttack(activeB);
		double d = activeB.getMove(i).effectiveDefense(activeA);
		double damage = (22*power*a/(d*50)) + 2;
		double stab = activeB.type1().stab(activeB.getMove(i).type())*activeB.type2().stab(activeB.getMove(i).type());
		double extra = activeB.getAbility().offensive(activeB.getMove(i), activeA)*activeA.getAbility().defensive(activeB.getMove(i), activeA);
		activeA.receiveDamage((int) (damage*stab*extra));
		activeB.apply(activeB.getMove(i).getStatus());
		if(!activeA.canFight()) {
			avaibleA[currentA] = 0;
			playerA.lost1();
		}
	}

	public boolean priority(int pokemonA, int pokemonB) {
		if(activeA.getMove(pokemonA).getPriority() > activeB.getMove(pokemonB).getPriority()) {
			return true;
		}
		if(activeA.getMove(pokemonA).getPriority() < activeB.getMove(pokemonB).getPriority()) {
			return false;
		}
		if(activeA.effectiveSpeed() > activeB.effectiveSpeed()) {
			return true;
		}
		if(activeA.effectiveSpeed() < activeB.effectiveSpeed()) {
			return false;
		}
		if(pokemonTurn) {
			return true;
		}
		return false;
	}

	public void battle(int pokemonA, int pokemonB) {
		if(activeA.canFight() && activeB.canFight()) {
			if(priority(pokemonA, pokemonB)) {
				if((activeA.getMove(pokemonA).requiresFirstTurn() && turnCounterA == 1) || !activeA.getMove(pokemonA).requiresFirstTurn()) {
					AtoB(pokemonA);
					if(activeB.canFight() && !activeA.getMove(pokemonA).isFlinch() && ((activeB.getMove(pokemonB).requiresFirstTurn() && turnCounterB == 1) || !activeB.getMove(pokemonB).requiresFirstTurn())) {
						BtoA(pokemonB);
					}
				}
				else if((activeB.getMove(pokemonB).requiresFirstTurn() && turnCounterB == 1) || !activeB.getMove(pokemonB).requiresFirstTurn()) {
					BtoA(pokemonB);
				}
			}
			else {
				if((activeB.getMove(pokemonB).requiresFirstTurn() && turnCounterB == 1) || !activeB.getMove(pokemonB).requiresFirstTurn()) {
					BtoA(pokemonB);
					if(activeA.canFight() && !activeB.getMove(pokemonB).isFlinch() && ((activeA.getMove(pokemonA).requiresFirstTurn() && turnCounterA == 1) || !activeA.getMove(pokemonA).requiresFirstTurn())) {
						AtoB(pokemonA);
					}
				}
				else if((activeA.getMove(pokemonA).requiresFirstTurn() && turnCounterA == 1) || !activeA.getMove(pokemonA).requiresFirstTurn()) {
					AtoB(pokemonB);
				}
			}
			turnCounterA++;
			turnCounterB++;
			pokemonTurn = !pokemonTurn;
		}
	}
	
}
