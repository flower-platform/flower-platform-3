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
package org.flowerplatform.web.git {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.git.action.ShowHistoryAction;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.history.GitHistoryViewProvider;
	import org.flowerplatform.web.git.history.remote.GitHistoryStatefulClientLocalState;
	import org.flowerplatform.web.git.history.remote.dto.HistoryCommitMessageDto;
	import org.flowerplatform.web.git.history.remote.dto.HistoryDrawingDto;
	import org.flowerplatform.web.git.history.remote.dto.HistoryEntryDto;
	import org.flowerplatform.web.git.history.remote.dto.HistoryFileDiffEntryDto;
	import org.flowerplatform.web.git.history.remote.dto.HistoryViewInfoDto;
	import org.flowerplatform.web.git.layout.GitPerspective;
	import org.flowerplatform.web.git.remote.dto.CommitDto;
	import org.flowerplatform.web.git.remote.dto.ViewInfoDto;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class GitPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:GitPlugin;
			
		public static function getInstance():GitPlugin {
			return INSTANCE;
		}
		
		protected var gitCommonPlugin:GitCommonPlugin = new GitCommonPlugin();
		
		public var service:GitService = new GitService();
		
		override public function preStart():void {
			super.preStart();
			gitCommonPlugin.preStart();
			if (INSTANCE != null) {
				throw new Error("Plugin " + Utils.getClassNameForObject(this, true) + " has already been started");
			}
			INSTANCE = this;	
						
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new GitHistoryViewProvider());
			WebPlugin.getInstance().perspectives.push(new GitPerspective());	
			WebCommonPlugin.getInstance().explorerTreeClassFactoryActionProvider.actionClasses.push(ShowHistoryAction);
		}
		
		override public function start():void {
			super.start();
			gitCommonPlugin.flexPluginDescriptor = flexPluginDescriptor;	
			gitCommonPlugin.start();
		}	
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(CommitDto);	
			registerClassAliasFromAnnotation(ViewInfoDto);			
			registerClassAliasFromAnnotation(HistoryEntryDto);
			registerClassAliasFromAnnotation(HistoryDrawingDto);
			registerClassAliasFromAnnotation(HistoryFileDiffEntryDto);
			registerClassAliasFromAnnotation(HistoryCommitMessageDto);
			registerClassAliasFromAnnotation(HistoryViewInfoDto);			
			registerClassAliasFromAnnotation(GitHistoryStatefulClientLocalState);			
		}
		
	}
}