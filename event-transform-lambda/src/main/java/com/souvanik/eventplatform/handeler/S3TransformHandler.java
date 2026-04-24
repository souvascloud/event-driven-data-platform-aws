package com.souvanik.eventplatform.handeler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.souvanik.eventplatform.service.S3Service;
import com.souvanik.eventplatform.service.TransformService;
import com.souvanik.eventplatform.service.impl.S3ServiceImpl;
import com.souvanik.eventplatform.service.impl.TransformServiceImpl;
import com.souvanik.eventplatform.transformer.EventTransformer;
import com.souvanik.eventplatform.transformer.factory.TransformerFactory;
import com.souvanik.eventplatform.transformer.impl.AppCrashTransformer;
import com.souvanik.eventplatform.transformer.impl.OrderPlacedTransformer;
import com.souvanik.eventplatform.transformer.impl.PaymentFailedTransformer;
import com.souvanik.eventplatform.transformer.impl.UserLoginTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;


public class S3TransformHandler implements RequestHandler<S3Event, String> {

    private static final Logger logger = LoggerFactory.getLogger(S3TransformHandler.class);
    private final TransformService transformService;

    public S3TransformHandler() {
        S3Service s3Service = new S3ServiceImpl();
        List<EventTransformer> transformers = Arrays.asList(
                new OrderPlacedTransformer(),
                new PaymentFailedTransformer(),
                new UserLoginTransformer(),
                new AppCrashTransformer());
        TransformerFactory factory = new TransformerFactory(transformers);

        this.transformService = new TransformServiceImpl(s3Service, factory);
    }

    @Override
    public String handleRequest(S3Event event, Context context) {

        logger.info("event=lambda_start requestId={}", context.getAwsRequestId());

        try {
            transformService.process(event);

            logger.info("event=lambda_success requestId={}", context.getAwsRequestId());
            return "SUCCESS";

        } catch (Exception e) {
            logger.error("event=lambda_failed requestId={}", context.getAwsRequestId(), e);
            throw new RuntimeException(e);
        }
    }
}