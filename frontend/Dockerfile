FROM node:latest as node


ARG publicStripeKey
ENV publicStripeKeyEnv="$publicStripeKey"


WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --prod


# stage 2
FROM nginx:alpine as NginX

ARG publicStripeKey
ENV publicStripeKeyEnv="$publicStripeKeyEnv"

COPY --from=node /app/dist/my-app /usr/share/nginx/html



