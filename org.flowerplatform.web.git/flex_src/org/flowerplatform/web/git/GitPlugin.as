package org.flowerplatform.web.git {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.dto.CleanPageDto;
	import org.flowerplatform.web.git.dto.CommitDto;
	import org.flowerplatform.web.git.dto.CommitPageDto;
	import org.flowerplatform.web.git.dto.CommitResourceDto;
	import org.flowerplatform.web.git.dto.ConfigBranchPageDto;
	import org.flowerplatform.web.git.dto.ConfigEntryDto;
	import org.flowerplatform.web.git.dto.ConfigFetchPushPageDto;
	import org.flowerplatform.web.git.dto.ConfigPropertiesPageDto;
	import org.flowerplatform.web.git.dto.ConfigTagPageDto;
	import org.flowerplatform.web.git.dto.CreateBranchPageDto;
	import org.flowerplatform.web.git.dto.GitActionDto;
	import org.flowerplatform.web.git.dto.GitRef;
	import org.flowerplatform.web.git.dto.ImportProjectPageDto;
	import org.flowerplatform.web.git.dto.ProjectDto;
	import org.flowerplatform.web.git.dto.RemoteConfig;
	import org.flowerplatform.web.git.dto.RepositoryDto;
	import org.flowerplatform.web.git.dto.ResetPageDto;
	import org.flowerplatform.web.git.dto.SharePageDto;
	import org.flowerplatform.web.git.dto.ViewInfoDto;
	import org.flowerplatform.web.git.history.GitHistoryViewProvider;
	import org.flowerplatform.web.git.history.communication.GitHistoryStatefulClientLocalState;
	import org.flowerplatform.web.git.history.dto.HistoryCommitMessageDto;
	import org.flowerplatform.web.git.history.dto.HistoryDrawingDto;
	import org.flowerplatform.web.git.history.dto.HistoryEntryDto;
	import org.flowerplatform.web.git.history.dto.HistoryFileDiffEntryDto;
	import org.flowerplatform.web.git.history.dto.HistoryViewInfoDto;
	import org.flowerplatform.web.git.layout.GitPerspective;
	
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
			
			WebCommonPlugin.getInstance().explorerTreeActionProviders.push(new GitExplorerTreeActionProvider());
		}
		
		override public function start():void {
			super.start();
			gitCommonPlugin.flexPluginDescriptor = flexPluginDescriptor;	
			gitCommonPlugin.start();
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(CommitDto);
			registerClassAliasFromAnnotation(CommitPageDto);
			registerClassAliasFromAnnotation(CommitResourceDto);
			registerClassAliasFromAnnotation(CreateBranchPageDto);
			registerClassAliasFromAnnotation(ConfigFetchPushPageDto);
			registerClassAliasFromAnnotation(ConfigTagPageDto);
			registerClassAliasFromAnnotation(GitRef);
			registerClassAliasFromAnnotation(RemoteConfig);
			registerClassAliasFromAnnotation(ResetPageDto);
			registerClassAliasFromAnnotation(ProjectDto);
			registerClassAliasFromAnnotation(ImportProjectPageDto);			
//			registerClassAliasFromAnnotation(GitAction);
			registerClassAliasFromAnnotation(GitActionDto);	
			registerClassAliasFromAnnotation(ConfigBranchPageDto);
			registerClassAliasFromAnnotation(CleanPageDto);
			registerClassAliasFromAnnotation(SharePageDto);
			registerClassAliasFromAnnotation(RepositoryDto);
			registerClassAliasFromAnnotation(ConfigPropertiesPageDto);
			registerClassAliasFromAnnotation(ConfigEntryDto);
			registerClassAliasFromAnnotation(ViewInfoDto);
			
			registerClassAliasFromAnnotation(HistoryEntryDto);
			registerClassAliasFromAnnotation(HistoryDrawingDto);
			registerClassAliasFromAnnotation(HistoryFileDiffEntryDto);
			registerClassAliasFromAnnotation(HistoryCommitMessageDto);
			registerClassAliasFromAnnotation(HistoryViewInfoDto);
			
//			registerClassAliasFromAnnotation(GitStagingStatefulClientLocalState);
//			registerClassAliasFromAnnotation(StagingViewInfoDto);
			
//			registerClassAliasFromAnnotation(GitReflogStatefulClientLocalState);
//			registerClassAliasFromAnnotation(ReflogViewInfoDto);
//			registerClassAliasFromAnnotation(ReflogEntryDto);
			
//			registerClassAliasFromAnnotation(OpenGitWindowClientCommand);
//			registerClassAliasFromAnnotation(OpenGitCredentialsWindowClientCommand);
//			registerClassAliasFromAnnotation(OpenOperationResultWindowClientCommand);
//			registerClassAliasFromAnnotation(ShowGitViewClientCommand);
			
			registerClassAliasFromAnnotation(GitHistoryStatefulClientLocalState);
		}
	}
}