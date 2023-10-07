package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;

public class CdkWorkshopStack extends Stack {
    public CdkWorkshopStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public CdkWorkshopStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        //  // Defines a new lambda resource
        //  final Function hello = Function.Builder.create(this, "HelloHandler")
        // //  .runtime(Runtime.NODEJS_18_X)    // execution environment
        //  .runtime(Runtime.JAVA_11)    // execution environment
        //  .code(Code.fromAsset("HelloWorldFunction"))  // code loaded from the "lambda" directory
        //  .handler("App.handler")        // file is "hello", function is "handler"
        //  .build();

        //  // Defines an API Gateway REST API resource backed by our "hello" function
        // LambdaRestApi.Builder.create(this, "Endpoint")
        // .handler(hello)
        // .build();

         // Define Lambda function
         Function lambdaFunction = Function.Builder.create(this, "SampleLambdaFunction")
         .runtime(Runtime.JAVA_11)
         .code(Code.fromAsset("HelloWorldFunction"))
         .handler("com.example.YourHandler::handleRequest")
         .build();

 // Create API Gateway and associate with Lambda function
        LambdaRestApi.Builder.create(this, "SampleApi")
         .handler(lambdaFunction)
         .build();

        
    }
}
