package Moves;

import Pokes.Pokemon;
import Types.Types;

public class PhysicalDamage extends AbstractMove {
	
	public PhysicalDamage(Types type, int i, int j, Status status) {
		this.set(type, i, j, status);
	}
	
	public double effectiveAttack(Pokemon activeA) {
		return activeA.effectiveAttack();
	}

	public double effectiveDefense(Pokemon activeB) {
		return activeB.effectiveDefense();
	}
}
