# pull official base image
FROM nikolaik/python-nodejs:latest
RUN mkdir -p /frontend
# set working directory
WORKDIR /frontend
COPY ./frontend/ ./
COPY frontend/package.json frontend/yarn.lock ./
RUN yarn cache clean && yarn
