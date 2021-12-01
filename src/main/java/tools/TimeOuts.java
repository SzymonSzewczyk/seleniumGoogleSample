package tools;

public class TimeOuts {

	public static final int TIMEOUT_1 = 1;
	public static final int TIMEOUT_5 = 5;
	public static final int TIMEOUT_10 = 10;
	public static final int TIMEOUT_30 = 30;
	public static final int TIMEOUT_60 = 60;

	public static final int TIMEOUT_100 = 100;
	public static final int TIMEOUT_200 = 200;
	public static final int TIMEOUT_500 = 500;
	public static final int TIMEOUT_1000 = 1000;

	//in milliseconds
	public static final int MS_1MIN = 60000;
	public static final int MS_2MIN = 120000;
	public static final int MS_5MIN = 300000;
	public static final int MS_10MIN = 600000;
	public static final int MS_DEFAULT_FOR_ELEMENT_APPEARS_VISIBLE = 100000; //10s

	//in seconds
	public static final int S_DEFAULT_FOR_ELEMENT_APPEARS_VISIBLE = MS_DEFAULT_FOR_ELEMENT_APPEARS_VISIBLE * 60;


	public long getInMilliseconds(int seconds) {
		return seconds * 1000L;
	}

}