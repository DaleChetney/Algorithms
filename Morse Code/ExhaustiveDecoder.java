import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.neumont.nlp.DecodingDictionary;
import edu.neumont.nlp.MorseCodeEncoder;


public class ExhaustiveDecoder {
	
	DecodingDictionary dict;
	float num;
	ArrayList<Possibility> possibilities;
	
	public ExhaustiveDecoder(DecodingDictionary dd, float n){
		dict = dd;
		num = n;
		possibilities = new ArrayList<Possibility>();
	}
	
	public List<String> decode(String message){
		decodeHelper(message,new String(""));
		sortPossibilities(0,possibilities.size()-1);
		ArrayList<String> top = new ArrayList<String>();
		for (int i=0; i<20&&i<possibilities.size(); i++)
		{
			top.add(possibilities.get(i).sentence);
		}
		return top;
	}
	
	private void decodeHelper(String toGo, String soFar){
		
		float likelihood = likelihood(soFar);
		if(toGo.isEmpty()){
			if(likelihood>10.0f/num){
				possibilities.add(new Possibility(soFar,likelihood));
			}
			return;
		}
		if(likelihood<10.0f/num)return;
		
		for(int i=1; i<toGo.length()+1; i++){
			
			String snip =  toGo.substring(0, i);
			Set<String> words = dict.getWordsForCode(snip);
			if(words!=null)
			{
				String newToGo = toGo.substring(i);
				for(String var : words)
				{
					String newSoFar = soFar+var+" ";
					decodeHelper(newToGo,newSoFar);
				}
			}
		}
	}
	
	private void sortPossibilities(int min, int max){
		
		if(min<max){
			int pivot = sortPivot(min,max);
			sortPossibilities(min,pivot-1);
			sortPossibilities(pivot+1,max);
		}
	}
	
	private int sortPivot(int min, int max){
		
		int pivotIndex = (min+max)/2;
		float pivotValue = possibilities.get(pivotIndex).likelihood;
		Possibility outtemp = possibilities.get(pivotIndex);
		possibilities.set(pivotIndex, possibilities.get(max));
		possibilities.set(max, outtemp);
		int index = min;
		for(int i = min; i<max; i++){
			if(possibilities.get(i).likelihood>=pivotValue){
				Possibility temp = possibilities.get(i);
				possibilities.set(i, possibilities.get(index));
				possibilities.set(index, temp);
				index++;
			}
		}
		Possibility temp = possibilities.get(index);
		possibilities.set(index, possibilities.get(max));
		possibilities.set(max, temp);
		return index;
	}
	
	private float likelihood(String soFar){
	
		String[] words = soFar.split(" ");
		float overall=1;
		for(int i=0; i<words.length-1; i++){
			overall *= dict.frequencyOfFollowingWord(words[i],words[i+1])/num;
		}
		return overall;
	}
	
	public class Possibility{
		String sentence;
		float likelihood;
		public Possibility(String s,float l)
		{
			sentence = s;
			likelihood = l;
		}
	}
}