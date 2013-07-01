package org.flowerplatform.web.git.common {
	import flash.net.registerClassAlias;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.git.common.remote.GitService;
	import org.flowerplatform.web.git.common.remote.OpenGitCredentialsWindowClientCommand;
	import org.flowerplatform.web.git.common.remote.dto.GitActionDto;
	import org.flowerplatform.web.git.common.remote.dto.GitRef;
	import org.flowerplatform.web.git.common.remote.dto.RemoteConfig;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class GitCommonPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:GitCommonPlugin;
						
		public static function getInstance():GitCommonPlugin {
			return INSTANCE;
		}		
		
		public static const NODE_TYPE_GIT_REPOSITORIES:String = "gitRepositories";
		
		public static const NODE_TYPE_REPOSITORY:String = "gitRepository";
		
		public static const NODE_TYPE_LOCAL_BRANCHES:String = "gitLocalBranches";
		public static const NODE_TYPE_REMOTE_BRANCHES:String = "gitRemoteBranches";
		public static const NODE_TYPE_TAGS:String = "gitTags";
		public static const NODE_TYPE_REMOTES:String = "gitRemotes";
		public static const NODE_TYPE_WDIRS:String = "gitWDirs";
		
		public static const NODE_TYPE_LOCAL_BRANCH:String = "gitLocalBranch";
		public static const NODE_TYPE_REMOTE_BRANCH:String = "gitRemoteBranch";
		public static const NODE_TYPE_TAG:String = "gitTag";
		
		public static const NODE_TYPE_REMOTE:String = "gitRemote";
		
		public static const NODE_TYPE_WDIR:String = "gitWDir";
		
		public static const NODE_TYPE_FILE:String = "gitFile";
		public static const NODE_TYPE_FOLDER:String = "gitFolder";
		
		public var service:GitService = new GitService();
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("Plugin " + Utils.getClassNameForObject(this, true) + " has already been started");
			}
			INSTANCE = this;			
		}
		
		override public function preStart():void {
			super.preStart();			
			WebCommonPlugin.getInstance().explorerTreeActionProviders.push(new GitExplorerTreeActionProvider());
		}
		
		override protected function registerClassAliases():void {
//			registerClassAliasFromAnnotation(CommitDto);
//			registerClassAliasFromAnnotation(CommitPageDto);
//			registerClassAliasFromAnnotation(CommitResourceDto);
//			registerClassAliasFromAnnotation(CreateBranchPageDto);
//			registerClassAliasFromAnnotation(ConfigFetchPushPageDto);
//			registerClassAliasFromAnnotation(ConfigTagPageDto);
			registerClassAlias("org.flowerplatform.web.git.remote.dto.GitRef", GitRef);
			registerClassAlias("org.flowerplatform.web.git.remote.dto.GitActionDto", GitActionDto);
			registerClassAlias("org.flowerplatform.web.git.remote.dto.RemoteConfig", RemoteConfig);
//			registerClassAliasFromAnnotation(RemoteConfig);
//			registerClassAliasFromAnnotation(ResetPageDto);
//			registerClassAliasFromAnnotation(ProjectDto);
//			registerClassAliasFromAnnotation(ImportProjectPageDto);			
			//			registerClassAliasFromAnnotation(GitAction);
//			registerClassAliasFromAnnotation(GitActionDto);	
//			registerClassAliasFromAnnotation(ConfigBranchPageDto);
//			registerClassAliasFromAnnotation(CleanPageDto);
//			registerClassAliasFromAnnotation(SharePageDto);
//			registerClassAliasFromAnnotation(RepositoryDto);
//			registerClassAliasFromAnnotation(ConfigPropertiesPageDto);
//			registerClassAliasFromAnnotation(ConfigEntryDto);
//			registerClassAliasFromAnnotation(ViewInfoDto);
			
//			registerClassAliasFromAnnotation(HistoryEntryDto);
//			registerClassAliasFromAnnotation(HistoryDrawingDto);
//			registerClassAliasFromAnnotation(HistoryFileDiffEntryDto);
//			registerClassAliasFromAnnotation(HistoryCommitMessageDto);
//			registerClassAliasFromAnnotation(HistoryViewInfoDto);
			
//			registerClassAliasFromAnnotation(GitHistoryStatefulClientLocalState);
//			registerClassAliasFromAnnotation(OpenOperationResultWindowClientCommand);		
			registerClassAlias("org.flowerplatform.web.git.remote.OpenGitCredentialsWindowClientCommand", OpenGitCredentialsWindowClientCommand);					
		}
	}	
}