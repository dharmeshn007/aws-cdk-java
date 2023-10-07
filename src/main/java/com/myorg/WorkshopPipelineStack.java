package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.codebuild.*;
import software.amazon.awscdk.services.codecommit.*;
import software.amazon.awscdk.services.codepipeline.*;
import software.amazon.awscdk.services.codepipeline.actions.*;
import java.util.*;
import static software.amazon.awscdk.services.codebuild.LinuxBuildImage.AMAZON_LINUX_2;
import  software.amazon.awscdk.SecretValue;

public class WorkshopPipelineStack extends Stack {
    public WorkshopPipelineStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public WorkshopPipelineStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Bucket artifactsBucket = new Bucket(this, "JavaCDKArtifactsBucketLambda");

        IRepository codeRepo = Repository.fromRepositoryName(this, "AppRepository", "sam-app");

    Pipeline pipeline = new Pipeline(this, "Pipeline", PipelineProps.builder()
        .artifactBucket(artifactsBucket).build());

    Artifact sourceOutput = new Artifact("sourceOutput");

    GitHubSourceAction sourceAction = GitHubSourceAction.Builder.create()
                .actionName("GitHub_Source")
                .owner("dharmeshn007")
                .repo("aws-cdk-java")
                .branch("main")
                .oauthToken(SecretValue.secretsManager("aws-cdk"))
                .output(sourceOutput)
                .build();

    pipeline.addStage(StageOptions.builder()
        .stageName("Source")
        .actions(Collections.singletonList(sourceAction))
        .build());

       

    // Declare build output as artifacts
Artifact buildOutput = new Artifact("buildOutput");

// Declare a new CodeBuild project
PipelineProject buildProject = new PipelineProject(this, "Build", PipelineProjectProps.builder()
        .environment(BuildEnvironment.builder()
                .buildImage(AMAZON_LINUX_2).build())
        .environmentVariables(Collections.singletonMap("PACKAGE_BUCKET", BuildEnvironmentVariable.builder()
                .value(artifactsBucket.getBucketName())
                .build()))
        .build());

// Add the build stage to our pipeline
CodeBuildAction buildAction = new CodeBuildAction(CodeBuildActionProps.builder()
        .actionName("Build")
        .project(buildProject)
        .input(sourceOutput)
        .outputs(Collections.singletonList(buildOutput))
        .build());

pipeline.addStage(StageOptions.builder()
        .stageName("Build")
        .actions(Collections.singletonList(buildAction))
        .build());

// // Deploy stage
// CloudFormationCreateReplaceChangeSetAction createChangeSet = new CloudFormationCreateReplaceChangeSetAction(CloudFormationCreateReplaceChangeSetActionProps.builder()
//         .actionName("CreateChangeSet")
//         .templatePath(buildOutput.atPath("packaged.yaml"))
//         .stackName("AppStack")
//         .adminPermissions(true)
//         .changeSetName("sam-app-dev-changeset")
//         .runOrder(1)
//         .build());

// CloudFormationExecuteChangeSetAction executeChangeSet = new CloudFormationExecuteChangeSetAction(CloudFormationExecuteChangeSetActionProps.builder()
//         .actionName("Deploy")
//         .stackName("AppStack")
//         .changeSetName("sam-app-dev-changeset")
//         .runOrder(2)
//         .build());

// pipeline.addStage(StageOptions.builder()
//         .stageName("Dev")
//         .actions(Arrays.asList(createChangeSet, executeChangeSet))
//         .build());
    }
}