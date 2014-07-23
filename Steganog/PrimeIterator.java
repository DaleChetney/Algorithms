import java.util.ArrayList;
import java.util.Iterator;

public class PrimeIterator implements Iterator<Integer>{
		ArrayList<Integer> numList;
		int index;
	public PrimeIterator(int max){
		int maxSqrt = (int) Math.sqrt(max);
		boolean[] isDivisible = new boolean[max+1];
		numList = new ArrayList<Integer>();
		for(int i=2; i<=maxSqrt; i++){
			if(!isDivisible[i]){
				numList.add(i);
				for (int j=i*i; j <= max; j+=i){
					isDivisible[j] = true;
				}
			}
		}
		for(int i=maxSqrt;i<=max; i++){
			if(!isDivisible[i])numList.add(i);
		}
		index = 0;
	}
	
	@Override
	public boolean hasNext() {
		return index<numList.size();
	}

	@Override
	public Integer next() {
		return numList.get(index++);
	}

	@Override
	public void remove() {
		
		
	}
}