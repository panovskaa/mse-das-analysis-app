FROM ubuntu:18.04

# install docker-ce (from tutorial for ubuntu)
RUN apt-get update -y && \
    apt-get install ca-certificates curl gnupg lsb-release -y && \
    mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg && \
    echo \
    "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" |\
      tee /etc/apt/sources.list.d/docker.list > /dev/null && \
    apt-get update -y && \
    apt-get install -y docker-ce docker-ce-cli containerd.io

# install docker compose
RUN curl -L "https://github.com/docker/compose/releases/download/1.27.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
    chmod 777 /usr/local/bin/docker-compose

# copy the root (i.e., repository root) to /root in the runner
COPY . ./

# TODO: log in to docker hub
# See https://webapp.io/docs/advanced-workflows#logging-in-to-docker to learn how to log in to docker

RUN docker-compose build

# TODO: change port to whatever is configured in your docker-compose.yml
EXPOSE 8080
ENTRYPOINT ['docker-compose up']