# hfdb-api
API layer for HFDB. This program allows the front end to grab the data from the database in a RESTful fashion

## Development Setup
First and foremost, the repository needs to be cloned to your local machine. The easiest way to do that is to [install github desktop](https://desktop.github.com/)

After installing github desktop and cloning it to your local machine, you'll need to have installed visual studio code. [Install visual studio code here.](https://code.visualstudio.com/)

Once vscode is installed, find the resulting directory where the repository was cloned and open it (It will be called hfdb-api). Once it's opened in explorer, right-click -> Open with code

Once visual studio code opens, it'll have loaded the directory as a project. The files structure in the explorer pane in vscode should match the directory exactly how it appears in the repo on github.

Finally for the last step, all the necessary extensions need to be installed. hit ctrl+p and copy/paste in the following command and hit enter:
`ext install vscjava.vscode-java-pack esbenp.prettier-vscode Pivotal.vscode-spring-boot`

Once the extensions are finished installing, everything should be good to go.

## Quick Tour

Maven and Spring wrap everything in a lot of project files which can make things confusing. To simplify things, most coding will be done in the directory `src/main/java/info/hfdb/hfdbapi/`.
Here there exists the file `HfdbApiApplication.java` where the main method lives and the package `Controller` which contains some example code I (Justin) have provided, particularly the classes `HFDBAPI.java` and `Status.java`.

Opening `HFDBAPI.java` reveals a class with the annotation `@RestController` and a couple of functions with the annotation `@RequestMapping("/somePath")`.
Basically when Strings and primatives are returned, they are returned as Text, however when Objects are returned, as in the case of the function, `getStatus()`,
it returns the class that has been serialized in JSON.

## Running/Debugging
Hitting F5 starts up the project and reveal the debug menu at the top that'll allow you to pause, Step over/into/out, restart, and stop execution where applicable.
Since the project will contain functions with the annotation `@RequestMapping("/somePath")`, it'll respond to the according HTTP requests. The default IP is `localhost:8080`
so when you hit F5 and start the project and navigate to `localhost:8080/somePath`, the project will respond to the HTTP requests as defined by the RequestMapping functions.
