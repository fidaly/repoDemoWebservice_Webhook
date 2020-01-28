package example;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class LambdaRequestHandler {

	static String gitMentionsJson="{\r\n" + 
			"  \"title\": \"Protected branch is created\",\r\n" + 
			"  \"body\": \"Protected branch is created. @awasam1 please call the issue\",\r\n" + 
			"  \"assignees\": [\r\n" + 
			"    \"awasam1\"\r\n" + 
			"  ],\r\n" + 
			"  \"labels\": [\r\n" + 
			"    \"info\"\r\n" + 
			"  ]\r\n" + 
			"}";
	 static String gitApiPayloadJson="{\r\n" + 
			"  \"required_status_checks\": {\r\n" + 
			"    \"strict\": true,\r\n" + 
			"    \"contexts\": [\r\n" + 
			"      \"continuous-integration/travis-ci\"\r\n" + 
			"    ]\r\n" + 
			"  },\r\n" + 
			"  \"enforce_admins\": true,\r\n" + 
			"  \"required_pull_request_reviews\": {\r\n" + 
			"    \"dismissal_restrictions\": {\r\n" + 
			"      \"users\": [\r\n" + 
			"        \"awasam1\"\r\n" + 
			"      ],\r\n" + 
			"      \"teams\": [\r\n" + 
			"        \"teamfidaly\"\r\n" + 
			"      ]\r\n" + 
			"    },\r\n" + 
			"    \"dismiss_stale_reviews\": false,\r\n" + 
			"    \"require_code_owner_reviews\": false,\r\n" + 
			"    \"required_approving_review_count\": 1\r\n" + 
			"  },\r\n" + 
			"  \"restrictions\": {\r\n" + 
			"    \"users\": [\r\n" + 
			"      \"awasam1\"\r\n" + 
			"    ],\r\n" + 
			"    \"teams\": [\r\n" + 
			"      \"teamfidaly\"\r\n" + 
			"    ]\r\n" + 
			"  }\r\n" + 
			"}";
	 
	static String webhookResponseJson1 ="{\r\n" + 
	 		"  \"ref\": \"master\",\r\n" + 
	 		"  \"ref_type\": \"branch\",\r\n" + 
	 		"  \"master_branch\": \"master\",\r\n" + 
	 		"  \"description\": null,\r\n" + 
	 		"  \"pusher_type\": \"user\",\r\n" + 
	 		"  \"repository\": {\r\n" + 
	 		"    \"id\": 236177267,\r\n" + 
	 		"    \"node_id\": \"MDEwOlJlcG9zaXRvcnkyMzYxNzcyNjc=\",\r\n" + 
	 		"    \"name\": \"bijirepo\",\r\n" + 
	 		"    \"full_name\": \"bijiac/bijirepo\",\r\n" + 
	 		"    \"private\": false,\r\n" + 
	 		"    \"owner\": {\r\n" + 
	 		"      \"login\": \"bijiac\",\r\n" + 
	 		"      \"id\": 60260704,\r\n" + 
	 		"      \"node_id\": \"MDEyOk9yZ2FuaXphdGlvbjYwMjYwNzA0\",\r\n" + 
	 		"      \"avatar_url\": \"https://avatars1.githubusercontent.com/u/60260704?v=4(2 kB)\r\n" + 
	 		"https://avatars1.githubusercontent.com/u/60260704?v=4\r\n" + 
	 		"\",\r\n" + 
	 		"      \"gravatar_id\": \"\",\r\n" + 
	 		"      \"url\": \"https://api.github.com/users/bijiac\",\r\n" + 
	 		"      \"html_url\": \"https://github.com/bijiac\",\r\n" + 
	 		"      \"followers_url\": \"https://api.github.com/users/bijiac/followers\",\r\n" + 
	 		"      \"following_url\": \"https://api.github.com/users/bijiac/following{/other_user}\",\r\n" + 
	 		"      \"gists_url\": \"https://api.github.com/users/bijiac/gists{/gist_id}\",\r\n" + 
	 		"      \"starred_url\": \"https://api.github.com/users/bijiac/starred{/owner}{/repo}\",\r\n" + 
	 		"      \"subscriptions_url\": \"https://api.github.com/users/bijiac/subscriptions\",\r\n" + 
	 		"      \"organizations_url\": \"https://api.github.com/users/bijiac/orgs\",\r\n" + 
	 		"      \"repos_url\": \"https://api.github.com/users/bijiac/repos\",\r\n" + 
	 		"      \"events_url\": \"https://api.github.com/users/bijiac/events{/privacy}\",\r\n" + 
	 		"      \"received_events_url\": \"https://api.github.com/users/bijiac/received_events\",\r\n" + 
	 		"      \"type\": \"Organization\",\r\n" + 
	 		"      \"site_admin\": false\r\n" + 
	 		"    },\r\n" + 
	 		"    \"html_url\": \"https://github.com/bijiac/bijirepo\",\r\n" + 
	 		"    \"description\": null,\r\n" + 
	 		"    \"fork\": false,\r\n" + 
	 		"    \"url\": \"https://api.github.com/repos/bijiac/bijirepo\",\r\n" + 
	 		"    \"forks_url\": \"https://api.github.com/repos/bijiac/bijirepo/forks\",\r\n" + 
	 		"    \"keys_url\": \"https://api.github.com/repos/bijiac/bijirepo/keys{/key_id}\",\r\n" + 
	 		"    \"collaborators_url\": \"https://api.github.com/repos/bijiac/bijirepo/collaborators{/collaborator}\",\r\n" + 
	 		"    \"teams_url\": \"https://api.github.com/repos/bijiac/bijirepo/teams\",\r\n" + 
	 		"    \"hooks_url\": \"https://api.github.com/repos/bijiac/bijirepo/hooks\",\r\n" + 
	 		"    \"issue_events_url\": \"https://api.github.com/repos/bijiac/bijirepo/issues/events{/number}\",\r\n" + 
	 		"    \"events_url\": \"https://api.github.com/repos/bijiac/bijirepo/events\",\r\n" + 
	 		"    \"assignees_url\": \"https://api.github.com/repos/bijiac/bijirepo/assignees{/user}\",\r\n" + 
	 		"    \"branches_url\": \"https://api.github.com/repos/bijiac/bijirepo/branches{/branch}\",\r\n" + 
	 		"    \"tags_url\": \"https://api.github.com/repos/bijiac/bijirepo/tags\",\r\n" + 
	 		"    \"blobs_url\": \"https://api.github.com/repos/bijiac/bijirepo/git/blobs{/sha}\",\r\n" + 
	 		"    \"git_tags_url\": \"https://api.github.com/repos/bijiac/bijirepo/git/tags{/sha}\",\r\n" + 
	 		"    \"git_refs_url\": \"https://api.github.com/repos/bijiac/bijirepo/git/refs{/sha}\",\r\n" + 
	 		"    \"trees_url\": \"https://api.github.com/repos/bijiac/bijirepo/git/trees{/sha}\",\r\n" + 
	 		"    \"statuses_url\": \"https://api.github.com/repos/bijiac/bijirepo/statuses/{sha}\",\r\n" + 
	 		"    \"languages_url\": \"https://api.github.com/repos/bijiac/bijirepo/languages\",\r\n" + 
	 		"    \"stargazers_url\": \"https://api.github.com/repos/bijiac/bijirepo/stargazers\",\r\n" + 
	 		"    \"contributors_url\": \"https://api.github.com/repos/bijiac/bijirepo/contributors\",\r\n" + 
	 		"    \"subscribers_url\": \"https://api.github.com/repos/bijiac/bijirepo/subscribers\",\r\n" + 
	 		"    \"subscription_url\": \"https://api.github.com/repos/bijiac/bijirepo/subscription\",\r\n" + 
	 		"    \"commits_url\": \"https://api.github.com/repos/bijiac/bijirepo/commits{/sha}\",\r\n" + 
	 		"    \"git_commits_url\": \"https://api.github.com/repos/bijiac/bijirepo/git/commits{/sha}\",\r\n" + 
	 		"    \"comments_url\": \"https://api.github.com/repos/bijiac/bijirepo/comments{/number}\",\r\n" + 
	 		"    \"issue_comment_url\": \"https://api.github.com/repos/bijiac/bijirepo/issues/comments{/number}\",\r\n" + 
	 		"    \"contents_url\": \"https://api.github.com/repos/bijiac/bijirepo/contents/{+path}\",\r\n" + 
	 		"    \"compare_url\": \"https://api.github.com/repos/bijiac/bijirepo/compare/{base}...{head}\",\r\n" + 
	 		"    \"merges_url\": \"https://api.github.com/repos/bijiac/bijirepo/merges\",\r\n" + 
	 		"    \"archive_url\": \"https://api.github.com/repos/bijiac/bijirepo/{archive_format}{/ref}\",\r\n" + 
	 		"    \"downloads_url\": \"https://api.github.com/repos/bijiac/bijirepo/downloads\",\r\n" + 
	 		"    \"issues_url\": \"https://api.github.com/repos/bijiac/bijirepo/issues{/number}\",\r\n" + 
	 		"    \"pulls_url\": \"https://api.github.com/repos/bijiac/bijirepo/pulls{/number}\",\r\n" + 
	 		"    \"milestones_url\": \"https://api.github.com/repos/bijiac/bijirepo/milestones{/number}\",\r\n" + 
	 		"    \"notifications_url\": \"https://api.github.com/repos/bijiac/bijirepo/notifications{?since,all,participating}\",\r\n" + 
	 		"    \"labels_url\": \"https://api.github.com/repos/bijiac/bijirepo/labels{/name}\",\r\n" + 
	 		"    \"releases_url\": \"https://api.github.com/repos/bijiac/bijirepo/releases{/id}\",\r\n" + 
	 		"    \"deployments_url\": \"https://api.github.com/repos/bijiac/bijirepo/deployments\",\r\n" + 
	 		"    \"created_at\": \"2020-01-25T13:59:31Z\",\r\n" + 
	 		"    \"updated_at\": \"2020-01-25T13:59:35Z\",\r\n" + 
	 		"    \"pushed_at\": \"2020-01-27T04:39:58Z\",\r\n" + 
	 		"    \"git_url\": \"git://github.com/bijiac/bijirepo.git\",\r\n" + 
	 		"    \"ssh_url\": \"git@github.com:bijiac/bijirepo.git\",\r\n" + 
	 		"    \"clone_url\": \"https://github.com/bijiac/bijirepo.git\",\r\n" + 
	 		"    \"svn_url\": \"https://github.com/bijiac/bijirepo\",\r\n" + 
	 		"    \"homepage\": null,\r\n" + 
	 		"    \"size\": 0,\r\n" + 
	 		"    \"stargazers_count\": 0,\r\n" + 
	 		"    \"watchers_count\": 0,\r\n" + 
	 		"    \"language\": null,\r\n" + 
	 		"    \"has_issues\": true,\r\n" + 
	 		"    \"has_projects\": true,\r\n" + 
	 		"    \"has_downloads\": true,\r\n" + 
	 		"    \"has_wiki\": true,\r\n" + 
	 		"    \"has_pages\": false,\r\n" + 
	 		"    \"forks_count\": 0,\r\n" + 
	 		"    \"mirror_url\": null,\r\n" + 
	 		"    \"archived\": false,\r\n" + 
	 		"    \"disabled\": false,\r\n" + 
	 		"    \"open_issues_count\": 0,\r\n" + 
	 		"    \"license\": null,\r\n" + 
	 		"    \"forks\": 0,\r\n" + 
	 		"    \"open_issues\": 0,\r\n" + 
	 		"    \"watchers\": 0,\r\n" + 
	 		"    \"default_branch\": \"master\"\r\n" + 
	 		"  },\r\n" + 
	 		"  \"organization\": {\r\n" + 
	 		"    \"login\": \"bijiac\",\r\n" + 
	 		"    \"id\": 60260704,\r\n" + 
	 		"    \"node_id\": \"MDEyOk9yZ2FuaXphdGlvbjYwMjYwNzA0\",\r\n" + 
	 		"    \"url\": \"https://api.github.com/orgs/bijiac\",\r\n" + 
	 		"    \"repos_url\": \"https://api.github.com/orgs/bijiac/repos\",\r\n" + 
	 		"    \"events_url\": \"https://api.github.com/orgs/bijiac/events\",\r\n" + 
	 		"    \"hooks_url\": \"https://api.github.com/orgs/bijiac/hooks\",\r\n" + 
	 		"    \"issues_url\": \"https://api.github.com/orgs/bijiac/issues\",\r\n" + 
	 		"    \"members_url\": \"https://api.github.com/orgs/bijiac/members{/member}\",\r\n" + 
	 		"    \"public_members_url\": \"https://api.github.com/orgs/bijiac/public_members{/member}\",\r\n" + 
	 		"    \"avatar_url\": \"https://avatars1.githubusercontent.com/u/60260704?v=4(2 kB)\r\n" + 
	 		"https://avatars1.githubusercontent.com/u/60260704?v=4\r\n" + 
	 		"\",\r\n" + 
	 		"    \"description\": null\r\n" + 
	 		"  },\r\n" + 
	 		"  \"sender\": {\r\n" + 
	 		"    \"login\": \"mailtobijigmail\",\r\n" + 
	 		"    \"id\": 60260241,\r\n" + 
	 		"    \"node_id\": \"MDQ6VXNlcjYwMjYwMjQx\",\r\n" + 
	 		"    \"avatar_url\": \"https://avatars0.githubusercontent.com/u/60260241?v=4(2 kB)\r\n" + 
	 		"https://avatars0.githubusercontent.com/u/60260241?v=4\r\n" + 
	 		"\",\r\n" + 
	 		"    \"gravatar_id\": \"\",\r\n" + 
	 		"    \"url\": \"https://api.github.com/users/mailtobijigmail\",\r\n" + 
	 		"    \"html_url\": \"https://github.com/mailtobijigmail\",\r\n" + 
	 		"    \"followers_url\": \"https://api.github.com/users/mailtobijigmail/followers\",\r\n" + 
	 		"    \"following_url\": \"https://api.github.com/users/mailtobijigmail/following{/other_user}\",\r\n" + 
	 		"    \"gists_url\": \"https://api.github.com/users/mailtobijigmail/gists{/gist_id}\",\r\n" + 
	 		"    \"starred_url\": \"https://api.github.com/users/mailtobijigmail/starred{/owner}{/repo}\",\r\n" + 
	 		"    \"subscriptions_url\": \"https://api.github.com/users/mailtobijigmail/subscriptions\",\r\n" + 
	 		"    \"organizations_url\": \"https://api.github.com/users/mailtobijigmail/orgs\",\r\n" + 
	 		"    \"repos_url\": \"https://api.github.com/users/mailtobijigmail/repos\",\r\n" + 
	 		"    \"events_url\": \"https://api.github.com/users/mailtobijigmail/events{/privacy}\",\r\n" + 
	 		"    \"received_events_url\": \"https://api.github.com/users/mailtobijigmail/received_events\",\r\n" + 
	 		"    \"type\": \"User\",\r\n" + 
	 		"    \"site_admin\": false\r\n" + 
	 		"  }\r\n" + 
	 		"}";
	
   static String redpoCreationJson1 ="{\r\n" + 
   		"  \"action\": \"created\",\r\n" + 
   		"  \"repository\": {\r\n" + 
   		"    \"id\": 236440049,\r\n" + 
   		"    \"node_id\": \"MDEwOlJlcG9zaXRvcnkyMzY0NDAwNDk=\",\r\n" + 
   		"    \"name\": \"btrr\",\r\n" + 
   		"    \"full_name\": \"bijiac/branchrepo\",\r\n" + 
   		"    \"private\": false,\r\n" + 
   		"    \"owner\": {\r\n" + 
   		"      \"login\": \"bijiac\",\r\n" + 
   		"      \"id\": 60260704,\r\n" + 
   		"      \"node_id\": \"MDEyOk9yZ2FuaXphdGlvbjYwMjYwNzA0\",\r\n" + 
   		"      \"avatar_url\": \"https://avatars1.githubusercontent.com/u/60260704?v=4\",\r\n" + 
   		"      \"gravatar_id\": \"\",\r\n" + 
   		"      \"url\": \"https://api.github.com/users/bijiac\",\r\n" + 
   		"      \"html_url\": \"https://github.com/bijiac\",\r\n" + 
   		"      \"followers_url\": \"https://api.github.com/users/bijiac/followers\",\r\n" + 
   		"      \"following_url\": \"https://api.github.com/users/bijiac/following{/other_user}\",\r\n" + 
   		"      \"gists_url\": \"https://api.github.com/users/bijiac/gists{/gist_id}\",\r\n" + 
   		"      \"starred_url\": \"https://api.github.com/users/bijiac/starred{/owner}{/repo}\",\r\n" + 
   		"      \"subscriptions_url\": \"https://api.github.com/users/bijiac/subscriptions\",\r\n" + 
   		"      \"organizations_url\": \"https://api.github.com/users/bijiac/orgs\",\r\n" + 
   		"      \"repos_url\": \"https://api.github.com/users/bijiac/repos\",\r\n" + 
   		"      \"events_url\": \"https://api.github.com/users/bijiac/events{/privacy}\",\r\n" + 
   		"      \"received_events_url\": \"https://api.github.com/users/bijiac/received_events\",\r\n" + 
   		"      \"type\": \"Organization\",\r\n" + 
   		"      \"site_admin\": false\r\n" + 
   		"    },\r\n" + 
   		"    \"html_url\": \"https://github.com/bijiac/branchrepo\",\r\n" + 
   		"    \"description\": null,\r\n" + 
   		"    \"fork\": false,\r\n" + 
   		"    \"url\": \"https://api.github.com/repos/bijiac/branchrepo\",\r\n" + 
   		"    \"forks_url\": \"https://api.github.com/repos/bijiac/branchrepo/forks\",\r\n" + 
   		"    \"keys_url\": \"https://api.github.com/repos/bijiac/branchrepo/keys{/key_id}\",\r\n" + 
   		"    \"collaborators_url\": \"https://api.github.com/repos/bijiac/branchrepo/collaborators{/collaborator}\",\r\n" + 
   		"    \"teams_url\": \"https://api.github.com/repos/bijiac/branchrepo/teams\",\r\n" + 
   		"    \"hooks_url\": \"https://api.github.com/repos/bijiac/branchrepo/hooks\",\r\n" + 
   		"    \"issue_events_url\": \"https://api.github.com/repos/bijiac/branchrepo/issues/events{/number}\",\r\n" + 
   		"    \"events_url\": \"https://api.github.com/repos/bijiac/branchrepo/events\",\r\n" + 
   		"    \"assignees_url\": \"https://api.github.com/repos/bijiac/branchrepo/assignees{/user}\",\r\n" + 
   		"    \"branches_url\": \"https://api.github.com/repos/bijiac/btrr/branches{/branch}\",\r\n" + 
   		"    \"tags_url\": \"https://api.github.com/repos/bijiac/branchrepo/tags\",\r\n" + 
   		"    \"blobs_url\": \"https://api.github.com/repos/bijiac/branchrepo/git/blobs{/sha}\",\r\n" + 
   		"    \"git_tags_url\": \"https://api.github.com/repos/bijiac/branchrepo/git/tags{/sha}\",\r\n" + 
   		"    \"git_refs_url\": \"https://api.github.com/repos/bijiac/branchrepo/git/refs{/sha}\",\r\n" + 
   		"    \"trees_url\": \"https://api.github.com/repos/bijiac/branchrepo/git/trees{/sha}\",\r\n" + 
   		"    \"statuses_url\": \"https://api.github.com/repos/bijiac/branchrepo/statuses/{sha}\",\r\n" + 
   		"    \"languages_url\": \"https://api.github.com/repos/bijiac/branchrepo/languages\",\r\n" + 
   		"    \"stargazers_url\": \"https://api.github.com/repos/bijiac/branchrepo/stargazers\",\r\n" + 
   		"    \"contributors_url\": \"https://api.github.com/repos/bijiac/branchrepo/contributors\",\r\n" + 
   		"    \"subscribers_url\": \"https://api.github.com/repos/bijiac/branchrepo/subscribers\",\r\n" + 
   		"    \"subscription_url\": \"https://api.github.com/repos/bijiac/branchrepo/subscription\",\r\n" + 
   		"    \"commits_url\": \"https://api.github.com/repos/bijiac/branchrepo/commits{/sha}\",\r\n" + 
   		"    \"git_commits_url\": \"https://api.github.com/repos/bijiac/branchrepo/git/commits{/sha}\",\r\n" + 
   		"    \"comments_url\": \"https://api.github.com/repos/bijiac/branchrepo/comments{/number}\",\r\n" + 
   		"    \"issue_comment_url\": \"https://api.github.com/repos/bijiac/branchrepo/issues/comments{/number}\",\r\n" + 
   		"    \"contents_url\": \"https://api.github.com/repos/bijiac/branchrepo/contents/{+path}\",\r\n" + 
   		"    \"compare_url\": \"https://api.github.com/repos/bijiac/branchrepo/compare/{base}...{head}\",\r\n" + 
   		"    \"merges_url\": \"https://api.github.com/repos/bijiac/branchrepo/merges\",\r\n" + 
   		"    \"archive_url\": \"https://api.github.com/repos/bijiac/branchrepo/{archive_format}{/ref}\",\r\n" + 
   		"    \"downloads_url\": \"https://api.github.com/repos/bijiac/branchrepo/downloads\",\r\n" + 
   		"    \"issues_url\": \"https://api.github.com/repos/bijiac/branchrepo/issues{/number}\",\r\n" + 
   		"    \"pulls_url\": \"https://api.github.com/repos/bijiac/branchrepo/pulls{/number}\",\r\n" + 
   		"    \"milestones_url\": \"https://api.github.com/repos/bijiac/branchrepo/milestones{/number}\",\r\n" + 
   		"    \"notifications_url\": \"https://api.github.com/repos/bijiac/branchrepo/notifications{?since,all,participating}\",\r\n" + 
   		"    \"labels_url\": \"https://api.github.com/repos/bijiac/branchrepo/labels{/name}\",\r\n" + 
   		"    \"releases_url\": \"https://api.github.com/repos/bijiac/branchrepo/releases{/id}\",\r\n" + 
   		"    \"deployments_url\": \"https://api.github.com/repos/bijiac/branchrepo/deployments\",\r\n" + 
   		"    \"created_at\": \"2020-01-27T07:35:13Z\",\r\n" + 
   		"    \"updated_at\": \"2020-01-27T07:35:13Z\",\r\n" + 
   		"    \"pushed_at\": null,\r\n" + 
   		"    \"git_url\": \"git://github.com/bijiac/branchrepo.git\",\r\n" + 
   		"    \"ssh_url\": \"git@github.com:bijiac/branchrepo.git\",\r\n" + 
   		"    \"clone_url\": \"https://github.com/bijiac/branchrepo.git\",\r\n" + 
   		"    \"svn_url\": \"https://github.com/bijiac/branchrepo\",\r\n" + 
   		"    \"homepage\": null,\r\n" + 
   		"    \"size\": 0,\r\n" + 
   		"    \"stargazers_count\": 0,\r\n" + 
   		"    \"watchers_count\": 0,\r\n" + 
   		"    \"language\": null,\r\n" + 
   		"    \"has_issues\": true,\r\n" + 
   		"    \"has_projects\": true,\r\n" + 
   		"    \"has_downloads\": true,\r\n" + 
   		"    \"has_wiki\": true,\r\n" + 
   		"    \"has_pages\": false,\r\n" + 
   		"    \"forks_count\": 0,\r\n" + 
   		"    \"mirror_url\": null,\r\n" + 
   		"    \"archived\": false,\r\n" + 
   		"    \"disabled\": false,\r\n" + 
   		"    \"open_issues_count\": 0,\r\n" + 
   		"    \"license\": null,\r\n" + 
   		"    \"forks\": 0,\r\n" + 
   		"    \"open_issues\": 0,\r\n" + 
   		"    \"watchers\": 0,\r\n" + 
   		"    \"default_branch\": \"master\"\r\n" + 
   		"  },\r\n" + 
   		"  \"organization\": {\r\n" + 
   		"    \"login\": \"bijiac\",\r\n" + 
   		"    \"id\": 60260704,\r\n" + 
   		"    \"node_id\": \"MDEyOk9yZ2FuaXphdGlvbjYwMjYwNzA0\",\r\n" + 
   		"    \"url\": \"https://api.github.com/orgs/bijiac\",\r\n" + 
   		"    \"repos_url\": \"https://api.github.com/orgs/bijiac/repos\",\r\n" + 
   		"    \"events_url\": \"https://api.github.com/orgs/bijiac/events\",\r\n" + 
   		"    \"hooks_url\": \"https://api.github.com/orgs/bijiac/hooks\",\r\n" + 
   		"    \"issues_url\": \"https://api.github.com/orgs/bijiac/issues\",\r\n" + 
   		"    \"members_url\": \"https://api.github.com/orgs/bijiac/members{/member}\",\r\n" + 
   		"    \"public_members_url\": \"https://api.github.com/orgs/bijiac/public_members{/member}\",\r\n" + 
   		"    \"avatar_url\": \"https://avatars1.githubusercontent.com/u/60260704?v=4\",\r\n" + 
   		"    \"description\": null\r\n" + 
   		"  },\r\n" + 
   		"  \"sender\": {\r\n" + 
   		"    \"login\": \"mailtobijigmail\",\r\n" + 
   		"    \"id\": 60260241,\r\n" + 
   		"    \"node_id\": \"MDQ6VXNlcjYwMjYwMjQx\",\r\n" + 
   		"    \"avatar_url\": \"https://avatars0.githubusercontent.com/u/60260241?v=4\",\r\n" + 
   		"    \"gravatar_id\": \"\",\r\n" + 
   		"    \"url\": \"https://api.github.com/users/mailtobijigmail\",\r\n" + 
   		"    \"html_url\": \"https://github.com/mailtobijigmail\",\r\n" + 
   		"    \"followers_url\": \"https://api.github.com/users/mailtobijigmail/followers\",\r\n" + 
   		"    \"following_url\": \"https://api.github.com/users/mailtobijigmail/following{/other_user}\",\r\n" + 
   		"    \"gists_url\": \"https://api.github.com/users/mailtobijigmail/gists{/gist_id}\",\r\n" + 
   		"    \"starred_url\": \"https://api.github.com/users/mailtobijigmail/starred{/owner}{/repo}\",\r\n" + 
   		"    \"subscriptions_url\": \"https://api.github.com/users/mailtobijigmail/subscriptions\",\r\n" + 
   		"    \"organizations_url\": \"https://api.github.com/users/mailtobijigmail/orgs\",\r\n" + 
   		"    \"repos_url\": \"https://api.github.com/users/mailtobijigmail/repos\",\r\n" + 
   		"    \"events_url\": \"https://api.github.com/users/mailtobijigmail/events{/privacy}\",\r\n" + 
   		"    \"received_events_url\": \"https://api.github.com/users/mailtobijigmail/received_events\",\r\n" + 
   		"    \"type\": \"User\",\r\n" + 
   		"    \"site_admin\": false\r\n" + 
   		"  }\r\n" + 
   		"}";
	 		
	public static void main(String args[]) throws ClientProtocolException, IOException
	{
		
		/*HttpClient client = new DefaultHttpClient();
		 HttpGet request = new HttpGet("https://api.github.com/repos/bijiac/bijirepo/branches/dev");
		  HttpResponse response = client.execute(request);
		  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  while ((line = rd.readLine()) != null) {
		    System.out.println(line);
		  }
		 StringEntity entity = new StringEntity(webhookJson, "UTF-8");
		 HttpPut httpPut = new HttpPut("https://api.github.com/repos/bijiac/bijirepo/branches/dev/protection");
		 httpPut.addHeader("content-type", "application/json");
		 httpPut.addHeader("Accept", "application/vnd.github.luke-cage-preview+json");
		 httpPut.addHeader("Authorization", "token ca4b993e93ea1aa199b5322ce254fc0dd7a631cb");
		 httpPut.setEntity(entity);
		 HttpResponse response = client.execute(httpPut);
		  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  while ((line = rd.readLine()) != null) {
		    System.out.println(line);
		  }*/
		 
				
		
		protectBranch(redpoCreationJson1);
	}
   public String myHandler(InputStream stream, Context context)  throws Exception          
   {
	   System.out.println("called myHandler GITTTTT....");    
	  
	 
      BufferedInputStream bis = new BufferedInputStream(stream);
      int length=bis.available();
      System.out.println("Data Length:"+length);
      byte[] bArray=new byte[length];
      bis.read(bArray);
      String webhookResponseJson = new String(bArray);
      
      System.out.println("Data................................. START");
      System.out.println(new String(bArray));
      System.out.println("Data................................. END");
      protectBranch(webhookResponseJson);
      return "test";
     
      
   }
   
   public static String mentions(String webhookResponseJson, String url)
   {
	 
	   HttpClient client = new DefaultHttpClient();
	   JsonObject convertedObject = new Gson().fromJson(webhookResponseJson, JsonObject.class);
	
	   
	   
	   
	try {
		//Thread.sleep(30000);
		StringEntity entity = new StringEntity(gitMentionsJson, "UTF-8");
		 //HttpPut httpPutIssue = new HttpPut("https://api.github.com/repos/bijiac/bijirepo/issues");
		 HttpPost httpPostIssue = new HttpPost(url);
		 System.out.println("IssueURL:"+url);
		 httpPostIssue.addHeader("content-type", "application/json");
		 httpPostIssue.addHeader("Accept", "application/vnd.github.luke-cage-preview+json");
		 httpPostIssue.addHeader("Authorization", "token f13342fd28cd3fb086322c1fb2bedec00bdd4d85");
		 httpPostIssue.setEntity(entity);
		 
		 HttpResponse response = client.execute(httpPostIssue);
		 System.out.println("Executed.......");
		  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  while ((line = rd.readLine()) != null) {
			  System.out.println("HELLO");  
		    System.out.println(line);
		  }
		  
		  rd.close();
		  rd=null;
		  httpPostIssue =null;
		  entity=null;
		 
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   return null;
   }
   
   public static String protectBranch(String webhookResponseJson)
   {
	   URL url;
	   HttpClient client = new DefaultHttpClient();
	   JsonObject convertedObject = new Gson().fromJson(webhookResponseJson, JsonObject.class);
	  /* String branchName = convertedObject.get("ref").toString();
	   * FOR BRANCH PROTECTION FOR ALL BRANCHES ALL REPOS
	   branchName = branchName.replace("\"","");
	   
	   System.out.println("branchName IS"+branchName);
	   JsonObject repoObj=(JsonObject)convertedObject.get("repository");
	   String branchUrl=repoObj.get("branches_url").toString();
	   branchUrl=branchUrl.substring(0, branchUrl.length()-10);
	   //branchUrl.replaceFirst("{/branch}", "/"+branchName);
	   branchUrl =branchUrl+"/"+branchName+"/protection";
	   branchUrl = branchUrl.replace("\"","");
	   System.out.println("branchURL IS"+branchUrl);*/
	   
	   String action = convertedObject.get("action").toString();
	   JsonObject repoObj=(JsonObject)convertedObject.get("repository");
	   
	   String default_branch=repoObj.get("default_branch").toString();
	   String gitUrl=repoObj.get("issues_url").toString();
	   gitUrl=gitUrl.substring(0, gitUrl.length()-10);
	   gitUrl = gitUrl.replace("\"","");	  
	   action = action.replace("\"","");
	   default_branch = default_branch.replace("\"","");
		   
		System.out.println("action IS"+action);
	
		if(action.equals("created"))
		{
		   String branchUrl=repoObj.get("branches_url").toString();
		   branchUrl=branchUrl.substring(0, branchUrl.length()-10);
		   branchUrl = branchUrl.replace("\"","");
	       String issueUrl=branchUrl;
		   branchUrl =branchUrl+"/master/protection";
		   
		   branchUrl =branchUrl.trim();
		   System.out.println("branchURL IS"+branchUrl);
	   
	   
	   
	   
	try {
		//Thread.sleep(30000);
		StringEntity entity = new StringEntity(gitApiPayloadJson, "UTF-8");
		 //HttpPut httpPut = new HttpPut("https://api.github.com/repos/bijiac/bijirepo/branches/dev/protection");
		 HttpPut httpPut = new HttpPut(branchUrl);
		 httpPut.addHeader("content-type", "application/json");
		 httpPut.addHeader("Accept", "application/vnd.github.luke-cage-preview+json");
		 httpPut.addHeader("Authorization", "token f13342fd28cd3fb086322c1fb2bedec00bdd4d85");
		 httpPut.setEntity(entity);
		 
		 HttpResponse response = client.execute(httpPut);
		 System.out.println("Executed.......");
		  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  while ((line = rd.readLine()) != null) {
			  System.out.println("HELLO");  
		    System.out.println(line);
		  }
		  
		  rd.close();
		  rd=null;
		  httpPut =null;
		  entity=null;
		  mentions(gitMentionsJson,gitUrl);
		 
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}}
	   return null;
   }
}