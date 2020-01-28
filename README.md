# repoDemoWebservice_Webhook
Demo Webservice

1) webhook.jar Webservice package is in the branch awasam1-patch-1 under repository repoDemoWebservice_Webhook.
2) All dependable files in lib folder for LambdaRequestHandler class in root directory under webhook.jar.
3) Create lambda function fidalylambdaFunc in aws cloud selecting Java8 jar -> webhook.jar and specifying handler as example.LambdaRequestHandler::myHandler.
4) Upload webhook.jar in create lambda function console in aws cloud.
5) Now search API gateway in aws cloud.
6) Select left bottom Rest API.
7) Create API in aws cloud.
8) Click action and create post method. Click on tick mark post.
9) select fidalylambdafunct and save.
10) Select depoy stage and enter test. And click deploy.
11) Copy from top URL -->    Invoke URL: https://i2oyk7c639.execute-api.us-east-2.amazonaws.com/test
12) This will be the webhook URL to be used in GitHUb while creating webhook.
13) First have org and team created.
14) Create webhook under organization fidaly in Github.
15) Copy lambda url, select json type and select 'create repository' checkbox.
16) Go from profile level top setting to developer setting. select personal token. Generate lambdatoken. This token will be used in LamdaRequestHandler webservice for aws <--> Github communication.
17) In webservice mention is post method. Protection is put method. Github to aws is post method.
18) Now we can test by creating repository inside fidaly organization in Github. As soon as repostory is created, we traverse through branch screen and can observe that the 'Master' branch appears under Protected branch.
