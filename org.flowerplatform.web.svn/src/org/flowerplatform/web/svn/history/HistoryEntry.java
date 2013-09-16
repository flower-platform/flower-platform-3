package org.flowerplatform.web.svn.history;


import java.util.Date;
import java.util.List;

import org.tigris.subversion.subclipse.core.history.ILogEntry;

/**
 * Holds data to be displayed as an entry in the SVN history table.
 * Corresponds to an {@link ILogEntry}.
 * 
 * @author Victor Badila
 */
public class HistoryEntry {
	
	private String revision;
	
	private Date date;
	
	private String author;
	
	private String comment;
	
	private List<AffectedPathEntry> affectedPathEntries;

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<AffectedPathEntry> getAffectedPathEntries() {
		return affectedPathEntries;
	}

	public void setAffectedPathEntries(List<AffectedPathEntry> affectedPathEntries) {
		this.affectedPathEntries = affectedPathEntries;
	}
	
}