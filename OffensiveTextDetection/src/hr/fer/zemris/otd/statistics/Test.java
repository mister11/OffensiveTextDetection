package hr.fer.zemris.otd.statistics;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		String path1 = "C:/Users/Big Sven/Desktop/tempFER/oznacenoS";
		String path2 = "C:/Users/Big Sven/Desktop/tempFER/oznacenoA";
		String path3 = "C:/Users/Big Sven/Desktop/tempFER/oznacenoV";
		String path = "C:/Users/Big Sven/Desktop/experiment/lemma/new_dataset1.txt";
		CohensKappa c = new CohensKappa();
		LabelVectors lv = new LabelVectors();

//		c.makeVector(new File(path), lv);
//		System.out.println("Offensive-positive: \t\t"
//				+ lv.percentageOfOffensive("1"));
//		System.out.println("Offensive-negative: \t\t"
//				+ lv.percentageOfOffensive("0"));
//		System.out.println("Offensive-undecided: \t\t"
//				+ lv.percentageOfOffensive("x"));
//
//		System.out.println("Rude-positive: \t\t" + lv.percentageOfRude("1"));
//		System.out.println("Rude-negative: \t\t" + lv.percentageOfRude("0"));
//		System.out.println("Rude-undecided: \t\t" + lv.percentageOfRude("x"));
//
//		System.out.println("Sarcasm-positive: \t\t"
//				+ lv.percentageOfSarcasm("1"));
//		System.out.println("Sarcasm-negative: \t\t"
//				+ lv.percentageOfSarcasm("0"));
//		System.out.println("Sarcasm-undecided: \t\t"
//				+ lv.percentageOfSarcasm("x"));
		// c.countPosts(path1);
		// c.countPosts(path2);
		// c.countPosts(path3);

		LabelVectors first = new LabelVectors();
		LabelVectors second = new LabelVectors();
		LabelVectors third = new LabelVectors();
		c.makeCharVectors(path1, first);
		c.makeCharVectors(path2, second);
		c.makeCharVectors(path3, third);
		System.out.println();
		System.out.println("Sven off: " + first.percentageOfOffensive("0"));
		System.out.println("Sven rude: " + first.percentageOfRude("0"));
		System.out.println("Sven sarc: " + first.percentageOfSarcasm("0"));
		System.out.println("Sven target: " + first.percentageOfTarget("0"));
		System.out.println();
		System.out.println("Arijana off: " +
				second.percentageOfOffensive("0"));
		System.out.println("Arijana rude: " + second.percentageOfRude("0"));
		System.out.println("Arijana sarc: " +
				second.percentageOfSarcasm("0"));
		System.out.println("Arijana target: " +
				second.percentageOfTarget("0"));
		System.out.println();
		System.out.println("Vlaho off: " + third.percentageOfOffensive("0"));
		System.out.println("Vlaho rude: " + third.percentageOfRude("0"));
		System.out.println("Vlaho sarc: " + third.percentageOfSarcasm("0"));
		System.out.println("Vlaho target: " + third.percentageOfTarget("0"));
		System.out.println();

		int[] statsSAo = c.makeStatsTableNoX(third.offensive,
				second.offensive);
		int[] statsSAr = c.makeStatsTableNoX(third.rude, second.rude);
		int[] statsSAs = c.makeStatsTableNoX(third.sarcasm, second.sarcasm);
		int[] statsSAt = c.makeStatsTableNoX(third.target, second.target);
		int[] statsSAxo = c.makeStatsTable(third.offensive, second.offensive);
		int[] statsSAxr = c.makeStatsTable(third.rude, second.rude);
		int[] statsSAxs = c.makeStatsTable(third.sarcasm, second.sarcasm);
		int[] statsSAxt = c.makeStatsTable(third.target, second.target);
		System.out.println("Kappa score (offensive; no X) Arijana-Vlaho: "
				+ c.getScore(statsSAo));
		System.out.println("Kappa score (   rude  ; no X) Arijana-Vlaho: "
				+ c.getScore(statsSAr));
		System.out.println("Kappa score ( sarcasm ; no X) Arijana-Vlaho: "
				+ c.getScore(statsSAs));
		System.out.println("Kappa score (  target ; no X) Arijana-Vlaho: "
				+ c.getScore(statsSAt));
		System.out.println("Kappa score (offensive) Arijana-Vlaho: "
				+ c.getScore(statsSAxo));
		System.out.println("Kappa score (   rude  ) Arijana-Vlaho: "
				+ c.getScore(statsSAxr));
		System.out.println("Kappa score ( sarcasm ) Arijana-Vlaho: "
				+ c.getScore(statsSAxs));
		System.out.println("Kappa score (  target ) Arijana-Vlaho: "
				+ c.getScore(statsSAxt));

		// System.out.println(c.countPosts(new File("new_dataset1.txt")));

	}
}
