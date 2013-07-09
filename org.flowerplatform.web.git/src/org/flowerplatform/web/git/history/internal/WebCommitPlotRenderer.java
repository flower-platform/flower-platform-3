package org.flowerplatform.web.git.history.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.AbstractPlotRenderer;
import org.flowerplatform.web.git.history.remote.dto.HistoryDrawingDto;

/**
 *	@author Cristina Constantinescu
 */
public class WebCommitPlotRenderer extends AbstractPlotRenderer<WebLane, String> {

	private final String black = "#000000";	
	private final String gray = "#bebebe";
	private final String white = "#ffffff";
	private final String commitDotFill = "#DCDCDC";
	private final String commitDotOutline = "#6E6E6E";
	
	private WebCommit commit;
				
	private List<HistoryDrawingDto> drawings;
	
	private String specialMessage;
			
	public List<HistoryDrawingDto> getDrawings() {
		return drawings;
	}

	public String getSpecialMessage() {
		return specialMessage;
	}

	public WebCommitPlotRenderer(WebCommit commit) {		
		this.commit = commit;
		drawings = new ArrayList<HistoryDrawingDto>();
		specialMessage = "";
	}

	public void paint() {
		paintCommit(commit, 20);
	}
	
	@Override
	protected String laneColor(WebLane myLane) {
		return myLane != null ? myLane.color : black;
	}

	@Override
	protected void drawLine(String color, int x1, int y1, int x2, int y2, int width) {
		drawings.add(new HistoryDrawingDto(HistoryDrawingDto.DRAW_LINE, x1, y1, x2, y2, width, color));	
	}

	@Override
	protected void drawCommitDot(int x, int y, int w, int h) {
		drawings.add(new HistoryDrawingDto(HistoryDrawingDto.DRAW_DOT, x, y, w, h, commitDotOutline, commitDotFill));	
	}

	@Override
	protected void drawBoundaryDot(int x, int y, int w, int h) {
		drawings.add(new HistoryDrawingDto(HistoryDrawingDto.DRAW_DOT, x, y, w, h, gray, white));
	}

	@Override
	protected int drawLabel(int x, int y, Ref ref) {
		specialMessage += "[" + Repository.shortenRefName(ref.getName()) + "]";
		return 0;
	}
	
	@Override
	protected void drawText(String msg, int x, int y) {
	}
	
}
