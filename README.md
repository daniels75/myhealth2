# myhealth


1. bower install bootstrap-material-design --save
1.1. grunt wiredep
1.2 removed line from index.html
 <script src="bower_components/bootstrap-sass/assets/javascripts/bootstrap.js"></script>
 
2.
Everything is configured, generating the entity...
 v  create .jhipster\Points.json
 
   create src\main\java\org\daniels\jhipster\myhealth\domain\Points.java
   create src\main\java\org\daniels\jhipster\myhealth\repository\PointsRepository.java
   create src\main\java\org\daniels\jhipster\myhealth\web\rest\PointsResource.java
   
   create src\main\resources\config\liquibase\changelog\20151217173640_added_entity_Points.xml
   
v   create src\main\webapp\scripts\app\entities\points\pointss.html
v   create src\main\webapp\scripts\app\entities\points\points-detail.html
v  create src\main\webapp\scripts\app\entities\points\points-dialog.html
   create src\main\webapp\scripts\app\entities\points\points-delete-dialog.html
   
   create src\main\webapp\scripts\app\entities\points\points.js
   create src\main\webapp\scripts\app\entities\points\points.controller.js
   create src\main\webapp\scripts\app\entities\points\points-dialog.controller.js
   create src\main\webapp\scripts\app\entities\points\points-delete-dialog.controller.js
   create src\main\webapp\scripts\app\entities\points\points-detail.controller.js
   create src\test\javascript\spec\app\entities\points\points-detail.controller.spec.js
v create src\main\webapp\scripts\components\entities\points\points.service.js
   
   create src\test\java\org\daniels\jhipster\myhealth\web\rest\PointsResourceIntTest.java
   create src\test\gatling\simulations\PointsGatlingTest.scala
   
v   create src\main\webapp\i18n\en\points.json
v   create src\main\webapp\i18n\fr\points.json
   
   look out
   search classes
   Date Json formatter - in User etc
   search nie dziala
   add missing tests from 21points
   
 3. database change -> liquibaseDiffChangelog
 

This application was generated using JHipster, you can find documentation and help at [https://jhipster.github.io](https://jhipster.github.io).

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Grunt][] as our build system. Install the grunt command-line tool globally with:

    npm install -g grunt-cli

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.

# Building for production

To optimize the myhealth client for production, run:

    mvn -Pprod clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

# Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript` and can be run with:

    grunt test



# Continuous Integration

To setup this project in Jenkins, use the following configuration:

* Project name: `myhealth`
* Source Code Management
    * Git Repository: `git@github.com:xxxx/myhealth.git`
    * Branches to build: `*/master`
    * Additional Behaviours: `Wipe out repository & force clone`
* Build Triggers
    * Poll SCM / Schedule: `H/5 * * * *`
* Build
    * Invoke Maven / Tasks: `-Pprod clean package`
* Post-build Actions
    * Publish JUnit test result report / Test Report XMLs: `build/test-results/*.xml`

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
