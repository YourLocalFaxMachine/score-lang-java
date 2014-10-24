package api.scorelang;

import java.io.InputStream;

import org.scorelang.compiler.ScoreCompiler;
import org.scorelang.object.ScoreObject;
import org.scorelang.vm.ScoreVM;

public class Score {
	
	public static ScVM create(int initStackSize) {
		return new ScVM(new ScoreVM(initStackSize));
	}
	
	public static void close(ScVM v) {
	}
	
	public static void compile(ScVM v, InputStream input) {
		ScoreCompiler c = new ScoreCompiler(input);
		v._vm.push(c.compile());
	}
	
	public static int call(ScVM v, int nargs) {
		ScoreVM vm = v._vm;
		return vm.call(vm.get(-(nargs + 1)), nargs, vm.top() - nargs);
	}
	
}