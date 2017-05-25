![TOREADOR logo](http://www.toreador-project.eu/wp-content/themes/acqualiofilizzata/images/logo-header.png)
# Docker image to install PHP 7.0 and Apache 2

### Installation
Clone the TOREADOR repository locally. To do so use the following command:

```sh
$ git clone https://github.com/kmantzoukas/TOREADOR.git
```

#### Build the docker image
In order to build the docker image navigate to the project's directory and use [Docker](https://www.docker.com/) to build it. The image is tagged under the name toreador-webui. For more details check the corresponding [Dockerfile](/blob/master/DockerApachePHP/Dockerfile)
```sh
$ cd TOREADOR/DockerApachePHP
$ docker build -t toreador-webui .
```
#### Run a container from the docker image
In order to run a container from the image above, navigate to the project's directory and use [Docker](https://www.docker.com/) to run it:
```sh
$ cd TOREADOR/DockerApachePHP
$ docker run -p 80:80 -d -v $PWD/www:/var/www/site toreador-webui
```
#### Access the web interface
In order to access the deployed web application go [here](http://soi-vm-test1.nsqdc.city.ac.uk/toreador/index.php).
