package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;

public class CdkWorkshopStack extends Stack {
    public CdkWorkshopStack(final Construct scope, final String id) {
        super(scope, id);

        Function helloWorldLambda = Function.Builder.create(this, "HelloWorldLambda")
                .runtime(Runtime.JAVA_11)
                .handler("com.example.HelloWorldLambda::handleRequest")
                .code(Code.fromAsset("../cdk-workshop/hello-world-lambda/target/hello-world-lambda-1.0-SNAPSHOT.jar"))
                .build();

        // Create API Gateway
        RestApi api = RestApi.Builder.create(this, "HelloWorldAPI")
                .restApiName("HelloWorldAPI")
                .build();

        // LambdaRestApi.Builder.create(this, "Endpoint")
        //         .handler(helloWorldLambda)
        //         .build();

        // Define API Gateway method and integrate with Lambda function
        LambdaIntegration integration = LambdaIntegration.Builder.create(helloWorldLambda)
                .build();

        api.getRoot().addMethod("GET", integration);
    }
}