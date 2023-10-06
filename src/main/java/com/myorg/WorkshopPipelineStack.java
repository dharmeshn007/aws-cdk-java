package com.myorg;

import java.util.List;
import java.util.Map;

import software.constructs.Construct;
import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.pipelines.CodeBuildStep;
import software.amazon.awscdk.pipelines.CodePipeline;
import software.amazon.awscdk.pipelines.CodePipelineSource;
import software.amazon.awscdk.services.codepipeline.sources.GitHubSource;
import software.amazon.awscdk.services.codecommit.Repository;

public class WorkshopPipelineStack extends Stack {
    public WorkshopPipelineStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public WorkshopPipelineStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        // The basic pipeline declaration. This sets the initial structure
        // of our pipeline
        final CodePipeline pipeline = CodePipeline.Builder.create(this, "Pipeline")
                .pipelineName("WorkshopPipeline")
                .synth(CodeBuildStep.Builder.create("SynthStep")
                        .input(GitHubSource.Builder.create()
                            .owner("dharmeshn007")                  // GitHub owner or organization name
                            .repo("aws-cdk-java")                    // GitHub repository name
                            .branch("main")                  // Branch to monitor for changes
                            .authenticationToken(githubAccessTokenSecret.secretValueFromJson("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")) // GitHub personal access token
                            .build())
                        .installCommands(List.of(
                                "npm install -g aws-cdk"   // Commands to run before build
                        ))
                        .commands(List.of(
                                "mvn package",            // Language-specific build commands
                                "npx cdk synth"           // Synth command (always same)
                        )).build())
                .build();
    }
}