
public class User {

  public static String getWiek(int wiek) {
  	if (wiek > 0) {
			int mod = wiek % 10;
			if (wiek < 5 || (wiek > 21 && mod > 1 && mod < 5)) {
				return "lata";
			}
		}
		return "lat";
	}

}
