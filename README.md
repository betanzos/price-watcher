# Price Watcher
Online stores price watcher.

The main idea is that you can create observers for those products you want to buy but at a lower price. Price Watcher will send you a notification email (if enable) when a product price is less than expected (require some configuration).

Price Watcher have a very simple and responsive web interface for add, remove and list product watchers. All of there are stored in a H2 database which is persisted in user home directory.

Created for my wife, Helen (again!). :grinning:

# Features
Current version is 1.0.
- Add watcher for products in online stores (current version only for Amazon)
- Update product prices every 12 hours in foreground
- Send notification email if current product price is lower than, or equal to, expected price
- Remove product watchers

# How to use
## JAR file
You can to obtain an Uber-JAR by compiling the project using Maven and, at least, JDK 11 or 
downloading one from dist directory.

The official JARs have been built with JDK 11.

### Compiling with Maven
```
> mvn clean package
```
JAR file will be generated into `target` directory.

### Run from JAR file
In order to run the app must use the following:
```
> java -jar price-watcher-<version>.jar
```

This command run an Apache Tomcat server on port 8080 which can be changed using the parameter
`--server.port` (e.g. `--server.port=80`).

### Setting up email notifications
E-Mail notifications are very useful since you don't need go to the WEB in order to know the last updated price for your products.

There are 7 parameters you need to specify for enable this notifications.
- `spring.mail.host`: (default `smtp.server.com`) SMTP server host name or IP
- `spring.mail.port`: (default `25`) SMTP server port
- `spring.mail.username`: (deafult `mailfrom@server.es`) SMTP server user name
- `spring.mail.password`: (deafult `password`) Password for the user
- `spring.mail.properties.mail.smtp.auth`: (default `true`) If the server require authentication
- `spring.mail.properties.mail.smtp.starttls.enable`: (default true) If true, enables the use of the STARTTLS command (if supported by the server) to switch the connection to a TLS-protected connection before issuing any login commands. If the server does not support STARTTLS, the connection continues without the use of TLS. 
- `app.notification.mail.to`: (default `mailto@server.com`) E-Mail of the person to notify

The following command enable email notifications:
```
> java -jar price-watcher-<version>.jar \
--spring.mail.host="my-smtp.com" \
--spring.mail.port=587 \
--spring.mail.username="my.email@myserver.com" \
--spring.mail.password="secret" \
--app.notification.mail.to="another.email@gmail.com"
```

**Note that** you can exclude those parameters which value will be default.

## Docker
### Making the image
You can make your own docker image using `docker-build.sh` script. Once you have the image you can use
`docker-run.sh` script for run one instance of it.

Note that `docker-run.sh` use the following parameter in order to bind the current user's home directory with root user's directory inside 
the container, to persist application database out of the container: `-v ~/:/root`. This parameter is optional but useful if you want to save your data and reused it with other containers. 

### From Docker Hub
If you don't want to built the image by yourself, official versions are published in 
[ebetanzos/price-watcher](https://hub.docker.com/r/ebetanzos/price-watcher) [Docker Hub](https://hub.docker.com/) 
repository.

The following command copy the image from Docker Hub to your local machine:
```
> docker pull ebetanzos/price-watcher[:tag]
```

Now you can run one instance of this image using `docker-run.sh` script changing image name to `ebetanzos\price-watcher[:tag]`.

### E-Mail notifications
Requires that some environment variables be added to `docker run` command, like this:

```
docker run --name 'price-watcher' --restart on-failure:3 -d -p 80:8080 \
-v ~/:/root \
-e SPRING_MAIL_HOST=my-smtp.com \
-e SPRING_MAIL_PORT=587 \
-e SPRING_MAIL_USERNAME=my.email@myserver.com \
-e SPRING_MAIL_PASSWORD=secret \
-e APP_NOTIFICATION_MAIL_TO=another.email@gmail.com \
price-watcher[:tag]
```

For more details see [Setting up email notifications](#setting-up-email-notifications) with JAR files.

# License
Price Watcher is Open Source software released under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).