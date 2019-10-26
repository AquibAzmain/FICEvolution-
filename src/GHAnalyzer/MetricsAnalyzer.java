package GHAnalyzer;

import java.util.List;

import FICMetrics.MetricsCalculator;
import GHObjects.GHCommit;
import GHObjects.GHFile;

public class MetricsAnalyzer {
	public void extractMetrics(List<GHCommit> commits) {
		for(GHCommit commit : commits) {
			if(!commit.shouldBeConsidered())
				continue;

			for(GHFile ghFile : commit.getChangedFiles()) {
				MetricsCalculator metricsCalculator = new MetricsCalculator();

				if(hasInvalidFile(ghFile, metricsCalculator)) {
					System.out.println("\t\tINVALID " + ghFile);
					continue;
				}

				List<Integer> cycs = metricsCalculator.countCylomaticComplexityForPreviousVersion(
						ghFile.getCurrentContent(),
						ghFile.getPreviousContent(),
						ghFile.getChangedLines());
				int loc = metricsCalculator.countLOC(ghFile.getPreviousContent(), ghFile.getChangedLines());

//				printMetrics(loc, cycs);

				ghFile.setCycs(cycs);
				ghFile.setLoc(loc);
			}
		}
	}

	private void printMetrics(int loc, List<Integer> cycs) {
		System.out.println("\t\t\tLoc: " + loc);

		System.out.print("\t\t\tCycs: ");
		for(Integer cyc : cycs){
			System.out.print(cyc + " ");
		}
		System.out.println();
	}

	private boolean hasInvalidFile(GHFile ghFile, MetricsCalculator metricsCalculator) {
		boolean currentInvalid = metricsCalculator.invalidFile(ghFile.getCurrentContent());
		boolean previousInvalid = metricsCalculator.invalidFile(ghFile.getPreviousContent());

		return currentInvalid || previousInvalid;
	}
}
