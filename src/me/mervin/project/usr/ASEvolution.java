package me.mervin.project.usr;

import me.mervin.core.Global.ExtractAS;
import me.mervin.core.Global.NumberType;
import me.mervin.module.extract.*;
import me.mervin.util.D;
import me.mervin.util.FileTool;


public class ASEvolution {
	public static void main(String[] args){
/*		int[] team={1, 2, 3};
		for (int i = 0; i < team.length; i++) {
			new ExtractASByLink(ExtractAS.ALL, "../data/AS-2013-3/team-"+team[i]+"/2013/03", "../data/AS-2013-3/T"+team[i]).run();
		}*/
		
		FileTool f = new FileTool();
/*		f.combine("../data/AS-2013-3/T1", "../data/AS-2013-3/T/as-t1-combine.txt", NumberType.INTEGER, 2);
		f.combine("../data/AS-2013-3/T2", "../data/AS-2013-3/T/as-t2-combine.txt", NumberType.INTEGER, 2);
		f.combine("../data/AS-2013-3/T3", "../data/AS-2013-3/T/as-t3-combine.txt", NumberType.INTEGER, 2);*/
		f.combine("../data/AS-2013-3/T", "../data/AS-2013-3/as-combine.txt", NumberType.INTEGER, 2);


	}
}
