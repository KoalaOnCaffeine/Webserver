package me.tomnewton.routes

import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import java.io.File

fun Route.staticRoutes() {
    // Serve under the root directory
    static("/") {
        // staticRoot=C:\Users\tomto\IdeaProjects\clientside\dist
        val staticRoot = System.getenv("staticRoot")
        staticRootFolder = File(staticRoot)
        files(".") // Serve all by default
        serveWithoutExtension(staticRootFolder!!)
    }
}

// Recursively checks for index.html files, and serve them under their directory relative to the static root, so that /folder/ returns /folder/index.html
private fun Route.serveWithoutExtension(root: File) {
    root.listFiles()?.forEach {
        if (it.isDirectory) serveWithoutExtension(it)
        // Serve index files under their parent's path
        else if (it.nameWithoutExtension == "index") {
            val relativePath = "/" + root.relativeTo(staticRootFolder!!).name
            file(relativePath, it)
        }
    }
}