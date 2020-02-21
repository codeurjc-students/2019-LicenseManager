# Base Image
FROM openjdk:8-jre-alpine

ARG privateStripeKey
ENV privateStripeKeyEnv="$privateStripeKey"



# Copy App
COPY build/license-web-0.5.0.jar /
COPY images /images

# Expose port number
EXPOSE 8080

#  Run app
CMD ["sh","-c","java -jar license-web-0.5.0.jar $privateStripeKeyEnv"]
