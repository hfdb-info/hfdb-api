# hfdb-api

API layer for HFDB. This program allows the front end to grab the data from the database in a RESTful fashion

## Development Setup

First and foremost, all the applications need to be setup. In order to do this, chocolatey needs to be setup if not already done so. Open powershell (as admin) and run the following command: `Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))`
**Do not click out of powershell and keep it in the foreground until it is finished**

Chocolatey is finished installing, close and reopen powershell (as admin) and run the following command:
`choco install git github-desktop maven openjdk vscode -y`
**Do not click out of powershell and keep it in the foreground until it is finished**

Some of these programs update on their own so run this command to pin them. It is highly recommended. Run the following commands to do so.
`choco pin add -n=vscode`
`choco pin add -n=github-desktop`

We are now finished with powershell so it can be closed.

Next the repository needs to be cloned. Open github desktop and sign into it with your github account. Once signed in, clone this repository to a folder somewhere.

Once github desktop has finished cloning the repository, make sure the branch selected is `development` **and not** `main` **or** `original template` **or any other branch if any exist**. The latter branches are reserved for special uses. Unless Justin says otherwise, only use the `development` branch.

Once visual studio code opens, it'll have loaded the directory as a project. The files structure in the explorer pane in vscode should match the directory exactly how it appears in the repo on github.

The next step to be accomplished is installing all the necessary extensions. With vscode open, hit ctrl+p and copy/paste in the following command and hit enter:
`ext install vscjava.vscode-java-pack esbenp.prettier-vscode Pivotal.vscode-boot-dev-pack`

Once the extensions are finished installing, the last thing to do is setup the `application.properties` file. Navigate to `src/main/resources/application.properties.example` in the project directory, copy/paste it, and remove `.example` from the name. **Do not delete or rename the original file!** In application.properties, where it says password, replace it with the password to your local instance of postgres.

From there you should be good to start working.

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

## FAQ

Q: When I load up the project in vscode after cloning the repo, vscode is throwing errors such as `Unbound classpath container`, `The type java.lang.Object cannot be resolved`, and numerous other errors.
A: At this time, the best solution for this is to backup your data and reinstall windows from scratch. Nathan had this issue when he first cloned the repo. Justin went through and performed a clean install of Windows 10 in a VM following the instructions as described in the setup and couldn't reproduce the issue. If anyone encounters this issue, they're welcome to attempt to fix it theirself, (if they successfully do so, great! Report it to Justin so this can be updated.) but deadlines will not be adjusted to work around doing so.
