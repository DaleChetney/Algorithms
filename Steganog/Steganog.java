import java.awt.Color;
import java.io.IOException;
import edu.neumont.ui.Picture;

public class Steganog {

	public Picture embedIntoImage(Picture cleanImage,String message)throws IOException{
		cleanImage.setOriginUpperLeft();
		PrimeIterator primes = new PrimeIterator(cleanImage.height()*cleanImage.width());
		
		for (int i =0; i<message.length();i++)
		{
			int index = primes.next();
			Color cleanColor = cleanImage.get(index%cleanImage.width(), index/cleanImage.width());
			
			int letter = message.charAt(i)-32;
			int r = cleanColor.getRed();
			int g = cleanColor.getGreen();
			int b = cleanColor.getBlue();
			r = (((letter>>5)&1)==1)? r | (1<<1) : r & ~(1 << 1);
			r = (((letter>>4)&1)==1)? r | (1<<0) : r & ~(1 << 0);
			g = (((letter>>3)&1)==1)? g | (1<<1) : g & ~(1 << 1);
			g = (((letter>>2)&1)==1)? g | (1<<0) : g & ~(1 << 0);
			b = (((letter>>1)&1)==1)? b | (1<<1) : b & ~(1 << 1);
			b = (((letter>>0)&1)==1)? b | (1<<0) : b & ~(1 << 0);
			
			
			Color ModdedColor = new Color(r,g,b);
			cleanImage.set(index%cleanImage.width(), index/cleanImage.width(), ModdedColor);
		}
		return cleanImage;
	}
	
	public String retrieveFromImage(Picture imageWithSecretMessage)throws IOException{
		imageWithSecretMessage.setOriginUpperLeft();
		PrimeIterator primes = new PrimeIterator(imageWithSecretMessage.height()*imageWithSecretMessage.width());
		String message = "";
		int i =0;
		while(primes.hasNext())
		{
			i=primes.next();
			Color ModdedColor = imageWithSecretMessage.get(i%imageWithSecretMessage.width(), i/imageWithSecretMessage.width());
			
			int letter = 0;
			int r = ModdedColor.getRed();
			int g = ModdedColor.getGreen();
			int b = ModdedColor.getBlue();
			letter = (((r>>1)&1)==1)? letter | (1<<5) : letter & ~(1 << 5);
			letter = (((r>>0)&1)==1)? letter | (1<<4) : letter & ~(1 << 4);
			letter = (((g>>1)&1)==1)? letter | (1<<3) : letter & ~(1 << 3);
			letter = (((g>>0)&1)==1)? letter | (1<<2) : letter & ~(1 << 2);
			letter = (((b>>1)&1)==1)? letter | (1<<1) : letter & ~(1 << 1);
			letter = (((b>>0)&1)==1)? letter | (1<<0) : letter & ~(1 << 0);
			
			letter+=32;
			message += String.valueOf((char)letter);
		}
		
		return message;
	}
}