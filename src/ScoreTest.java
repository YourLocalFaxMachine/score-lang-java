import api.scorelang.*;

public class ScoreTest {
	
	public static void main(String[] args) {
		ScVM v = Score.create(10);
		
		Score.compile(v, ScoreTest.class.getResourceAsStream("/test.scor"));
		Score.call(v, 0);
		
		Score.close(v);
	}
	
}