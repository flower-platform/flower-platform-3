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
package org.flowerplatform.web.git;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jgit.lib.ProgressMonitor;

/**
 * @author Cristina Constantinescu
 */
public class GitProgressMonitor implements ProgressMonitor {
	private static final String EMPTY_STRING = "";  //$NON-NLS-1$

	private final IProgressMonitor root;

	private IProgressMonitor task;

	private String msg;

	private int lastWorked;

	private int totalWork;

	/**
	 * Create a new progress monitor.
	 *
	 * @param eclipseMonitor
	 *            the Eclipse monitor we update.
	 */
	public GitProgressMonitor(final IProgressMonitor eclipseMonitor) {
		root = eclipseMonitor;
	}

	public void start(final int totalTasks) {
		root.beginTask(EMPTY_STRING, totalTasks * 1000);
	}

	public void beginTask(final String name, final int total) {
		endTask();
		msg = name;
		lastWorked = 0;
		totalWork = total;
		task = new SubProgressMonitor(root, 1000);
		if (totalWork == UNKNOWN)
			task.beginTask(EMPTY_STRING, IProgressMonitor.UNKNOWN);
		else
			task.beginTask(EMPTY_STRING, totalWork);
		task.subTask(msg);
	}

	public void update(final int work) {
		if (task == null)
			return;

		final int cmp = lastWorked + work;
		if (totalWork == UNKNOWN && cmp > 0) {
			if (lastWorked != cmp)
				task.subTask(msg + ", " + cmp); //$NON-NLS-1$
		} else if (totalWork <= 0) {
			// Do nothing to update the task.
		} else if (cmp * 100 / totalWork != lastWorked * 100 / totalWork) {
			final StringBuilder m = new StringBuilder();
			m.append(msg);
			m.append(": ");  //$NON-NLS-1$
			while (m.length() < 25)
				m.append(' ');

			final String twstr = String.valueOf(totalWork);
			String cmpstr = String.valueOf(cmp);
			while (cmpstr.length() < twstr.length())
				cmpstr = " " + cmpstr; //$NON-NLS-1$
			final int pcnt = (cmp * 100 / totalWork);
			if (pcnt < 100)
				m.append(' ');
			if (pcnt < 10)
				m.append(' ');
			m.append(pcnt);
			m.append("% ("); //$NON-NLS-1$
			m.append(cmpstr);
			m.append("/"); //$NON-NLS-1$
			m.append(twstr);
			m.append(")"); //$NON-NLS-1$

			task.subTask(m.toString());
		}
		lastWorked = cmp;
		task.worked(work);
	}

	public void endTask() {
		if (task != null) {
			try {
				task.done();
			} finally {
				task = null;
			}
		}
	}

	public boolean isCancelled() {
		if (task != null)
			return task.isCanceled();
		return root.isCanceled();
	}
}