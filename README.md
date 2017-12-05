sbt-package-client
==========

Allows you to script the packaging of your `client` project before the packaging of the `server`.

To use this plugin use the addSbtPlugin command within your project's plugins.sbt (or as a global setting) i.e.:

    addSbtPlugin("org.max-peroch" % "sbt-package-client" % "0.0.1")

And add :

    resolvers += Resolver.bintrayIvyRepo("max-peroch","sbt-plugins")


By default, the plugin assume your `client` directory location in `baseDirectory.value / "client"`, but you can override it with `clientDirectory`, ex.: 

    clientDirectory := baseDirectory.value / "myReactApp"

Then your script can goes like :

    buildCommands := Seq("npm install", "npm build")

Not related to the plugin, but in case the output of your packaged client is not in the `public` directory you can add its location like this :

    unmanagedResourceDirectories in Assets += baseDirectory.value / "client" / "dist"