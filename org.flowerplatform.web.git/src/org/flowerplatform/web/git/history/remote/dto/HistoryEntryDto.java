/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.web.git.history.remote.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *	@author Cristina Constantinescu
 */
public class HistoryEntryDto {

	private String id;
	
	private String shortId;
	
	private String message;
	
	private String author;
	
	private String authorEmail;
	
	private Date authoredDate;
	
	private String committer;
	
	private String committerEmail;
	
	private Date committeredDate;
	
	private List<HistoryDrawingDto> drawings;
	
	private String specialMessage;
		
	private List<HistoryFileDiffEntryDto> fileDiffs;
	
	private HistoryCommitMessageDto commitMessage;
	
	public String getShortId() {
		return shortId;
	}

	public void setShortId(String shortId) {
		this.shortId = shortId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getAuthoredDate() {
		return authoredDate;
	}

	public void setAuthoredDate(Date authoredDate) {
		this.authoredDate = authoredDate;
	}

	public String getCommitter() {
		return committer;
	}

	public void setCommitter(String committer) {
		this.committer = committer;
	}

	public Date getCommitteredDate() {
		return committeredDate;
	}

	public void setCommitteredDate(Date committeredDate) {
		this.committeredDate = committeredDate;
	}

	public List<HistoryDrawingDto> getDrawings() {
		return drawings;
	}

	public void setDrawings(List<HistoryDrawingDto> drawings) {
		this.drawings = drawings;
	}

	public String getSpecialMessage() {
		return specialMessage;
	}

	public void setSpecialMessage(String specialMessage) {
		this.specialMessage = specialMessage;
	}

	public List<HistoryFileDiffEntryDto> getFileDiffs() {
		if (fileDiffs == null) {
			fileDiffs = new ArrayList<HistoryFileDiffEntryDto>();
		}
		return fileDiffs;
	}

	public void setFileDiffs(List<HistoryFileDiffEntryDto> fileDiffs) {
		this.fileDiffs = fileDiffs;
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}

	public String getCommitterEmail() {
		return committerEmail;
	}

	public void setCommitterEmail(String committerEmail) {
		this.committerEmail = committerEmail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HistoryCommitMessageDto getCommitMessage() {
		if (commitMessage == null) {
			commitMessage = new HistoryCommitMessageDto();
		}
		return commitMessage;
	}

	public void setCommitMessage(HistoryCommitMessageDto commitMessage) {
		this.commitMessage = commitMessage;
	}
				
}