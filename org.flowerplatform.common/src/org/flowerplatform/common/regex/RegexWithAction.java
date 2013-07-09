package org.flowerplatform.common.regex;

import java.util.regex.Pattern;

/**
 * @author Cristi
 */
public abstract class RegexWithAction {

	protected String humanReadableRegexMeaning;
	
	protected String regex;
	
	protected int numberOfCaptureGroups;
	
	public abstract void executeAction(RegexProcessingSession session);
	
	public RegexWithAction(String humanReadableRegexMeaning, String regex) {
		super();
		this.humanReadableRegexMeaning = humanReadableRegexMeaning;
		this.regex = regex;
		this.numberOfCaptureGroups = Pattern.compile(regex).matcher("").groupCount();
	}

	public static class IfFindThisSkip extends RegexWithAction {

		public IfFindThisSkip(String humanReadableRegexMeaning, String regex) {
			super(humanReadableRegexMeaning, regex);
		}

		@Override
		public void executeAction(RegexProcessingSession session) {
			// do nothing
		}
		
	}
	
	public static class IfFindThisAnnounceMatchCandidate extends RegexWithAction {

		protected String category;
		
		public IfFindThisAnnounceMatchCandidate(
				String humanReadableRegexMeaning, String regex, String category) {
			super(humanReadableRegexMeaning, regex);
			this.category = category;
		}

		@Override
		public void executeAction(RegexProcessingSession session) {
			if (!session.ignoreMatches && session.currentNestingLevel == session.configuration.targetNestingForMatches) {
				session.candidateAnnounced(category);
			}
		}
		
	}
	
	public static class IfFindThisModifyNesting extends RegexWithAction {

		protected int increment;
		
		public IfFindThisModifyNesting(String humanReadableRegexMeaning,
				String regex, int increment) {
			super(humanReadableRegexMeaning, regex);
			this.increment = increment;
		}

		@Override
		public void executeAction(RegexProcessingSession session) {
			session.currentNestingLevel += increment;
		}
		
	}
	
	public static class UntilFoundThisIgnoreAll extends RegexWithAction {

		public UntilFoundThisIgnoreAll(String humanReadableRegexMeaning,
				String regex) {
			super(humanReadableRegexMeaning, regex);
		}

		@Override
		public void executeAction(RegexProcessingSession session) {
			session.ignoreMatches = false;
		}
		
	}
	
}
